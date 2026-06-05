package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class SaudeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private MaterialCardView cardActividade;
    private MaterialCardView cardPeso;
    private MaterialCardView cardVacinas;
    private MaterialCardView cardMedicacion;
    private MaterialCardView cardHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saude);

        // Referencias XML
        bottomNavigation = findViewById(R.id.bottomNavigation);

        cardActividade = findViewById(R.id.cardActividade);
        cardPeso = findViewById(R.id.cardPeso);
        cardVacinas = findViewById(R.id.cardVacinas);
        cardMedicacion = findViewById(R.id.cardMedicacion);
        cardHistorial = findViewById(R.id.cardHistorial);

        // Actividade
        cardActividade.setOnClickListener(v ->
                mostrarPendente("Actividade")
        );

        // Peso
        cardPeso.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SaudeActivity.this,
                            PesoActivity.class
                    );

            startActivity(intent);
        });

        // Vacinas
        cardVacinas.setOnClickListener(v ->
                mostrarPendente("Vacinas")
        );

        // Medicación
        cardMedicacion.setOnClickListener(v ->
                mostrarPendente("Medicación")
        );

        // Historial
        cardHistorial.setOnClickListener(v ->
                mostrarPendente("Historial")
        );

        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_saude);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if(id == R.id.nav_inicio){

                Intent intent =
                        new Intent(
                                SaudeActivity.this,
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
                                SaudeActivity.this,
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
                                SaudeActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Saúde
            if(id == R.id.nav_saude){

                return true;
            }

            // Perfil
            if(id == R.id.nav_perfil){

                Intent intent =
                        new Intent(
                                SaudeActivity.this,
                                PerfilActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }

    // Mostra unha mensaxe para os apartados aínda non implementados
    private void mostrarPendente(String apartado) {

        Toast.makeText(
                this,
                apartado + " pendente de implementar",
                Toast.LENGTH_SHORT
        ).show();
    }
}