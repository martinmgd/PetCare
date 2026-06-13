package com.example.petcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.util.MascotasUtils;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MedicacionDAO;
import com.example.petcare.dao.PesoDAO;
import com.example.petcare.dao.VacinaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.MedicacionDTO;
import com.example.petcare.dto.PesoDTO;
import com.example.petcare.dto.VacinaDTO;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class HistorialSaudeActivity extends AppCompatActivity {

    private Toolbar toolbarHistorialSaude;

    private MaterialAutoCompleteTextView txtMascotaHistorial;
    private TextView txtHistorialSaude;
    private EventoDAO eventoDAO;
    private PesoDAO pesoDAO;
    private VacinaDAO vacinaDAO;
    private MedicacionDAO medicacionDAO;

    private ArrayList<MascotaDTO> listaMascotas;
    private MascotaDTO mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historial_saude);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        eventoDAO = db.eventoDAO();
        pesoDAO = db.pesoDAO();
        vacinaDAO = db.vacinaDAO();
        medicacionDAO = db.medicacionDAO();

        // Referencias XML
        toolbarHistorialSaude = findViewById(R.id.toolbarHistorialSaude);
        txtMascotaHistorial = findViewById(R.id.txtMascotaHistorial);
        txtHistorialSaude = findViewById(R.id.txtHistorialSaude);

        // Botón volver
        toolbarHistorialSaude.setNavigationOnClickListener(v -> finish());

        // Lista de mascotas accesibles
        listaMascotas = new ArrayList<>();

        cargarMascotasPermitidas();
        configurarSelectorMascota();
    }

    // Cargar mascotas propias y compartidas aceptadas
    private void cargarMascotasPermitidas() {

        listaMascotas =
                MascotasUtils.obtenerMascotasDisponibles(
                        this
                );
    }

    // Configurar desplegable de mascotas
    private void configurarSelectorMascota() {

        ArrayList<String> nombresMascotas =
                new ArrayList<>();

        for (MascotaDTO mascota : listaMascotas) {

            nombresMascotas.add(
                    "   " + mascota.getNombre()
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombresMascotas
                );

        txtMascotaHistorial.setAdapter(adapter);

        txtMascotaHistorial.setFocusable(false);
        txtMascotaHistorial.setClickable(true);
        txtMascotaHistorial.setCursorVisible(false);

        // Al seleccionar una mascota, se carga su historial
        txtMascotaHistorial.setOnItemClickListener((parent, view, position, id) -> {

            mascotaSeleccionada =
                    listaMascotas.get(position);

            cargarHistorial();
        });

        // Si no hay mascotas, mostrar aviso
        if (listaMascotas.isEmpty()) {

            txtHistorialSaude.setText("Non hai mascotas dispoñibles");

            Toast.makeText(
                    this,
                    "Non hai mascotas dispoñibles",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            // Seleccionar la primera mascota por defecto
            mascotaSeleccionada =
                    listaMascotas.get(0);

            txtMascotaHistorial.setText(
                    "   " + mascotaSeleccionada.getNombre(),
                    false
            );

            cargarHistorial();
        }
    }

    // Cargar historial de salud de la mascota seleccionada
    private void cargarHistorial() {

        if (mascotaSeleccionada == null) {

            txtHistorialSaude.setText("Selecciona unha mascota");
            return;
        }

        ArrayList<ItemHistorial> historial =
                new ArrayList<>();

        cargarPesos(historial);
        cargarVacinas(historial);
        cargarMedicaciones(historial);
        cargarEventos(historial);

        // Ordenar por fecha descendente
        Collections.sort(
                historial,
                (item1, item2) ->
                        item2.getFechaOrden()
                                .compareTo(
                                        item1.getFechaOrden()
                                )
        );

        if (historial.isEmpty()) {

            txtHistorialSaude.setText("Sen rexistros no historial");
            return;
        }

        StringBuilder textoHistorial =
                new StringBuilder();

        for (int i = 0; i < historial.size(); i++) {

            textoHistorial.append(
                    historial.get(i).getTexto()
            );

            if (i < historial.size() - 1) {

                textoHistorial.append("\n\n");
            }
        }

        txtHistorialSaude.setText(
                textoHistorial.toString()
        );
    }

    // Añadir pesos al historial
    private void cargarPesos(ArrayList<ItemHistorial> historial) {

        ArrayList<PesoDTO> pesos =
                new ArrayList<>(
                        pesoDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        for (PesoDTO peso : pesos) {

            historial.add(
                    new ItemHistorial(
                            peso.getFecha(),
                            peso.getFecha()
                                    + "  Peso: "
                                    + peso.getPeso()
                                    + " kg"
                    )
            );
        }
    }

    // Añadir vacunas al historial desde VacinaDTO
    private void cargarVacinas(ArrayList<ItemHistorial> historial) {

        ArrayList<VacinaDTO> vacinas =
                new ArrayList<>(
                        vacinaDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        for (VacinaDTO vacina : vacinas) {

            String texto =
                    vacina.getFecha()
                            + "  Vacuna: "
                            + vacina.getNombre();

            if (vacina.getNotas() != null
                    && !vacina.getNotas().isEmpty()) {

                texto =
                        texto
                                + "\n"
                                + vacina.getNotas();
            }

            historial.add(
                    new ItemHistorial(
                            vacina.getFecha(),
                            texto
                    )
            );
        }
    }

    // Añadir medicaciones al historial desde MedicacionDTO
    private void cargarMedicaciones(ArrayList<ItemHistorial> historial) {

        ArrayList<MedicacionDTO> medicaciones =
                new ArrayList<>(
                        medicacionDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        for (MedicacionDTO medicacion : medicaciones) {

            String texto =
                    medicacion.getFechaInicio();

            if (medicacion.getFechaFin() != null
                    && !medicacion.getFechaFin().isEmpty()) {

                texto =
                        texto
                                + " - "
                                + medicacion.getFechaFin();
            }

            texto =
                    texto
                            + "  Medicación: "
                            + medicacion.getNombre();

            if (medicacion.getDosis() != null
                    && !medicacion.getDosis().isEmpty()) {

                texto =
                        texto
                                + "\nDosis: "
                                + medicacion.getDosis();
            }

            if (medicacion.getNotas() != null
                    && !medicacion.getNotas().isEmpty()) {

                texto =
                        texto
                                + "\n"
                                + medicacion.getNotas();
            }

            historial.add(
                    new ItemHistorial(
                            medicacion.getFechaInicio(),
                            texto
                    )
            );
        }
    }

    // Añadir eventos sanitarios evitando duplicar vacunas y medicacion
    private void cargarEventos(ArrayList<ItemHistorial> historial) {

        ArrayList<EventoDTO> eventos =
                new ArrayList<>(
                        eventoDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        for (EventoDTO evento : eventos) {

            if (evento.getTipo() != null
                    && (
                    evento.getTipo().equals("Vacuna")
                            || evento.getTipo().equals("Medicación")
                    )) {

                continue;
            }

            String texto =
                    evento.getFecha()
                            + "  "
                            + evento.getTipo();

            if (evento.getTitulo() != null
                    && !evento.getTitulo().isEmpty()) {

                texto =
                        texto + ": " + evento.getTitulo();
            }

            historial.add(
                    new ItemHistorial(
                            evento.getFecha(),
                            texto
                    )
            );
        }
    }

    // Convertir fecha String a Date para ordenar correctamente
    private Date convertirFecha(String fecha) {

        try {

            return new SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
            ).parse(fecha);

        } catch (Exception e) {

            return new Date(0);
        }
    }

    // Clase interna para mezclar pesos, vacunas, medicaciones y eventos
    private class ItemHistorial {

        private String fecha;
        private String texto;

        public ItemHistorial(
                String fecha,
                String texto
        ) {

            this.fecha = fecha;
            this.texto = texto;
        }

        public Date getFechaOrden() {

            return convertirFecha(fecha);
        }

        public String getTexto() {

            return texto;
        }
    }
}