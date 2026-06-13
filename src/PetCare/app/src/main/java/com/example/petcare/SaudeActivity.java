package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import com.example.petcare.util.SwipeNavigationHelper;

public class SaudeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private MaterialCardView cardActividade;
    private MaterialCardView cardPeso;
    private MaterialCardView cardVacinas;
    private MaterialCardView cardMedicacion;
    private MaterialCardView cardHistorial;

    private SwipeNavigationHelper swipeNavigationHelper;

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
        cardActividade.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SaudeActivity.this,
                            ActividadeActivity.class
                    );

            startActivity(intent);
        });

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
        cardVacinas.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SaudeActivity.this,
                            VacinasActivity.class
                    );

            startActivity(intent);
        });

        // Medicación
        cardMedicacion.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SaudeActivity.this,
                            MedicacionActivity.class
                    );

            startActivity(intent);
        });

        // Historial de saúde
        cardHistorial.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SaudeActivity.this,
                            HistorialSaudeActivity.class
                    );

            startActivity(intent);
        });

        configurarBottomNavigation();

        // Navegación entre apartados mediante deslizamiento
        swipeNavigationHelper =
                new SwipeNavigationHelper(
                        this,
                        PerfilActivity.class,
                        EventosActivity.class
                );
    }

    // Bottom Navigation
    private void configurarBottomNavigation() {

        bottomNavigation.setSelectedItemId(R.id.nav_saude);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if (id == R.id.nav_inicio) {

                Intent intent =
                        new Intent(
                                SaudeActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            // Mascotas
            if (id == R.id.nav_mascotas) {

                Intent intent =
                        new Intent(
                                SaudeActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            // Eventos
            if (id == R.id.nav_eventos) {

                Intent intent =
                        new Intent(
                                SaudeActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            // Saúde
            if (id == R.id.nav_saude) {

                return true;
            }

            // Perfil
            if (id == R.id.nav_perfil) {

                Intent intent =
                        new Intent(
                                SaudeActivity.this,
                                PerfilActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
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

    // Permitir navegar entre apartados deslizando o dedo
    @Override
    public boolean dispatchTouchEvent(android.view.MotionEvent event) {

        if (swipeNavigationHelper != null
                && swipeNavigationHelper.procesarEvento(event)) {

            return true;
        }

        return super.dispatchTouchEvent(event);
    }
}