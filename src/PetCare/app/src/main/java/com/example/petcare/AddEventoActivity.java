package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dto.EventoDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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

        // Referencias XML
        toolbarAddEvento = findViewById(R.id.toolbarAddEvento);

        txtTituloEvento = findViewById(R.id.txtTituloEvento);
        txtDescripcionEvento = findViewById(R.id.txtDescripcionEvento);
        txtFechaEvento = findViewById(R.id.txtFechaEvento);
        txtHoraEvento = findViewById(R.id.txtHoraEvento);
        txtTipoEvento = findViewById(R.id.txtTipoEvento);
        txtMascotaEvento = findViewById(R.id.txtMascotaEvento);

        btnGuardarEvento = findViewById(R.id.btnGuardarEvento);

        // Toolbar
        toolbarAddEvento.setNavigationOnClickListener(v -> finish());

        // Botón guardar
        btnGuardarEvento.setOnClickListener(v -> guardarEvento());

    }

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

        // Validaciones
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

        // Crear DTO
        EventoDTO evento = new EventoDTO();

        evento.setTitulo(titulo);
        evento.setDescripcion(descripcion);
        evento.setFecha(fecha);
        evento.setHora(hora);
        evento.setTipo(tipo);
        evento.setMascota(mascota);

        // Guardar
        dao.insertar(evento);

        Toast.makeText(
                this,
                "Evento gardado correctamente",
                Toast.LENGTH_SHORT
        ).show();

        // Resultado
        Intent intent = new Intent();

        intent.putExtra("evento", evento);

        setResult(RESULT_OK, intent);

        finish();

    }

}