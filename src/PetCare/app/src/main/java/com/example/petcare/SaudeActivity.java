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
    private MaterialCardView cardCitas;
    private MaterialCardView cardMedicacion;
    private MaterialCardView cardHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saude);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        cardActividade = findViewById(R.id.cardActividade);
        cardPeso = findViewById(R.id.cardPeso);
        cardVacinas = findViewById(R.id.cardVacinas);
        cardCitas = findViewById(R.id.cardCitas);
        cardMedicacion = findViewById(R.id.cardMedicacion);
        cardHistorial = findViewById(R.id.cardHistorial);

        cardActividade.setOnClickListener(v -> mostrarPendente("Actividade"));
        cardPeso.setOnClickListener(v -> mostrarPendente("Peso"));
        cardVacinas.setOnClickListener(v -> mostrarPendente("Vacinas"));
        cardCitas.setOnClickListener(v -> mostrarPendente("Citas veterinarias"));
        cardMedicacion.setOnClickListener(v -> mostrarPendente("Medicación"));
        cardHistorial.setOnClickListener(v -> mostrarPendente("Historial"));

        bottomNavigation.setSelectedItemId(R.id.nav_saude);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if(id == R.id.nav_inicio){

                startActivity(new Intent(SaudeActivity.this, HomeActivity.class));
                finish();

                return true;
            }

            if(id == R.id.nav_mascotas){

                startActivity(new Intent(SaudeActivity.this, ListadoMascotasActivity.class));
                finish();

                return true;
            }

            if(id == R.id.nav_eventos){

                startActivity(new Intent(SaudeActivity.this, EventosActivity.class));
                finish();

                return true;
            }

            if(id == R.id.nav_saude){

                return true;
            }

            if(id == R.id.nav_perfil){

                return true;
            }

            return false;
        });
    }

    private void mostrarPendente(String apartado) {

        Toast.makeText(
                this,
                apartado + " pendente de implementar",
                Toast.LENGTH_SHORT
        ).show();
    }
}