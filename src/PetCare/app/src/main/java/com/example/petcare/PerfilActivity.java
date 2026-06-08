package com.example.petcare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.UsuarioDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class PerfilActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private TextView txtNomePerfil;
    private TextView txtCorreoPerfil;

    private TextView txtNomeConta;
    private TextView txtCorreoConta;
    private TextView txtUsuarioId;

    private MaterialButton btnCopiarId;
    private MaterialButton btnEditarPerfil;
    private MaterialButton btnCerrarSesion;

    private UsuarioDAO usuarioDAO;
    private UsuarioDTO usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        // Base de datos
        usuarioDAO =
                PetCareDatabase
                        .getInstance(this)
                        .usuarioDAO();

        // Referencias XML
        bottomNavigation = findViewById(R.id.bottomNavigation);

        txtNomePerfil = findViewById(R.id.txtNomePerfil);
        txtCorreoPerfil = findViewById(R.id.txtCorreoPerfil);

        txtNomeConta = findViewById(R.id.txtNomeConta);
        txtCorreoConta = findViewById(R.id.txtCorreoConta);
        txtUsuarioId = findViewById(R.id.txtUsuarioId);

        btnCopiarId = findViewById(R.id.btnCopiarId);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Cargar datos reais do usuario
        cargarUsuario();

        // Copiar ID
        btnCopiarId.setOnClickListener(v -> copiarUsuarioId());

        // Editar perfil
        btnEditarPerfil.setOnClickListener(v -> {

            Toast.makeText(
                    PerfilActivity.this,
                    "Edición de perfil pendente",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Pechar sesión
        btnCerrarSesion.setOnClickListener(v -> {

            SesionUsuario.cerrarSesion(PerfilActivity.this);

            Intent intent =
                    new Intent(
                            PerfilActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);
            finish();
        });

        configurarBottomNavigation();
    }

    // Cargar usuario gardado na sesión
    private void cargarUsuario() {

        String usuarioId =
                SesionUsuario.obtenerUsuarioId(this);

        if (usuarioId.isEmpty()) {

            volverLogin();
            return;
        }

        int idUsuario;

        try {

            idUsuario = Integer.parseInt(usuarioId);

        } catch (Exception e) {

            volverLogin();
            return;
        }

        usuarioActual =
                usuarioDAO.obtenerPorId(idUsuario);

        if (usuarioActual == null) {

            volverLogin();
            return;
        }

        String nombre = usuarioActual.getNombre();
        String email = usuarioActual.getEmail();

        txtNomePerfil.setText(nombre);
        txtCorreoPerfil.setText(email);

        txtNomeConta.setText("Nome: " + nombre);
        txtCorreoConta.setText("Correo: " + email);

        txtUsuarioId.setText("ID usuario: " + usuarioActual.getId());
    }

    // Copiar ID do usuario ao portapapeis
    private void copiarUsuarioId() {

        if (usuarioActual == null) {

            return;
        }

        String usuarioId =
                String.valueOf(usuarioActual.getId());

        ClipboardManager clipboard =
                (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip =
                ClipData.newPlainText(
                        "UsuarioID",
                        usuarioId
                );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                "ID copiado",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Volver ao login se non hai usuario
    private void volverLogin() {

        SesionUsuario.cerrarSesion(this);

        Intent intent =
                new Intent(
                        PerfilActivity.this,
                        LoginActivity.class
                );

        startActivity(intent);
        finish();
    }

    // Bottom Navigation
    private void configurarBottomNavigation() {

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_inicio) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_mascotas) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_eventos) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_saude) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                SaudeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_perfil) {

                return true;
            }

            return false;
        });

        bottomNavigation.setSelectedItemId(R.id.nav_perfil);
    }
}