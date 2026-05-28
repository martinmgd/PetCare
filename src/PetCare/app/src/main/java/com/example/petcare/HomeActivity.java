package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMascotasHome;
    private RecyclerView rvEventosHome;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        // Referencias XML
        rvMascotasHome = findViewById(R.id.rvMascotasHome);
        rvEventosHome = findViewById(R.id.rvEventosHome);

        bottomNavigation = findViewById(R.id.bottomNavigation);


        // Mascotas → scroll horizontal
        rvMascotasHome.setLayoutManager(

                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );


        // Eventos → vertical
        rvEventosHome.setLayoutManager(

                new LinearLayoutManager(this)
        );


        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_inicio);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if(id == R.id.nav_inicio){

                return true;
            }

            // Mascotas
            if(id == R.id.nav_mascotas){

                Intent intent =
                        new Intent(
                                HomeActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Eventos
            if(id == R.id.nav_eventos){

//                Intent intent =
//                        new Intent(
//                                HomeActivity.this,
//                                EventosActivity.class
//                        );
//
//                startActivity(intent);
//                finish();

                return true;
            }

            // Saúde
            if(id == R.id.nav_saude){

//                Intent intent =
//                        new Intent(
//                                HomeActivity.this,
//                                SaudeActivity.class
//                        );
//
//                startActivity(intent);
//                finish();

                return true;
            }

            // Perfil
            if(id == R.id.nav_perfil){

//                Intent intent =
//                        new Intent(
//                                HomeActivity.this,
//                                PerfilActivity.class
//                        );
//
//                startActivity(intent);
//                finish();

                return true;
            }

            return false;
        });
    }
}