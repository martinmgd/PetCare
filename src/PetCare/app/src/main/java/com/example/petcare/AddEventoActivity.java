package com.example.petcare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEventoActivity extends AppCompatActivity {

    private Toolbar toolbarAddEvento;

    private TextInputEditText txtTituloEvento;
    private TextInputEditText txtDescripcionEvento;
    private TextInputEditText txtFechaEvento;
    private TextInputEditText txtHoraEvento;

    private MaterialAutoCompleteTextView txtTipoEvento;
    private MaterialAutoCompleteTextView txtMascotaEvento;

    private MaterialButton btnGuardarEvento;

    private EventoDAO dao;
    private MascotaDAO mascotaDAO;

    private boolean editar;
    private EventoDTO eventoEditar;

    private ArrayList<MascotaDTO> listaMascotasDisponibles;
    private MascotaDTO mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_evento);

        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        dao = db.eventoDAO();
        mascotaDAO = db.mascotaDAO();

        listaMascotasDisponibles = new ArrayList<>();

        toolbarAddEvento = findViewById(R.id.toolbarAddEvento);

        txtTituloEvento = findViewById(R.id.txtTituloEvento);
        txtDescripcionEvento = findViewById(R.id.txtDescripcionEvento);
        txtFechaEvento = findViewById(R.id.txtFechaEvento);
        txtHoraEvento = findViewById(R.id.txtHoraEvento);
        txtTipoEvento = findViewById(R.id.txtTipoEvento);
        txtMascotaEvento = findViewById(R.id.txtMascotaEvento);

        btnGuardarEvento = findViewById(R.id.btnGuardarEvento);

        editar = getIntent().getBooleanExtra("editar", false);

        eventoEditar =
                (EventoDTO)
                        getIntent().getSerializableExtra("evento");

        if (editar) {

            toolbarAddEvento.setTitle("Editar evento");

        } else {

            toolbarAddEvento.setTitle("Novo evento");
        }

        toolbarAddEvento.setNavigationOnClickListener(v -> finish());

        configurarSelectorFecha();
        configurarSelectorHora();
        configurarSelectorTipoEvento();
        configurarSelectorMascota();

        if (editar && eventoEditar != null) {

            cargarDatosEvento();
        }

        btnGuardarEvento.setOnClickListener(v -> guardarEvento());
    }

    private void configurarSelectorFecha() {

        Calendar calendario = Calendar.getInstance();

        int anoActual = calendario.get(Calendar.YEAR);
        int mesActual = calendario.get(Calendar.MONTH);
        int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

        String fechaActual =
                String.format(
                        "%02d/%02d/%04d",
                        diaActual,
                        mesActual + 1,
                        anoActual
                );

        txtFechaEvento.setText(fechaActual);

        txtFechaEvento.setFocusable(false);
        txtFechaEvento.setClickable(true);
        txtFechaEvento.setCursorVisible(false);

        txtFechaEvento.setOnClickListener(v -> mostrarCalendario());
    }

    private void mostrarCalendario() {

        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogoFecha =
                new DatePickerDialog(
                        AddEventoActivity.this,
                        (view, year, month, dayOfMonth) -> {

                            String fecha =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                    );

                            txtFechaEvento.setText(fecha);
                        },
                        ano,
                        mes,
                        dia
                );

        dialogoFecha.show();
    }

    private void configurarSelectorHora() {

        txtHoraEvento.setFocusable(false);
        txtHoraEvento.setClickable(true);
        txtHoraEvento.setCursorVisible(false);

        txtHoraEvento.setOnClickListener(v -> mostrarRelojHora());
    }

    private void mostrarRelojHora() {

        Calendar calendario = Calendar.getInstance();

        int horaActual = calendario.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendario.get(Calendar.MINUTE);

        TimePickerDialog dialogoHora =
                new TimePickerDialog(
                        AddEventoActivity.this,
                        (view, hourOfDay, minute) -> {

                            String horaSeleccionada =
                                    String.format(
                                            "%02d:%02d",
                                            hourOfDay,
                                            minute
                                    );

                            txtHoraEvento.setText(horaSeleccionada);
                        },
                        horaActual,
                        minutoActual,
                        true
                );

        dialogoHora.setTitle("Selecciona unha hora");

        dialogoHora.show();
    }

    private void configurarSelectorTipoEvento() {

        String[] tiposEvento = {
                "Cita veterinaria",
                "Vacuna",
                "Medicación",
                "Desparasitación",
                "Baño",
                "Peluquería",
                "Control de peso",
                "Cirurxía",
                "Alerxia",
                "Revisión",
                "Outro"
        };

        ArrayAdapter<String> adapterTipos =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        tiposEvento
                );

        txtTipoEvento.setAdapter(adapterTipos);

        txtTipoEvento.setFocusable(false);
        txtTipoEvento.setClickable(true);
        txtTipoEvento.setCursorVisible(false);
    }

    private void configurarSelectorMascota() {

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        listaMascotasDisponibles.clear();

        listaMascotasDisponibles.addAll(
                mascotaDAO.listarPorPropietario(usuarioActualId)
        );

        ArrayList<String> nombresMascotas =
                new ArrayList<>();

        for (MascotaDTO mascota : listaMascotasDisponibles) {

            nombresMascotas.add(
                    mascota.getNombre()
            );
        }

        ArrayAdapter<String> adapterMascotas =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombresMascotas
                );

        txtMascotaEvento.setAdapter(adapterMascotas);

        txtMascotaEvento.setFocusable(false);
        txtMascotaEvento.setClickable(true);
        txtMascotaEvento.setCursorVisible(false);

        txtMascotaEvento.setOnItemClickListener((parent, view, position, id) -> {

            mascotaSeleccionada =
                    listaMascotasDisponibles.get(position);
        });

        if (nombresMascotas.isEmpty()) {

            Toast.makeText(
                    this,
                    "Non tes mascotas dispoñibles",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void cargarDatosEvento() {

        txtTituloEvento.setText(eventoEditar.getTitulo());
        txtDescripcionEvento.setText(eventoEditar.getDescripcion());
        txtFechaEvento.setText(eventoEditar.getFecha());
        txtHoraEvento.setText(eventoEditar.getHora());
        txtTipoEvento.setText(eventoEditar.getTipo(), false);

        txtMascotaEvento.setText(
                eventoEditar.getMascota(),
                false
        );

        for (MascotaDTO mascota : listaMascotasDisponibles) {

            if (mascota.getId() == eventoEditar.getMascotaId()) {

                mascotaSeleccionada = mascota;
                break;
            }
        }

        btnGuardarEvento.setText("Gardar cambios");
    }

    private void guardarEvento() {

        String titulo;

        if (txtTituloEvento.getText() != null) {

            titulo =
                    txtTituloEvento
                            .getText()
                            .toString()
                            .trim();

        } else {

            titulo = "";
        }

        String descripcion;

        if (txtDescripcionEvento.getText() != null) {

            descripcion =
                    txtDescripcionEvento
                            .getText()
                            .toString()
                            .trim();

        } else {

            descripcion = "";
        }

        String fecha;

        if (txtFechaEvento.getText() != null) {

            fecha =
                    txtFechaEvento
                            .getText()
                            .toString()
                            .trim();

        } else {

            fecha = "";
        }

        String hora;

        if (txtHoraEvento.getText() != null) {

            hora =
                    txtHoraEvento
                            .getText()
                            .toString()
                            .trim();

        } else {

            hora = "";
        }

        String tipo;

        if (txtTipoEvento.getText() != null) {

            tipo =
                    txtTipoEvento
                            .getText()
                            .toString()
                            .trim();

        } else {

            tipo = "";
        }

        String mascota;

        if (txtMascotaEvento.getText() != null) {

            mascota =
                    txtMascotaEvento
                            .getText()
                            .toString()
                            .trim();

        } else {

            mascota = "";
        }

        if (titulo.isEmpty()) {

            txtTituloEvento.setError("Introduce o título do evento");
            return;
        }

        if (fecha.isEmpty()) {

            txtFechaEvento.setError("Introduce a data");
            return;
        }

        if (hora.isEmpty()) {

            txtHoraEvento.setError("Introduce a hora");
            return;
        }

        if (tipo.isEmpty()) {

            txtTipoEvento.setError("Selecciona o tipo de evento");
            return;
        }

        if (mascota.isEmpty()) {

            txtMascotaEvento.setError("Selecciona unha mascota");
            return;
        }

        if (mascotaSeleccionada == null) {

            for (MascotaDTO mascotaActual : listaMascotasDisponibles) {

                if (mascotaActual.getNombre().equals(mascota)) {

                    mascotaSeleccionada = mascotaActual;
                    break;
                }
            }
        }

        if (mascotaSeleccionada == null) {

            txtMascotaEvento.setError("Selecciona unha mascota válida");
            return;
        }

        EventoDTO evento =
                new EventoDTO(
                        titulo,
                        descripcion,
                        fecha,
                        hora,
                        tipo,
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

        if (editar && eventoEditar != null) {

            evento.setId(
                    eventoEditar.getId()
            );

            evento.setSupabaseId(
                    eventoEditar.getSupabaseId()
            );

            evento.setFechaCreacion(
                    eventoEditar.getFechaCreacion()
            );

            evento.setFechaActualizacion(
                    System.currentTimeMillis()
            );

            evento.setPendienteSync(true);
        }

        Toast.makeText(
                this,
                "Evento gardado correctamente",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent();

        intent.putExtra("evento", evento);
        intent.putExtra("editar", editar);

        setResult(
                RESULT_OK,
                intent
        );

        finish();
    }
}