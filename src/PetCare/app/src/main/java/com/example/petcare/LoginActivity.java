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
        tvForgotPassword.setOnClickListener(v ->

                Toast.makeText(
                        LoginActivity.this,
                        "Recuperación de contrasinal pendente",
                        Toast.LENGTH_SHORT
                ).show()
        );
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

        UsuarioDTO usuario =
                usuarioDAO.obtenerPorEmail(email);

        if (usuario == null) {

            Toast.makeText(
                    this,
                    "Usuario non rexistrado",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Gardar sesión local
        SesionUsuario.guardarUsuario(
                this,
                String.valueOf(usuario.getId()),
                usuario.getEmail()
        );

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
}