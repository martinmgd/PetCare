package com.example.petcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Calendar;

public class AddMascotaActivity extends AppCompatActivity {

    EditText txtNombre;
    EditText txtEspecie;
    EditText txtRaza;
    EditText txtEdad;
    EditText txtFechaNacimiento;

    MaterialAutoCompleteTextView txtSexo;

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

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (editar) {
                getSupportActionBar().setTitle("Editar mascota");
            } else {
                getSupportActionBar().setTitle("Nova mascota");
            }
        }

        toolbarAddMascota.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
            }
        });

        // Configuración do calendario e do despregable de sexo
        configurarSelectorFechaNacimiento();
        configurarSelectorSexo();

        // Cargar datos se estamos editando
        if (editar && mascotaEditar != null) {

            txtNombre.setText(mascotaEditar.getNombre());
            txtEspecie.setText(mascotaEditar.getEspecie());
            txtRaza.setText(mascotaEditar.getRaza());
            txtEdad.setText(String.valueOf(mascotaEditar.getEdad()));
            txtFechaNacimiento.setText(mascotaEditar.getFechaNacimiento());
            txtSexo.setText(mascotaEditar.getSexo(), false);
        }

        // Guardar mascota
        btnGuardarMascota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                guardarMascota();
            }
        });
    }

    // Configura o campo de data de nacemento para abrir un calendario
    private void configurarSelectorFechaNacimiento() {

        txtFechaNacimiento.setFocusable(false);
        txtFechaNacimiento.setClickable(true);
        txtFechaNacimiento.setCursorVisible(false);

        txtFechaNacimiento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mostrarCalendarioNacimiento();
            }
        });
    }

    // Mostra un calendario para seleccionar a data de nacemento
    private void mostrarCalendarioNacimiento() {

        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogoFecha =
                new DatePickerDialog(
                        AddMascotaActivity.this,
                        (view, year, month, dayOfMonth) -> {

                            String fecha =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                    );

                            txtFechaNacimiento.setText(fecha);
                        },
                        ano,
                        mes,
                        dia
                );

        dialogoFecha.show();
    }

    // Configura o despregable de sexo da mascota
    private void configurarSelectorSexo() {

        String[] sexos = {
                "Macho",
                "Femia"
        };

        ArrayAdapter<String> adapterSexo =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        sexos
                );

        txtSexo.setAdapter(adapterSexo);

        txtSexo.setFocusable(false);
        txtSexo.setClickable(true);
        txtSexo.setCursorVisible(false);
    }

    // Recolle os datos, valida o formulario e devolve a mascota creada ou editada
    private void guardarMascota() {

        String nombre;

        if (txtNombre.getText() != null) {
            nombre = txtNombre.getText().toString().trim();
        } else {
            nombre = "";
        }

        String especie;

        if (txtEspecie.getText() != null) {
            especie = txtEspecie.getText().toString().trim();
        } else {
            especie = "";
        }

        String raza;

        if (txtRaza.getText() != null) {
            raza = txtRaza.getText().toString().trim();
        } else {
            raza = "";
        }

        String edadTexto;

        if (txtEdad.getText() != null) {
            edadTexto = txtEdad.getText().toString().trim();
        } else {
            edadTexto = "";
        }

        String fechaNacimiento;

        if (txtFechaNacimiento.getText() != null) {
            fechaNacimiento = txtFechaNacimiento.getText().toString().trim();
        } else {
            fechaNacimiento = "";
        }

        String sexo;

        if (txtSexo.getText() != null) {
            sexo = txtSexo.getText().toString().trim();
        } else {
            sexo = "";
        }

        // Validar datos
        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty()
                || edadTexto.isEmpty() || fechaNacimiento.isEmpty()
                || sexo.isEmpty()) {

            Toast.makeText(
                    AddMascotaActivity.this,
                    "Cubre todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int edad;

        try {
            edad = Integer.parseInt(edadTexto);
        } catch (NumberFormatException e) {

            Toast.makeText(
                    AddMascotaActivity.this,
                    "Introduce unha idade válida",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Crear mascota
        MascotaDTO mascota = new MascotaDTO(nombre, especie, raza, edad);

        mascota.setFechaNacimiento(fechaNacimiento);
        mascota.setSexo(sexo);

        // Enviar datos á Activity anterior
        Intent intent = new Intent();

        intent.putExtra("mascota", mascota);
        intent.putExtra("posicion", posicion);
        intent.putExtra("editar", editar);

        setResult(RESULT_OK, intent);

        finish();
    }
}