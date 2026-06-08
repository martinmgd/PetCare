package com.example.petcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.PesoDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.PesoDTO;
import com.example.petcare.view.GraficoPesoView;
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
    private TextView txtHistorialPeso;

    private GraficoPesoView graficoPesoView;

    private MaterialButton btnGuardarPeso;

    private MascotaDAO mascotaDAO;
    private PesoDAO pesoDAO;

    private ArrayList<MascotaDTO> mascotas;

    private MascotaDTO mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peso);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        mascotaDAO = db.mascotaDAO();
        pesoDAO = db.pesoDAO();

        // Listas
        mascotas = new ArrayList<>();
        mascotas.addAll(mascotaDAO.listar());

        // Referencias XML
        toolbarPeso = findViewById(R.id.toolbarPeso);
        txtMascotaPeso = findViewById(R.id.txtMascotaPeso);
        txtNovoPeso = findViewById(R.id.txtNovoPeso);
        txtPesoActual = findViewById(R.id.txtPesoActual);
        txtHistorialPeso = findViewById(R.id.txtHistorialPeso);
        graficoPesoView = findViewById(R.id.graficoPesoView);
        btnGuardarPeso = findViewById(R.id.btnGuardarPeso);

        // Botón volver
        toolbarPeso.setNavigationOnClickListener(v -> finish());

        // Selector de mascotas
        configurarSelectorMascota();

        // Gardar peso
        btnGuardarPeso.setOnClickListener(v -> guardarPeso());
    }

    // Configurar selector de mascotas
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

            txtPesoActual.setText("Sen rexistrar");
            txtHistorialPeso.setText("Sen rexistros de peso");
            graficoPesoView.setPesos(new ArrayList<>());

        } else {

            mascotaSeleccionada = mascotas.get(0);
            txtMascotaPeso.setText(mascotaSeleccionada.getNombre(), false);

            actualizarPesoActual();
            actualizarHistorial();
            actualizarGrafico();
        }
    }

    // Actualizar peso actual
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

    // Gardar peso
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

        String fecha =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                ).format(new Date());

        String hora =
                new SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                ).format(new Date());

        // Crear rexistro de peso
        PesoDTO pesoDTO =
                new PesoDTO(
                        mascotaSeleccionada.getId(),
                        peso,
                        fecha,
                        hora
                );

        // Gardar rexistro en Room
        pesoDAO.insertar(pesoDTO);

        // Actualizar mascota
        mascotaSeleccionada.setPesoActual(peso);
        mascotaSeleccionada.setFechaActualizacion(System.currentTimeMillis());
        mascotaSeleccionada.setPendienteSync(true);

        // Gardar mascota en Room
        mascotaDAO.actualizar(mascotaSeleccionada);

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

    // Actualizar historial
    private void actualizarHistorial() {

        if (mascotaSeleccionada == null) {

            txtHistorialPeso.setText("Sen rexistros de peso");
            return;
        }

        ArrayList<PesoDTO> historial =
                new ArrayList<>(
                        pesoDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        if (historial.isEmpty()) {

            txtHistorialPeso.setText("Sen rexistros de peso");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < historial.size(); i++) {

            PesoDTO pesoDTO = historial.get(i);

            texto.append(pesoDTO.getFecha())
                    .append("  ")
                    .append(pesoDTO.getHora())
                    .append("  →  ")
                    .append(pesoDTO.getPeso())
                    .append(" kg");

            if (i < historial.size() - 1) {

                texto.append("\n\n");
            }
        }

        txtHistorialPeso.setText(texto.toString());
    }

    // Actualizar gráfico
    private void actualizarGrafico() {

        if (mascotaSeleccionada == null) {

            graficoPesoView.setPesos(new ArrayList<>());
            return;
        }

        ArrayList<PesoDTO> historial =
                new ArrayList<>(
                        pesoDAO.listarPorMascotaAsc(
                                mascotaSeleccionada.getId()
                        )
                );

        graficoPesoView.setPesos(historial);
    }
}