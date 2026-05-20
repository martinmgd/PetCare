package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.dto.MascotaDTO;

public class AddMascotaActivity extends AppCompatActivity {

    EditText txtNombre;
    EditText txtEspecie;
    EditText txtRaza;
    EditText txtEdad;
    EditText txtFechaNacimiento;
    EditText txtSexo;
    Button btnGuardarMascota;

    boolean editar;
    int posicion;
    MascotaDTO mascotaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_mascota);

        // Referencias
        txtNombre = findViewById(R.id.txtNombre);
        txtEspecie = findViewById(R.id.txtEspecie);
        txtRaza = findViewById(R.id.txtRaza);
        txtEdad = findViewById(R.id.txtEdad);
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
        txtSexo = findViewById(R.id.txtSexo);

        btnGuardarMascota = findViewById(R.id.btnGuardarMascota);

        // Recibir datos
        editar = getIntent().getBooleanExtra("editar", false);
        posicion = getIntent().getIntExtra("posicion", -1);
        mascotaEditar = (MascotaDTO) getIntent().getSerializableExtra("mascota");

        // Cargar datos
        if(editar && mascotaEditar != null){

            txtNombre.setText(mascotaEditar.getNombre());
            txtEspecie.setText(mascotaEditar.getEspecie());
            txtRaza.setText(mascotaEditar.getRaza());
            txtEdad.setText(String.valueOf(mascotaEditar.getEdad()));
            txtFechaNacimiento.setText(mascotaEditar.getFechaNacimiento());
            txtSexo.setText(mascotaEditar.getSexo());
        }

        // Guardar mascota
        btnGuardarMascota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String nombre = txtNombre.getText().toString();
                String especie = txtEspecie.getText().toString();
                String raza = txtRaza.getText().toString();

                int edad = Integer.parseInt(txtEdad.getText().toString());
                String fechaNacimiento = txtFechaNacimiento.getText().toString();
                String sexo = txtSexo.getText().toString();

                // Crear mascota
                MascotaDTO mascota = new MascotaDTO(nombre, especie, raza, edad);

                mascota.setFechaNacimiento(fechaNacimiento);
                mascota.setSexo(sexo);

                // Enviar datos
                Intent intent = new Intent();

                intent.putExtra("mascota", mascota);
                intent.putExtra("posicion", posicion);
                intent.putExtra("editar", editar);

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}