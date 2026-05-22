package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dto.MascotaDTO;

public class AddMascotaActivity extends AppCompatActivity {

    EditText txtNombre;
    EditText txtEspecie;
    EditText txtRaza;
    EditText txtEdad;
    EditText txtFechaNacimiento;
    EditText txtSexo;
    Button btnGuardarMascota;

    Toolbar toolbarAddMascota;

    boolean editar;
    int posicion;
    MascotaDTO mascotaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_mascota);

        // Referencias
        toolbarAddMascota = findViewById(R.id.toolbarAddMascota);

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

        // Toolbar
        setSupportActionBar(toolbarAddMascota);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if(editar){
                getSupportActionBar().setTitle("Editar mascota");
            }else{
                getSupportActionBar().setTitle("Nueva mascota");
            }
        }

        toolbarAddMascota.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
            }
        });

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
                String edadTexto = txtEdad.getText().toString();
                String fechaNacimiento = txtFechaNacimiento.getText().toString();
                String sexo = txtSexo.getText().toString();

                // Validar datos
                if(nombre.isEmpty() || especie.isEmpty() || raza.isEmpty()
                        || edadTexto.isEmpty() || fechaNacimiento.isEmpty()
                        || sexo.isEmpty()){

                    Toast.makeText(AddMascotaActivity.this, "Cubre todos los campos", Toast.LENGTH_SHORT).show();

                    return;
                }

                int edad = Integer.parseInt(edadTexto);

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