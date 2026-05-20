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

import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dto.MascotaDTO;

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

        // Recibir mascota
        mascota = (MascotaDTO) getIntent().getSerializableExtra("mascota");
        posicion = getIntent().getIntExtra("posicion", -1);

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
        }
    }
}