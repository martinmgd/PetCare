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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_evento);

        dao = new EventoDAO();
        mascotaDAO = new MascotaDAO();

        // Referencias aos compoñentes da interface
        toolbarAddEvento = findViewById(R.id.toolbarAddEvento);

        txtTituloEvento = findViewById(R.id.txtTituloEvento);
        txtDescripcionEvento = findViewById(R.id.txtDescripcionEvento);
        txtFechaEvento = findViewById(R.id.txtFechaEvento);
        txtHoraEvento = findViewById(R.id.txtHoraEvento);
        txtTipoEvento = findViewById(R.id.txtTipoEvento);
        txtMascotaEvento = findViewById(R.id.txtMascotaEvento);

        btnGuardarEvento = findViewById(R.id.btnGuardarEvento);

        // Botón de volver da barra superior
        toolbarAddEvento.setNavigationOnClickListener(v -> finish());

        // Configuración dos campos especiais
        configurarSelectorFecha();
        configurarSelectorHora();
        configurarSelectorMascota();

        // Acción do botón gardar
        btnGuardarEvento.setOnClickListener(v -> guardarEvento());
    }

    // Configura o campo de data para que abra un calendario ao pulsar nel
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

    // Mostra un DatePickerDialog para seleccionar a data do evento
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

    // Configura o campo de hora para que abra un reloxo ao pulsar nel
    private void configurarSelectorHora() {

        txtHoraEvento.setFocusable(false);
        txtHoraEvento.setClickable(true);
        txtHoraEvento.setCursorVisible(false);

        txtHoraEvento.setOnClickListener(v -> mostrarRelojHora());
    }

    // Mostra un TimePickerDialog para seleccionar a hora e os minutos
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

    // Configura o despregable coas mascotas dispoñibles
    private void configurarSelectorMascota() {

        ArrayList<MascotaDTO> listaMascotas = mascotaDAO.listar();

        ArrayList<String> nombresMascotas = new ArrayList<>();

        for (MascotaDTO mascota : listaMascotas) {

            nombresMascotas.add(mascota.getNombre());
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

    // Recolle os datos do formulario, valida os campos obrigatorios e devolve o evento creado
    private void guardarEvento() {

        String titulo;

        if (txtTituloEvento.getText() != null) {
            titulo = txtTituloEvento.getText().toString().trim();
        } else {
            titulo = "";
        }

        String descripcion;

        if (txtDescripcionEvento.getText() != null) {
            descripcion = txtDescripcionEvento.getText().toString().trim();
        } else {
            descripcion = "";
        }

        String fecha;

        if (txtFechaEvento.getText() != null) {
            fecha = txtFechaEvento.getText().toString().trim();
        } else {
            fecha = "";
        }

        String hora;

        if (txtHoraEvento.getText() != null) {
            hora = txtHoraEvento.getText().toString().trim();
        } else {
            hora = "";
        }

        String tipo;

        if (txtTipoEvento.getText() != null) {
            tipo = txtTipoEvento.getText().toString().trim();
        } else {
            tipo = "";
        }

        String mascota;

        if (txtMascotaEvento.getText() != null) {
            mascota = txtMascotaEvento.getText().toString().trim();
        } else {
            mascota = "";
        }

        // Validación do título
        if (titulo.isEmpty()) {

            txtTituloEvento.setError("Introduce o título do evento");
            return;
        }

        // Validación da data
        if (fecha.isEmpty()) {

            txtFechaEvento.setError("Introduce a data");
            return;
        }

        // Validación da hora
        if (hora.isEmpty()) {

            txtHoraEvento.setError("Introduce a hora");
            return;
        }

        // Validación da mascota
        if (mascota.isEmpty()) {

            txtMascotaEvento.setError("Selecciona unha mascota");
            return;
        }

        // Creación do obxecto EventoDTO cos datos introducidos
        EventoDTO evento =
                new EventoDTO(
                        titulo,
                        descripcion,
                        fecha,
                        hora,
                        tipo,
                        mascota
                );

        Toast.makeText(
                this,
                "Evento gardado correctamente",
                Toast.LENGTH_SHORT
        ).show();

        // Devolvemos o evento á Activity anterior
        Intent intent = new Intent();

        intent.putExtra("evento", evento);

        setResult(RESULT_OK, intent);

        finish();
    }
}