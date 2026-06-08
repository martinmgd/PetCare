package com.example.petcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.button.MaterialButton;

public class MascotaCompletaActivity extends AppCompatActivity {

    private ImageView ivMascotaCompleta;

    private TextView txtNombreCompleto;
    private TextView txtEspecie;
    private TextView txtRaza;
    private TextView txtEdad;
    private TextView txtPeso;
    private TextView txtNacimiento;
    private TextView txtSexo;

    private MaterialButton btnEditarMascota;
    private MaterialButton btnGestionarUsuarios;

    private Toolbar toolbarMascota;

    private MascotaDTO mascota;
    private MascotaDAO dao;

    private Uri imagenSeleccionada;

    ActivityResultLauncher<String> selectorImagen =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            imagenSeleccionada = uri;
                            ivMascotaCompleta.setImageURI(uri);
                        }
                    }
            );

    ActivityResultLauncher<Intent> launcherEditar =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            MascotaDTO mascotaEditada =
                                    (MascotaDTO) result.getData()
                                            .getSerializableExtra("mascota");

                            if (mascotaEditada != null) {

                                // Actualizar mascota en Room usando o id
                                dao.actualizar(mascotaEditada);

                                mascota = mascotaEditada;

                                // Actualizar pantalla
                                mostrarDatos();
                                configurarBotonGestionUsuarios();

                                Toast.makeText(
                                        MascotaCompletaActivity.this,
                                        "Mascota actualizada",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mascota_completa);

        // Base de datos
        dao =
                PetCareDatabase
                        .getInstance(this)
                        .mascotaDAO();

        // Referencias XML
        toolbarMascota = findViewById(R.id.toolbarMascota);

        ivMascotaCompleta = findViewById(R.id.ivMascotaCompleta);

        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtEspecie = findViewById(R.id.txtEspecie);
        txtRaza = findViewById(R.id.txtRaza);
        txtEdad = findViewById(R.id.txtEdad);
        txtPeso = findViewById(R.id.txtPeso);
        txtNacimiento = findViewById(R.id.txtNacimiento);
        txtSexo = findViewById(R.id.txtSexo);

        btnEditarMascota = findViewById(R.id.btnEditarMascota);
        btnGestionarUsuarios = findViewById(R.id.btnGestionarUsuarios);

        // Recibir mascota
        mascota =
                (MascotaDTO)
                        getIntent().getSerializableExtra("mascota");

        // Toolbar
        setSupportActionBar(toolbarMascota);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (mascota != null) {

                getSupportActionBar().setTitle(mascota.getNombre());

            } else {

                getSupportActionBar().setTitle("Mascota");
            }
        }

        // Botón volver
        toolbarMascota.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
            }
        });

        // Mostrar datos
        mostrarDatos();

        // Configurar botón de gestión de usuarios
        configurarBotonGestionUsuarios();

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

                if (mascota != null) {

                    Intent intent =
                            new Intent(
                                    MascotaCompletaActivity.this,
                                    AddMascotaActivity.class
                            );

                    // Enviamos a mascota completa co seu id
                    intent.putExtra("mascota", mascota);
                    intent.putExtra("editar", true);

                    launcherEditar.launch(intent);
                }
            }
        });

        // Gestionar usuarios da mascota
        btnGestionarUsuarios.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mascota != null) {

                    Intent intent =
                            new Intent(
                                    MascotaCompletaActivity.this,
                                    GestionUsuariosMascotaActivity.class
                            );

                    intent.putExtra("mascota", mascota);

                    startActivity(intent);
                }
            }
        });
    }

    // Mostrar ou ocultar o botón segundo o propietario
    private void configurarBotonGestionUsuarios() {

        if (mascota == null) {

            btnGestionarUsuarios.setVisibility(View.GONE);
            return;
        }

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        if (usuarioActualId.equals(mascota.getPropietarioId())) {

            btnGestionarUsuarios.setVisibility(View.VISIBLE);

        } else {

            btnGestionarUsuarios.setVisibility(View.GONE);
        }
    }

    // Mostrar datos
    private void mostrarDatos() {

        if (mascota != null) {

            ivMascotaCompleta.setImageResource(R.drawable.ic_launcher_foreground);

            txtNombreCompleto.setText(mascota.getNombre());
            txtEspecie.setText("Especie: " + mascota.getEspecie());
            txtRaza.setText("Raza: " + mascota.getRaza());
            txtEdad.setText("Idade: " + mascota.getEdad() + " anos");

            if (mascota.getPesoActual() == 0) {

                txtPeso.setText("Peso actual: Sen rexistrar");

            } else {

                txtPeso.setText("Peso actual: " + mascota.getPesoActual() + " kg");
            }

            txtNacimiento.setText("Fecha nacemento: " + mascota.getFechaNacimiento());
            txtSexo.setText("Sexo: " + mascota.getSexo());

            if (getSupportActionBar() != null) {

                getSupportActionBar().setTitle(mascota.getNombre());
            }
        }
    }
}