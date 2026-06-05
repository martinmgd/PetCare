package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class PerfilActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private MaterialButton btnEditarPerfil;
    private MaterialButton btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        // Referencias XML
        bottomNavigation = findViewById(R.id.bottomNavigation);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

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

            Intent intent =
                    new Intent(
                            PerfilActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);
            finish();
        });

        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_perfil);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if(id == R.id.nav_inicio){

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Mascotas
            if(id == R.id.nav_mascotas){

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Eventos
            if(id == R.id.nav_eventos){

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Saúde
            if(id == R.id.nav_saude){

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                SaudeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Perfil
            if(id == R.id.nav_perfil){

                return true;
            }

            return false;
        });
    }
}