package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.EventoAdapter;
import com.example.petcare.adapter.MascotaAdapter;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MascotaDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMascotasHome;
    private RecyclerView rvEventosHome;

    private BottomNavigationView bottomNavigation;

    // DAO e Adapter de eventos
    private EventoDAO eventoDAO;
    private EventoAdapter eventoAdapter;

    // DAO e Adapter de mascotas
    private MascotaDAO mascotaDAO;
    private MascotaAdapter mascotaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        // Referencias aos compoñentes do XML
        rvMascotasHome = findViewById(R.id.rvMascotasHome);
        rvEventosHome = findViewById(R.id.rvEventosHome);
        bottomNavigation = findViewById(R.id.bottomNavigation);


        // MASCOTAS

        // Obtén a lista de mascotas gardadas
        mascotaDAO = new MascotaDAO();

        // Crea o adaptador coas mascotas
        mascotaAdapter =
                new MascotaAdapter(
                        mascotaDAO.listar()
                );

        // RecyclerView horizontal para mostrar as mascotas
        rvMascotasHome.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        // Asigna o adaptador ao RecyclerView
        rvMascotasHome.setAdapter(mascotaAdapter);


        // EVENTOS

        // Obtén a lista de eventos gardados
        eventoDAO = new EventoDAO();

        // Crea o adaptador cos eventos
        eventoAdapter =
                new EventoAdapter(
                        eventoDAO.listar()
                );

        // RecyclerView vertical para os eventos
        rvEventosHome.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Asigna o adaptador ao RecyclerView
        rvEventosHome.setAdapter(eventoAdapter);


        // MENÚ INFERIOR

        // Marca Inicio como pantalla actual
        bottomNavigation.setSelectedItemId(R.id.nav_inicio);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Xa estamos en Inicio
            if(id == R.id.nav_inicio){
                return true;
            }

            // Ir a Mascotas
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

            // Ir a Eventos
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

            // Saúde (pendente de implementar)
            if(id == R.id.nav_saude){

                Intent intent =
                        new Intent(
                                HomeActivity.this,
                                SaudeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Perfil
            if(id == R.id.nav_perfil){

                Intent intent =
                        new Intent(
                                HomeActivity.this,
                                PerfilActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Actualiza a lista de mascotas ao volver á pantalla
        if(mascotaAdapter != null){

            mascotaAdapter.notifyDataSetChanged();
        }

        // Actualiza a lista de eventos ao volver á pantalla
        if(eventoAdapter != null){

            eventoAdapter.notifyDataSetChanged();
        }
    }
}