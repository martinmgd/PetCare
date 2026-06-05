package com.example.petcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PesoActivity extends AppCompatActivity {

    private Toolbar toolbarPeso;

    private MaterialAutoCompleteTextView txtMascotaPeso;
    private TextInputEditText txtNovoPeso;

    private TextView txtPesoActual;
    private TextView txtGraficoPeso;
    private TextView txtHistorialPeso;

    private MaterialButton btnGuardarPeso;

    private MascotaDAO mascotaDAO;
    private ArrayList<MascotaDTO> mascotas;

    private MascotaDTO mascotaSeleccionada;

    private ArrayList<String> historialPeso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peso);

        mascotaDAO = new MascotaDAO();
        mascotas = mascotaDAO.listar();

        historialPeso = new ArrayList<>();

        // Referencias XML
        toolbarPeso = findViewById(R.id.toolbarPeso);
        txtMascotaPeso = findViewById(R.id.txtMascotaPeso);
        txtNovoPeso = findViewById(R.id.txtNovoPeso);
        txtPesoActual = findViewById(R.id.txtPesoActual);
        txtGraficoPeso = findViewById(R.id.txtGraficoPeso);
        txtHistorialPeso = findViewById(R.id.txtHistorialPeso);
        btnGuardarPeso = findViewById(R.id.btnGuardarPeso);

        // Botón de volver da barra superior
        toolbarPeso.setNavigationOnClickListener(v -> finish());

        // Configuración do despregable de mascotas
        configurarSelectorMascota();

        // Gardar novo peso
        btnGuardarPeso.setOnClickListener(v -> guardarPeso());
    }

    // Configura o despregable coas mascotas dispoñibles
    private void configurarSelectorMascota() {

        ArrayList<String> nomesMascotas = new ArrayList<>();

        for (MascotaDTO mascota : mascotas) {

            nomesMascotas.add(mascota.getNombre());
        }

        ArrayAdapter<String> adapterMascotas =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        nomesMascotas
                );

        txtMascotaPeso.setAdapter(adapterMascotas);

        txtMascotaPeso.setFocusable(false);
        txtMascotaPeso.setClickable(true);
        txtMascotaPeso.setCursorVisible(false);

        txtMascotaPeso.setOnItemClickListener((parent, view, position, id) -> {

            mascotaSeleccionada = mascotas.get(position);

            actualizarPesoActual();
            actualizarHistorial();
            actualizarGrafico();
        });

        if (mascotas.isEmpty()) {

            Toast.makeText(
                    this,
                    "Non hai mascotas rexistradas",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            mascotaSeleccionada = mascotas.get(0);
            txtMascotaPeso.setText(mascotaSeleccionada.getNombre(), false);

            actualizarPesoActual();
            actualizarHistorial();
            actualizarGrafico();
        }
    }

    // Actualiza o texto do peso actual da mascota seleccionada
    private void actualizarPesoActual() {

        if (mascotaSeleccionada == null) {

            txtPesoActual.setText("Sen rexistrar");
            return;
        }

        if (mascotaSeleccionada.getPesoActual() == 0) {

            txtPesoActual.setText("Sen rexistrar");

        } else {

            txtPesoActual.setText(
                    mascotaSeleccionada.getPesoActual() + " kg"
            );
        }
    }

    // Garda un novo peso e actualiza a mascota seleccionada
    private void guardarPeso() {

        if (mascotaSeleccionada == null) {

            Toast.makeText(
                    this,
                    "Selecciona unha mascota",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String textoPeso;

        if (txtNovoPeso.getText() != null) {

            textoPeso = txtNovoPeso.getText().toString().trim();

        } else {

            textoPeso = "";
        }

        if (textoPeso.isEmpty()) {

            txtNovoPeso.setError("Introduce o peso");
            return;
        }

        double peso;

        try {

            peso = Double.parseDouble(textoPeso.replace(",", "."));

        } catch (Exception e) {

            txtNovoPeso.setError("Introduce un peso válido");
            return;
        }

        if (peso <= 0) {

            txtNovoPeso.setError("O peso debe ser maior ca 0");
            return;
        }

        mascotaSeleccionada.setPesoActual(peso);

        String fecha =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                ).format(new Date());

        historialPeso.add(
                fecha + "  →  " + peso + " kg"
        );

        txtNovoPeso.setText("");

        actualizarPesoActual();
        actualizarHistorial();
        actualizarGrafico();

        Toast.makeText(
                this,
                "Peso gardado correctamente",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Actualiza o historial textual de pesos
    private void actualizarHistorial() {

        if (historialPeso.isEmpty()) {

            txtHistorialPeso.setText("Sen rexistros de peso");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (int i = historialPeso.size() - 1; i >= 0; i--) {

            texto.append(historialPeso.get(i));

            if (i > 0) {

                texto.append("\n\n");
            }
        }

        txtHistorialPeso.setText(texto.toString());
    }

    // Actualiza unha representación simple do gráfico de pesos
    private void actualizarGrafico() {

        if (historialPeso.size() < 2) {

            txtGraficoPeso.setText(
                    "O gráfico mostrarase cando existan varios rexistros de peso."
            );

        } else {

            txtGraficoPeso.setText(
                    "Gráfico de evolución pendente de implementar.\n\n" +
                            "Último peso rexistrado: " +
                            mascotaSeleccionada.getPesoActual() +
                            " kg"
            );
        }
    }
}