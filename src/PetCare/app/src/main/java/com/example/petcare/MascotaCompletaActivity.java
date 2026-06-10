package com.example.petcare;

import android.content.Intent;
import android.content.res.ColorStateList;
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

import com.example.petcare.dao.ComidaDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.ComidaDTO;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MascotaCompletaActivity extends AppCompatActivity {

    private ImageView ivMascotaCompleta;

    private TextView txtNombreCompleto;
    private TextView txtEspecie;
    private TextView txtRaza;
    private TextView txtEdad;
    private TextView txtPeso;
    private TextView txtNacimiento;
    private TextView txtSexo;

    private MaterialButton btnAlmorzo;
    private MaterialButton btnComida;
    private MaterialButton btnCea;

    private MaterialButton btnEditarMascota;
    private MaterialButton btnGestionarUsuarios;

    private Toolbar toolbarMascota;

    private MascotaDTO mascota;
    private MascotaDAO dao;
    private ComidaDAO comidaDAO;

    private ComidaDTO comidaHoy;

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

                                dao.actualizar(mascotaEditada);

                                mascota = mascotaEditada;

                                mostrarDatos();
                                configurarBotonGestionUsuarios();
                                cargarComidaHoy();

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

        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        dao = db.mascotaDAO();
        comidaDAO = db.comidaDAO();

        toolbarMascota = findViewById(R.id.toolbarMascota);

        ivMascotaCompleta = findViewById(R.id.ivMascotaCompleta);

        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtEspecie = findViewById(R.id.txtEspecie);
        txtRaza = findViewById(R.id.txtRaza);
        txtEdad = findViewById(R.id.txtEdad);
        txtPeso = findViewById(R.id.txtPeso);
        txtNacimiento = findViewById(R.id.txtNacimiento);
        txtSexo = findViewById(R.id.txtSexo);

        btnAlmorzo = findViewById(R.id.btnAlmorzo);
        btnComida = findViewById(R.id.btnComida);
        btnCea = findViewById(R.id.btnCea);

        btnEditarMascota = findViewById(R.id.btnEditarMascota);
        btnGestionarUsuarios = findViewById(R.id.btnGestionarUsuarios);

        mascota =
                (MascotaDTO)
                        getIntent().getSerializableExtra("mascota");

        setSupportActionBar(toolbarMascota);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (mascota != null) {

                getSupportActionBar().setTitle(mascota.getNombre());

            } else {

                getSupportActionBar().setTitle("Mascota");
            }
        }

        toolbarMascota.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
            }
        });

        mostrarDatos();
        configurarBotonGestionUsuarios();
        cargarComidaHoy();

        ivMascotaCompleta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                selectorImagen.launch("image/*");
            }
        });

        btnAlmorzo.setOnClickListener(v -> cambiarAlmorzo());
        btnComida.setOnClickListener(v -> cambiarComida());
        btnCea.setOnClickListener(v -> cambiarCea());

        btnEditarMascota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mascota != null) {

                    Intent intent =
                            new Intent(
                                    MascotaCompletaActivity.this,
                                    AddMascotaActivity.class
                            );

                    intent.putExtra("mascota", mascota);
                    intent.putExtra("editar", true);

                    launcherEditar.launch(intent);
                }
            }
        });

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

    private void cargarComidaHoy() {

        if (mascota == null) {

            return;
        }

        String fechaHoy =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                ).format(new Date());

        comidaHoy =
                comidaDAO.obtenerPorMascotaYFecha(
                        mascota.getId(),
                        fechaHoy
                );

        if (comidaHoy == null) {

            comidaHoy =
                    new ComidaDTO(
                            mascota.getId(),
                            fechaHoy
                    );

            long id =
                    comidaDAO.insertar(comidaHoy);

            comidaHoy.setId((int) id);
        }

        actualizarBotonesComida();
    }

    private void cambiarAlmorzo() {

        if (comidaHoy == null) {

            return;
        }

        comidaHoy.setAlmorzoComido(
                !comidaHoy.isAlmorzoComido()
        );

        guardarComida();
    }

    private void cambiarComida() {

        if (comidaHoy == null) {

            return;
        }

        comidaHoy.setComidaComida(
                !comidaHoy.isComidaComida()
        );

        guardarComida();
    }

    private void cambiarCea() {

        if (comidaHoy == null) {

            return;
        }

        comidaHoy.setCeaComida(
                !comidaHoy.isCeaComida()
        );

        guardarComida();
    }

    private void guardarComida() {

        comidaHoy.setFechaActualizacion(
                System.currentTimeMillis()
        );

        comidaHoy.setPendienteSync(true);

        comidaDAO.actualizar(comidaHoy);

        actualizarBotonesComida();
    }

    private void actualizarBotonesComida() {

        if (comidaHoy == null) {

            return;
        }

        configurarBotonComida(
                btnAlmorzo,
                "Almorzo",
                comidaHoy.isAlmorzoComido()
        );

        configurarBotonComida(
                btnComida,
                "Comida",
                comidaHoy.isComidaComida()
        );

        configurarBotonComida(
                btnCea,
                "Cea",
                comidaHoy.isCeaComida()
        );
    }

    private void configurarBotonComida(
            MaterialButton boton,
            String texto,
            boolean comido
    ) {

        if (comido) {

            boton.setText(texto + ": Si");
            boton.setTextColor(0xFFFFFFFF);
            boton.setBackgroundTintList(
                    ColorStateList.valueOf(0xFF8F61FF)
            );
            boton.setStrokeColor(
                    ColorStateList.valueOf(0xFF8F61FF)
            );

        } else {

            boton.setText(texto + ": Non");
            boton.setTextColor(0xFF8F61FF);
            boton.setBackgroundTintList(
                    ColorStateList.valueOf(0xFFFFFFFF)
            );
            boton.setStrokeColor(
                    ColorStateList.valueOf(0xFF8F61FF)
            );
        }
    }

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