package com.example.petcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dto.EventoDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddEventoActivity extends AppCompatActivity {

    private Toolbar toolbarAddEvento;

    private TextInputEditText txtTituloEvento;
    private TextInputEditText txtDescripcionEvento;
    private TextInputEditText txtFechaEvento;
    private TextInputEditText txtHoraEvento;
    private TextInputEditText txtTipoEvento;
    private TextInputEditText txtMascotaEvento;

    private MaterialButton btnGuardarEvento;

    private EventoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_evento);

        dao = new EventoDAO();

        // Referencias dos compoñentes da interface
        toolbarAddEvento = findViewById(R.id.toolbarAddEvento);

        txtTituloEvento = findViewById(R.id.txtTituloEvento);
        txtDescripcionEvento = findViewById(R.id.txtDescripcionEvento);
        txtFechaEvento = findViewById(R.id.txtFechaEvento);
        txtHoraEvento = findViewById(R.id.txtHoraEvento);
        txtTipoEvento = findViewById(R.id.txtTipoEvento);
        txtMascotaEvento = findViewById(R.id.txtMascotaEvento);

        btnGuardarEvento = findViewById(R.id.btnGuardarEvento);

        // Acción do botón volver
        toolbarAddEvento.setNavigationOnClickListener(v -> finish());

        // Inicialización dos selectores de data e hora
        configurarSelectorFecha();
        configurarSelectorHora();

        // Acción do botón gardar
        btnGuardarEvento.setOnClickListener(v -> guardarEvento());
    }

    // Configura o selector de data
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

    // Mostra o calendario para seleccionar unha data
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

    // Configura o selector de hora en intervalos de 30 minutos
    private void configurarSelectorHora() {

        txtHoraEvento.setFocusable(false);
        txtHoraEvento.setClickable(true);

        txtHoraEvento.setOnClickListener(v -> {

            String[] horas = {
                    "08:00", "08:30",
                    "09:00", "09:30",
                    "10:00", "10:30",
                    "11:00", "11:30",
                    "12:00", "12:30",
                    "13:00", "13:30",
                    "14:00", "14:30",
                    "15:00", "15:30",
                    "16:00", "16:30",
                    "17:00", "17:30",
                    "18:00", "18:30",
                    "19:00", "19:30",
                    "20:00", "20:30",
                    "21:00", "21:30",
                    "22:00", "22:30"
            };

            new AlertDialog.Builder(AddEventoActivity.this)
                    .setTitle("Selecciona unha hora")
                    .setItems(horas, (dialog, which) ->
                            txtHoraEvento.setText(horas[which]))
                    .show();
        });
    }

    // Garda un novo evento
    private void guardarEvento() {

        String titulo =
                txtTituloEvento.getText() != null
                        ? txtTituloEvento.getText().toString().trim()
                        : "";

        String descripcion =
                txtDescripcionEvento.getText() != null
                        ? txtDescripcionEvento.getText().toString().trim()
                        : "";

        String fecha =
                txtFechaEvento.getText() != null
                        ? txtFechaEvento.getText().toString().trim()
                        : "";

        String hora =
                txtHoraEvento.getText() != null
                        ? txtHoraEvento.getText().toString().trim()
                        : "";

        String tipo =
                txtTipoEvento.getText() != null
                        ? txtTipoEvento.getText().toString().trim()
                        : "";

        String mascota =
                txtMascotaEvento.getText() != null
                        ? txtMascotaEvento.getText().toString().trim()
                        : "";

        if(titulo.isEmpty()){

            txtTituloEvento.setError("Introduce o título do evento");
            return;
        }

        if(fecha.isEmpty()){

            txtFechaEvento.setError("Introduce a data");
            return;
        }

        if(hora.isEmpty()){

            txtHoraEvento.setError("Introduce a hora");
            return;
        }

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

        Intent intent = new Intent();

        intent.putExtra("evento", evento);

        setResult(RESULT_OK, intent);

        finish();
    }
}