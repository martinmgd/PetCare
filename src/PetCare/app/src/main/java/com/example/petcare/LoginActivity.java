package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private MaterialButton btnLogin;

    private TextView tvRegister;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Referencias XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Botón iniciar sesión
        btnLogin.setOnClickListener(v -> iniciarSesion());

        // Registro (por ahora pendiente)
        tvRegister.setOnClickListener(v ->

                Toast.makeText(
                        LoginActivity.this,
                        "Pantalla de rexistro pendente",
                        Toast.LENGTH_SHORT
                ).show()
        );


        // Recuperar contraseña (por ahora pendiente)
        tvForgotPassword.setOnClickListener(v ->

                Toast.makeText(
                        LoginActivity.this,
                        "Recuperación de contrasinal pendente",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void iniciarSesion() {

        String email =
                etEmail.getText() != null
                        ? etEmail.getText().toString().trim()
                        : "";

        String password =
                etPassword.getText() != null
                        ? etPassword.getText().toString().trim()
                        : "";

        if(email.isEmpty()){

            etEmail.setError("Introduce o correo electrónico");
            return;
        }

        if(password.isEmpty()){

            etPassword.setError("Introduce o contrasinal");
            return;
        }

        // Aquí irá Supabase más adelante
        Toast.makeText(
                this,
                "Inicio de sesión correcto",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        ListadoMascotasActivity.class
                );

        startActivity(intent);

        finish();
    }
}