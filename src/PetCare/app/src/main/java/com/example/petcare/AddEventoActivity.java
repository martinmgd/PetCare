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
    private TextInputEditText txtTipoEvento;

    private MaterialAutoCompleteTextView txtMascotaEvento;

    private MaterialButton btnGuardarEvento;

    private EventoDAO dao;
    private MascotaDAO mascotaDAO;

    private boolean editar;
    private EventoDTO eventoEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_evento);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        dao = db.eventoDAO();
        mascotaDAO = db.mascotaDAO();

        // Referencias XML
        toolbarAddEvento = findViewById(R.id.toolbarAddEvento);

        txtTituloEvento = findViewById(R.id.txtTituloEvento);
        txtDescripcionEvento = findViewById(R.id.txtDescripcionEvento);
        txtFechaEvento = findViewById(R.id.txtFechaEvento);
        txtHoraEvento = findViewById(R.id.txtHoraEvento);
        txtTipoEvento = findViewById(R.id.txtTipoEvento);
        txtMascotaEvento = findViewById(R.id.txtMascotaEvento);

        btnGuardarEvento = findViewById(R.id.btnGuardarEvento);

        // Recibir datos
        editar = getIntent().getBooleanExtra("editar", false);

        eventoEditar =
                (EventoDTO)
                        getIntent().getSerializableExtra("evento");

        // Configurar título
        if (editar) {

            toolbarAddEvento.setTitle("Editar evento");

        } else {

            toolbarAddEvento.setTitle("Novo evento");
        }

        // Botón volver
        toolbarAddEvento.setNavigationOnClickListener(v -> finish());

        // Configurar selectores
        configurarSelectorFecha();
        configurarSelectorHora();
        configurarSelectorMascota();

        // Cargar datos si estamos editando
        if (editar && eventoEditar != null) {

            cargarDatosEvento();
        }

        // Guardar evento
        btnGuardarEvento.setOnClickListener(v -> guardarEvento());
    }

    // Configurar selector de fecha
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

    // Mostrar calendario
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

    // Configurar selector de hora
    private void configurarSelectorHora() {

        txtHoraEvento.setFocusable(false);
        txtHoraEvento.setClickable(true);
        txtHoraEvento.setCursorVisible(false);

        txtHoraEvento.setOnClickListener(v -> mostrarRelojHora());
    }

    // Mostrar selector de hora
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

    // Configurar selector de mascotas
    private void configurarSelectorMascota() {

        ArrayList<MascotaDTO> listaMascotas =
                new ArrayList<>(mascotaDAO.listar());

        ArrayList<String> nombresMascotas =
                new ArrayList<>();

        for (MascotaDTO mascota : listaMascotas) {

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

        if (nombresMascotas.isEmpty()) {

            Toast.makeText(
                    this,
                    "Non hai mascotas rexistradas",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // Cargar datos do evento en edición
    private void cargarDatosEvento() {

        txtTituloEvento.setText(eventoEditar.getTitulo());
        txtDescripcionEvento.setText(eventoEditar.getDescripcion());
        txtFechaEvento.setText(eventoEditar.getFecha());
        txtHoraEvento.setText(eventoEditar.getHora());
        txtTipoEvento.setText(eventoEditar.getTipo());

        txtMascotaEvento.setText(
                eventoEditar.getMascota(),
                false
        );

        btnGuardarEvento.setText("Gardar cambios");
    }

    // Guardar evento
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

        // Validar título
        if (titulo.isEmpty()) {

            txtTituloEvento.setError(
                    "Introduce o título do evento"
            );

            return;
        }

        // Validar fecha
        if (fecha.isEmpty()) {

            txtFechaEvento.setError(
                    "Introduce a data"
            );

            return;
        }

        // Validar hora
        if (hora.isEmpty()) {

            txtHoraEvento.setError(
                    "Introduce a hora"
            );

            return;
        }

        // Validar mascota
        if (mascota.isEmpty()) {

            txtMascotaEvento.setError(
                    "Selecciona unha mascota"
            );

            return;
        }

        // Crear evento
        EventoDTO evento =
                new EventoDTO(
                        titulo,
                        descripcion,
                        fecha,
                        hora,
                        tipo,
                        mascota
                );

        // Manter o id ao editar
        if (editar && eventoEditar != null) {

            evento.setId(
                    eventoEditar.getId()
            );
        }

        Toast.makeText(
                this,
                "Evento gardado correctamente",
                Toast.LENGTH_SHORT
        ).show();

        // Devolver evento á Activity anterior
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