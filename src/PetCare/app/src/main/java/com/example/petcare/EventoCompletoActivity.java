package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.EventoDTO;
import com.google.android.material.button.MaterialButton;

public class EventoCompletoActivity extends AppCompatActivity {

    private Toolbar toolbarEventoCompleto;

    private TextView txtTituloEventoCompleto;
    private TextView txtDescripcionEventoCompleto;
    private TextView txtFechaEventoCompleto;
    private TextView txtHoraEventoCompleto;
    private TextView txtTipoEventoCompleto;
    private TextView txtMascotaEventoCompleto;

    private MaterialButton btnEditarEvento;

    private EventoDTO evento;
    private EventoDAO dao;

    ActivityResultLauncher<Intent> launcherEditar =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            EventoDTO eventoEditado =
                                    (EventoDTO) result.getData()
                                            .getSerializableExtra("evento");

                            if (eventoEditado != null) {

                                // Actualizar evento en Room usando o id do evento
                                dao.actualizar(eventoEditado);

                                evento = eventoEditado;

                                mostrarDatos();

                                Toast.makeText(
                                        EventoCompletoActivity.this,
                                        "Evento actualizado correctamente",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                Toast.makeText(
                                        EventoCompletoActivity.this,
                                        "Non se puido actualizar o evento",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evento_completo);

        // Base de datos
        dao =
                PetCareDatabase
                        .getInstance(this)
                        .eventoDAO();

        // Recibir evento
        evento = (EventoDTO) getIntent().getSerializableExtra("evento");

        // Referencias XML
        toolbarEventoCompleto = findViewById(R.id.toolbarEventoCompleto);

        txtTituloEventoCompleto = findViewById(R.id.txtTituloEventoCompleto);
        txtDescripcionEventoCompleto = findViewById(R.id.txtDescripcionEventoCompleto);
        txtFechaEventoCompleto = findViewById(R.id.txtFechaEventoCompleto);
        txtHoraEventoCompleto = findViewById(R.id.txtHoraEventoCompleto);
        txtTipoEventoCompleto = findViewById(R.id.txtTipoEventoCompleto);
        txtMascotaEventoCompleto = findViewById(R.id.txtMascotaEventoCompleto);

        btnEditarEvento = findViewById(R.id.btnEditarEvento);

        // Botón volver
        toolbarEventoCompleto.setNavigationOnClickListener(v -> finish());

        // Mostrar datos
        mostrarDatos();

        // Editar evento
        btnEditarEvento.setOnClickListener(v -> {

            if (evento != null) {

                Intent intent =
                        new Intent(
                                EventoCompletoActivity.this,
                                AddEventoActivity.class
                        );

                intent.putExtra("editar", true);
                intent.putExtra("evento", evento);

                launcherEditar.launch(intent);

            } else {

                Toast.makeText(
                        EventoCompletoActivity.this,
                        "Non se pode editar este evento",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // Mostrar datos
    private void mostrarDatos() {

        if (evento != null) {

            txtTituloEventoCompleto.setText(evento.getTitulo());
            txtDescripcionEventoCompleto.setText("Descrición: " + evento.getDescripcion());
            txtFechaEventoCompleto.setText("Data: " + evento.getFecha());
            txtHoraEventoCompleto.setText("Hora: " + evento.getHora());
            txtTipoEventoCompleto.setText("Tipo: " + evento.getTipo());
            txtMascotaEventoCompleto.setText("Mascota asociada: " + evento.getMascota());
        }
    }
}