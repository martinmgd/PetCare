package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etEmailRegister;
    TextInputEditText etPasswordRegister;
    TextInputEditText etPasswordRepeatRegister;

    MaterialButton btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

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

                String email = etEmailRegister.getText().toString();
                String password = etPasswordRegister.getText().toString();
                String passwordRepeat = etPasswordRepeatRegister.getText().toString();

                if(email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()){

                    Toast.makeText(RegisterActivity.this, "Cubre todos os campos", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(!password.equals(passwordRepeat)){

                    Toast.makeText(RegisterActivity.this, "As contrasinais non coinciden", Toast.LENGTH_SHORT).show();

                    return;
                }

                Toast.makeText(RegisterActivity.this, "Rexistro correcto", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                startActivity(intent);

                finish();
            }
        });

        // Volver al login
        tvLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                startActivity(intent);

                finish();
            }
        });
    }
}