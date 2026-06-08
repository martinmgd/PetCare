package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.UsuarioDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etEmailRegister;
    TextInputEditText etPasswordRegister;
    TextInputEditText etPasswordRepeatRegister;

    MaterialButton btnRegister;
    TextView tvLogin;

    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // Base de datos
        usuarioDAO =
                PetCareDatabase
                        .getInstance(this)
                        .usuarioDAO();

        // Referencias
        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etPasswordRepeatRegister = findViewById(R.id.etPasswordRepeatRegister);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Registrar usuario
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                registrarUsuario();
            }
        });

        // Volver al login
        tvLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent =
                        new Intent(
                                RegisterActivity.this,
                                LoginActivity.class
                        );

                startActivity(intent);

                finish();
            }
        });
    }

    private void registrarUsuario() {

        String email;

        if (etEmailRegister.getText() != null) {

            email = etEmailRegister.getText().toString().trim();

        } else {

            email = "";
        }

        String password;

        if (etPasswordRegister.getText() != null) {

            password = etPasswordRegister.getText().toString().trim();

        } else {

            password = "";
        }

        String passwordRepeat;

        if (etPasswordRepeatRegister.getText() != null) {

            passwordRepeat = etPasswordRepeatRegister.getText().toString().trim();

        } else {

            passwordRepeat = "";
        }

        if (email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {

            Toast.makeText(
                    RegisterActivity.this,
                    "Cubre todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!password.equals(passwordRepeat)) {

            Toast.makeText(
                    RegisterActivity.this,
                    "As contrasinais non coinciden",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        UsuarioDTO usuarioExistente =
                usuarioDAO.obtenerPorEmail(email);

        if (usuarioExistente != null) {

            Toast.makeText(
                    RegisterActivity.this,
                    "Este correo xa está rexistrado",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String nombre =
                email.split("@")[0];

        UsuarioDTO usuario =
                new UsuarioDTO(
                        nombre,
                        email
                );

        usuario.setFechaCreacion(System.currentTimeMillis());
        usuario.setFechaActualizacion(System.currentTimeMillis());
        usuario.setPendienteSync(true);

        usuarioDAO.insertar(usuario);

        Toast.makeText(
                RegisterActivity.this,
                "Rexistro correcto",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        RegisterActivity.this,
                        LoginActivity.class
                );

        startActivity(intent);

        finish();
    }
}