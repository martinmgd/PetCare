package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private MaterialButton btnAddEvento;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eventos);

        // Referencias XML
        rvEventos = findViewById(R.id.rvEventos);
        btnAddEvento = findViewById(R.id.btnAddEvento);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // RecyclerView vertical
        rvEventos.setLayoutManager(new LinearLayoutManager(this));

        // Botón añadir evento
        btnAddEvento.setOnClickListener(v -> {

            // Aquí abriremos AddEventoActivity más adelante
            // Intent intent = new Intent(EventosActivity.this, AddEventoActivity.class);
            // startActivity(intent);

        });

        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_eventos);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if(id == R.id.nav_inicio){

                Intent intent =
                        new Intent(
                                EventosActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if(id == R.id.nav_mascotas){

                Intent intent =
                        new Intent(
                                EventosActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if(id == R.id.nav_eventos){

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
}