package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.UsuarioDTO;
import com.example.petcare.supabase.SupabaseAuthManager;
import com.example.petcare.supabase.SupabaseSyncManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;

    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Se xa hai sesión gardada, entrar directamente
        String usuarioId =
                SesionUsuario.obtenerUsuarioId(this);

        if (!usuarioId.isEmpty()) {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            HomeActivity.class
                    );

            startActivity(intent);
            finish();

            return;
        }

        // Base de datos
        usuarioDAO =
                PetCareDatabase
                        .getInstance(this)
                        .usuarioDAO();

        // Referencias XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Botón iniciar sesión
        btnLogin.setOnClickListener(v -> iniciarSesion());

        // Abrir registro
        tvRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent =
                        new Intent(
                                LoginActivity.this,
                                RegisterActivity.class
                        );

                startActivity(intent);
            }
        });

        // Recuperar contraseña
        tvForgotPassword.setOnClickListener(v -> recuperarContrasinal());
    }

    private void iniciarSesion() {

        String email;

        if (etEmail.getText() != null) {

            email = etEmail.getText().toString().trim();

        } else {

            email = "";
        }

        String password;

        if (etPassword.getText() != null) {

            password = etPassword.getText().toString().trim();

        } else {

            password = "";
        }

        if (email.isEmpty()) {

            etEmail.setError("Introduce o correo electrónico");
            return;
        }

        if (password.isEmpty()) {

            etPassword.setError("Introduce o contrasinal");
            return;
        }

        btnLogin.setEnabled(false);

        SupabaseAuthManager.iniciarSesion(
                email,
                password,
                new SupabaseAuthManager.AuthCallback() {

                    @Override
                    public void onSuccess(
                            String supabaseUserId,
                            String email,
                            String accessToken
                    ) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                comprobarUsuarioLocal(
                                        supabaseUserId,
                                        email
                                );
                            }
                        });
                    }

                    @Override
                    public void onError(String mensaje) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                btnLogin.setEnabled(true);

                                Toast.makeText(
                                        LoginActivity.this,
                                        mensaje,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                    }
                }
        );
    }

    private void comprobarUsuarioLocal(
            String supabaseUserId,
            String email
    ) {

        UsuarioDTO usuario =
                usuarioDAO.obtenerPorEmail(email);

        if (usuario == null) {

            String nombre =
                    email.split("@")[0];

            usuario =
                    new UsuarioDTO(
                            nombre,
                            email
                    );

            usuario.setSupabaseId(supabaseUserId);
            usuario.setFechaCreacion(System.currentTimeMillis());
            usuario.setFechaActualizacion(System.currentTimeMillis());
            usuario.setPendienteSync(true);

            usuarioDAO.insertar(usuario);

            usuario =
                    usuarioDAO.obtenerPorEmail(email);

        } else {

            usuario.setSupabaseId(supabaseUserId);
            usuario.setFechaActualizacion(System.currentTimeMillis());
            usuario.setPendienteSync(true);

            usuarioDAO.actualizar(usuario);
        }

        if (usuario == null) {

            btnLogin.setEnabled(true);

            Toast.makeText(
                    this,
                    "Non se puido gardar o usuario local",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Gardar sesión local
        SesionUsuario.guardarUsuario(
                this,
                String.valueOf(usuario.getId()),
                usuario.getSupabaseId(),
                usuario.getEmail()
        );

        // Sincronizar datos pendentes
        SupabaseSyncManager.sincronizar(this);

        Toast.makeText(
                this,
                "Acceso correcto",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        HomeActivity.class
                );

        startActivity(intent);

        finish();
    }

    private void recuperarContrasinal() {

        String email;

        if (etEmail.getText() != null) {

            email = etEmail.getText().toString().trim();

        } else {

            email = "";
        }

        if (email.isEmpty()) {

            etEmail.setError("Introduce o correo electrónico");
            return;
        }

        SupabaseAuthManager.recuperarContrasinal(
                email,
                new SupabaseAuthManager.SimpleCallback() {

                    @Override
                    public void onSuccess() {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Enviouse un correo para recuperar o contrasinal",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String mensaje) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                Toast.makeText(
                                        LoginActivity.this,
                                        mensaje,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                    }
                }
        );
    }
}