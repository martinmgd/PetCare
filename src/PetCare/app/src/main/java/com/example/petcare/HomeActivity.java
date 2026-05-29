package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.EventoAdapter;
import com.example.petcare.dao.EventoDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMascotasHome;
    private RecyclerView rvEventosHome;

    private BottomNavigationView bottomNavigation;

    private EventoDAO eventoDAO;
    private EventoAdapter eventoAdapter;

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
        eventoDAO = new EventoDAO();
        eventoAdapter = new EventoAdapter(eventoDAO.listar());

        rvEventosHome.setLayoutManager(new LinearLayoutManager(this));
        rvEventosHome.setAdapter(eventoAdapter);

        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_inicio);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if(id == R.id.nav_inicio){
                return true;
            }

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

            if(id == R.id.nav_eventos){

                Intent intent =
                        new Intent(
                                HomeActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
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

    @Override
    protected void onResume() {

        super.onResume();

        if(eventoAdapter != null){
            eventoAdapter.notifyDataSetChanged();
        }
    }
}