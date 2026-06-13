package com.example.petcare;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MedicacionDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.MedicacionDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.petcare.util.MascotasUtils;
import com.example.petcare.supabase.SupabaseSyncManager;

public class MedicacionActivity extends AppCompatActivity {

    private Toolbar toolbarMedicacion;

    private MaterialAutoCompleteTextView txtMascotaMedicacion;

    private TextInputEditText txtNombreMedicamento;
    private TextInputEditText txtDosis;
    private TextInputEditText txtFechaInicio;
    private TextInputEditText txtFechaFin;
    private TextInputEditText txtNotasMedicacion;

    private MaterialButton btnGuardarMedicacion;

    private TextView txtHistorialMedicacion;

    private MedicacionDAO medicacionDAO;
    private EventoDAO eventoDAO;

    private ArrayList<MascotaDTO> listaMascotas;
    private MascotaDTO mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicacion);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        medicacionDAO = db.medicacionDAO();
        eventoDAO = db.eventoDAO();

        // Referencias XML
        toolbarMedicacion = findViewById(R.id.toolbarMedicacion);

        txtMascotaMedicacion = findViewById(R.id.txtMascotaMedicacion);
        txtNombreMedicamento = findViewById(R.id.txtNombreMedicamento);
        txtDosis = findViewById(R.id.txtDosis);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        txtNotasMedicacion = findViewById(R.id.txtNotasMedicacion);

        btnGuardarMedicacion = findViewById(R.id.btnGuardarMedicacion);

        txtHistorialMedicacion = findViewById(R.id.txtHistorialMedicacion);

        // Botón volver
        toolbarMedicacion.setNavigationOnClickListener(v -> finish());

        // Lista de mascotas accesibles
        listaMascotas = new ArrayList<>();

        cargarMascotasPermitidas();
        configurarSelectorMascota();
        configurarSelectorFechaInicio();
        configurarSelectorFechaFin();

        // Guardar medicación
        btnGuardarMedicacion.setOnClickListener(v -> guardarMedicacion());
    }

    // Cargar mascotas propias y compartidas aceptadas
    private void cargarMascotasPermitidas() {

        listaMascotas =
                MascotasUtils.obtenerMascotasDisponibles(
                        this
                );
    }

    // Configurar selector de mascota
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

        txtMascotaMedicacion.setAdapter(adapter);

        txtMascotaMedicacion.setFocusable(false);
        txtMascotaMedicacion.setClickable(true);
        txtMascotaMedicacion.setCursorVisible(false);

        txtMascotaMedicacion.setOnItemClickListener((parent, view, position, id) -> {

            mascotaSeleccionada =
                    listaMascotas.get(position);

            cargarHistorialMedicacion();
        });

        if (listaMascotas.isEmpty()) {

            txtHistorialMedicacion.setText("Non hai mascotas dispoñibles");

            Toast.makeText(
                    this,
                    "Non hai mascotas dispoñibles",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            mascotaSeleccionada =
                    listaMascotas.get(0);

            txtMascotaMedicacion.setText(
                    "   " + mascotaSeleccionada.getNombre(),
                    false
            );

            cargarHistorialMedicacion();
        }
    }

    // Configurar fecha de inicio
    private void configurarSelectorFechaInicio() {

        ponerFechaActual(txtFechaInicio);

        txtFechaInicio.setFocusable(false);
        txtFechaInicio.setClickable(true);
        txtFechaInicio.setCursorVisible(false);

        txtFechaInicio.setOnClickListener(v -> mostrarCalendario(txtFechaInicio));
    }

    // Configurar fecha de fin
    private void configurarSelectorFechaFin() {

        txtFechaFin.setFocusable(false);
        txtFechaFin.setClickable(true);
        txtFechaFin.setCursorVisible(false);

        txtFechaFin.setOnClickListener(v -> mostrarCalendario(txtFechaFin));
    }

    // Poner fecha actual en un campo
    private void ponerFechaActual(TextInputEditText campoFecha) {

        Calendar calendario =
                Calendar.getInstance();

        int anoActual =
                calendario.get(Calendar.YEAR);

        int mesActual =
                calendario.get(Calendar.MONTH);

        int diaActual =
                calendario.get(Calendar.DAY_OF_MONTH);

        String fechaActual =
                String.format(
                        "%02d/%02d/%04d",
                        diaActual,
                        mesActual + 1,
                        anoActual
                );

        campoFecha.setText(fechaActual);
    }

    // Mostrar calendario para un campo concreto
    private void mostrarCalendario(TextInputEditText campoFecha) {

        Calendar calendario =
                Calendar.getInstance();

        int ano =
                calendario.get(Calendar.YEAR);

        int mes =
                calendario.get(Calendar.MONTH);

        int dia =
                calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogoFecha =
                new DatePickerDialog(
                        MedicacionActivity.this,
                        (view, year, month, dayOfMonth) -> {

                            String fecha =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                    );

                            campoFecha.setText(fecha);
                        },
                        ano,
                        mes,
                        dia
                );

        dialogoFecha.show();
    }

    // Guardar medicación
    private void guardarMedicacion() {

        if (mascotaSeleccionada == null) {

            Toast.makeText(
                    this,
                    "Selecciona unha mascota",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String nombre;

        if (txtNombreMedicamento.getText() != null) {

            nombre =
                    txtNombreMedicamento
                            .getText()
                            .toString()
                            .trim();

        } else {

            nombre = "";
        }

        String dosis;

        if (txtDosis.getText() != null) {

            dosis =
                    txtDosis
                            .getText()
                            .toString()
                            .trim();

        } else {

            dosis = "";
        }

        String fechaInicio;

        if (txtFechaInicio.getText() != null) {

            fechaInicio =
                    txtFechaInicio
                            .getText()
                            .toString()
                            .trim();

        } else {

            fechaInicio = "";
        }

        String fechaFin;

        if (txtFechaFin.getText() != null) {

            fechaFin =
                    txtFechaFin
                            .getText()
                            .toString()
                            .trim();

        } else {

            fechaFin = "";
        }

        String notas;

        if (txtNotasMedicacion.getText() != null) {

            notas =
                    txtNotasMedicacion
                            .getText()
                            .toString()
                            .trim();

        } else {

            notas = "";
        }

        if (nombre.isEmpty()) {

            txtNombreMedicamento.setError("Introduce o medicamento");
            return;
        }

        if (fechaInicio.isEmpty()) {

            txtFechaInicio.setError("Introduce a data de inicio");
            return;
        }

        MedicacionDTO medicacion =
                new MedicacionDTO(
                        mascotaSeleccionada.getId(),
                        nombre,
                        dosis,
                        fechaInicio,
                        fechaFin,
                        notas
                );

        medicacion.setUsuarioId(
                SesionUsuario.obtenerUsuarioId(this)
        );

        medicacionDAO.insertar(medicacion);

        crearEventosMedicacion(
                nombre,
                fechaInicio,
                fechaFin
        );

        SupabaseSyncManager.sincronizar(this);

        txtNombreMedicamento.setText("");
        txtDosis.setText("");
        txtNotasMedicacion.setText("");

        cargarHistorialMedicacion();

        Toast.makeText(
                this,
                "Medicación gardada correctamente",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Crear eventos diarios de medicación para que aparezcan en calendario
    private void crearEventosMedicacion(
            String nombreMedicamento,
            String fechaInicio,
            String fechaFin
    ) {

        try {

            SimpleDateFormat formato =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    );

            Date fechaInicial =
                    formato.parse(fechaInicio);

            Date fechaFinal;

            if (fechaFin == null || fechaFin.isEmpty()) {

                fechaFinal = fechaInicial;

            } else {

                fechaFinal =
                        formato.parse(fechaFin);
            }

            Calendar calendario =
                    Calendar.getInstance();

            calendario.setTime(fechaInicial);

            while (!calendario.getTime().after(fechaFinal)) {

                String fechaEvento =
                        formato.format(
                                calendario.getTime()
                        );

                EventoDTO evento =
                        new EventoDTO(
                                "Medicación",
                                nombreMedicamento,
                                fechaEvento,
                                "09:00",
                                "Medicación",
                                mascotaSeleccionada.getNombre()
                        );

                evento.setUsuarioId(
                        SesionUsuario.obtenerUsuarioId(this)
                );

                evento.setMascotaId(
                        mascotaSeleccionada.getId()
                );

                evento.setFechaActualizacion(
                        System.currentTimeMillis()
                );

                evento.setPendienteSync(true);

                eventoDAO.insertar(evento);

                calendario.add(
                        Calendar.DAY_OF_MONTH,
                        1
                );
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Non se puideron crear os eventos da medicación",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Cargar historial de medicación de la mascota seleccionada
    private void cargarHistorialMedicacion() {

        if (mascotaSeleccionada == null) {

            txtHistorialMedicacion.setText("Selecciona unha mascota");
            return;
        }

        ArrayList<MedicacionDTO> medicaciones =
                new ArrayList<>(
                        medicacionDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        if (medicaciones.isEmpty()) {

            txtHistorialMedicacion.setText("Sen medicación rexistrada");
            return;
        }

        StringBuilder texto =
                new StringBuilder();

        for (int i = 0; i < medicaciones.size(); i++) {

            MedicacionDTO medicacion =
                    medicaciones.get(i);

            texto.append(medicacion.getFechaInicio());

            if (medicacion.getFechaFin() != null
                    && !medicacion.getFechaFin().isEmpty()) {

                texto.append(" - ")
                        .append(medicacion.getFechaFin());
            }

            texto.append("  Medicación: ")
                    .append(medicacion.getNombre());

            if (medicacion.getDosis() != null
                    && !medicacion.getDosis().isEmpty()) {

                texto.append("\n")
                        .append("Dosis: ")
                        .append(medicacion.getDosis());
            }

            if (medicacion.getNotas() != null
                    && !medicacion.getNotas().isEmpty()) {

                texto.append("\n")
                        .append(medicacion.getNotas());
            }

            if (i < medicaciones.size() - 1) {

                texto.append("\n\n");
            }
        }

        txtHistorialMedicacion.setText(
                texto.toString()
        );
    }
}