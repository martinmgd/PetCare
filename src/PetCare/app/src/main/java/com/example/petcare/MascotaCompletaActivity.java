package com.example.petcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dto.MascotaDTO;

import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

public class MascotaCompletaActivity extends AppCompatActivity {

    ImageView ivMascotaCompleta;

    TextView txtNombreCompleto;
    TextView txtEspecie;
    TextView txtRaza;
    TextView txtEdad;
    TextView txtPeso;
    TextView txtNacimiento;
    TextView txtSexo;
    TextView txtFavorito;

    Button btnEditarMascota;

    MascotaDTO mascota;
    MascotaDAO dao;

    Uri imagenSeleccionada;

    int posicion;

    Toolbar toolbarMascota;

    Button btnRegistrarPeso;

    ActivityResultLauncher<String> selectorImagen =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {

                if(uri != null){
                    imagenSeleccionada = uri;
                    ivMascotaCompleta.setImageURI(uri);
                }
            });

    ActivityResultLauncher<Intent> launcherEditar =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == RESULT_OK){

                    Intent data = result.getData();

                    MascotaDTO mascotaEditada = (MascotaDTO) data.getSerializableExtra("mascota");

                    if(posicion != -1 && mascotaEditada != null){

                        // Actualizar mascota
                        dao.actualizar(posicion, mascotaEditada);

                        mascota = mascotaEditada;

                        // Actualizar pantalla
                        mostrarDatos();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mascota_completa);

        dao = new MascotaDAO();

        // Referencias
        toolbarMascota = findViewById(R.id.toolbarMascota);

        ivMascotaCompleta = findViewById(R.id.ivMascotaCompleta);

        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtEspecie = findViewById(R.id.txtEspecie);
        txtRaza = findViewById(R.id.txtRaza);
        txtEdad = findViewById(R.id.txtEdad);
        txtPeso = findViewById(R.id.txtPeso);
        txtNacimiento = findViewById(R.id.txtNacimiento);
        txtSexo = findViewById(R.id.txtSexo);
        txtFavorito = findViewById(R.id.txtFavorito);

        btnEditarMascota = findViewById(R.id.btnEditarMascota);

        btnRegistrarPeso = findViewById(R.id.btnRegistrarPeso);

        // Recibir mascota
        mascota = (MascotaDTO) getIntent().getSerializableExtra("mascota");
        posicion = getIntent().getIntExtra("posicion", -1);

        // Toolbar
        setSupportActionBar(toolbarMascota);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if(mascota != null){
                getSupportActionBar().setTitle(mascota.getNombre());
            }else{
                getSupportActionBar().setTitle("Mascota");
            }
        }

        toolbarMascota.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
            }
        });

        // Mostrar datos
        mostrarDatos();

        // Cambiar imagen

        ivMascotaCompleta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                selectorImagen.launch("image/*");
            }
        });

        // Editar mascota
        btnEditarMascota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MascotaCompletaActivity.this, AddMascotaActivity.class);

                intent.putExtra("mascota", mascota);
                intent.putExtra("posicion", posicion);
                intent.putExtra("editar", true);

                launcherEditar.launch(intent);
            }
        });

        // Registrar peso

        btnRegistrarPeso.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText txtPesoNuevo = new EditText(MascotaCompletaActivity.this);

                txtPesoNuevo.setHint("Peso actual");
                txtPesoNuevo.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

                AlertDialog.Builder dialogo = new AlertDialog.Builder(MascotaCompletaActivity.this);

                dialogo.setTitle("Registrar peso");
                dialogo.setMessage("Introduce el peso actual de la mascota:");
                dialogo.setView(txtPesoNuevo);

                dialogo.setPositiveButton("Guardar", (dialogInterface, i) -> {

                    String pesoTexto = txtPesoNuevo.getText().toString();

                    if(!pesoTexto.isEmpty()){

                        double peso = Double.parseDouble(pesoTexto);

                        mascota.setPesoActual(peso);

                        dao.actualizar(posicion, mascota);

                        mostrarDatos();

                        Toast.makeText(MascotaCompletaActivity.this, "Peso registrado", Toast.LENGTH_SHORT).show();
                    }
                });

                dialogo.setNegativeButton("Cancelar", null);

                dialogo.show();
            }
        });
    }

    // Mostrar datos
    private void mostrarDatos(){

        if(mascota != null){

            ivMascotaCompleta.setImageResource(R.drawable.ic_launcher_foreground);

            txtNombreCompleto.setText(mascota.getNombre());
            txtEspecie.setText("Especie: " + mascota.getEspecie());
            txtRaza.setText("Raza: " + mascota.getRaza());
            txtEdad.setText("Edad: " + mascota.getEdad() + " años");

            if(mascota.getPesoActual() == 0){
                txtPeso.setText("Peso actual: Sin registrar");
            }else{
                txtPeso.setText("Peso actual: " + mascota.getPesoActual() + " kg");
            }

            txtNacimiento.setText("Fecha nacimiento: " + mascota.getFechaNacimiento());
            txtSexo.setText("Sexo: " + mascota.getSexo());
            txtFavorito.setText("Favorito: " + (mascota.isFavorito() ? "Sí" : "No"));

            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(mascota.getNombre());
            }
        }
    }
}