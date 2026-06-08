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
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMascotasHome;
    private RecyclerView rvEventosHome;

    private BottomNavigationView bottomNavigation;

    private EventoDAO eventoDAO;
    private MascotaDAO mascotaDAO;

    private EventoAdapter eventoAdapter;
    private MascotaAdapter mascotaAdapter;

    private ArrayList<MascotaDTO> listaMascotas;
    private ArrayList<EventoDTO> listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        mascotaDAO = db.mascotaDAO();
        eventoDAO = db.eventoDAO();

        // Referencias XML
        rvMascotasHome = findViewById(R.id.rvMascotasHome);
        rvEventosHome = findViewById(R.id.rvEventosHome);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Listas
        listaMascotas = new ArrayList<>();
        listaEventos = new ArrayList<>();

        // Adapter mascotas
        mascotaAdapter =
                new MascotaAdapter(listaMascotas);

        // RecyclerView mascotas
        rvMascotasHome.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        rvMascotasHome.setAdapter(mascotaAdapter);

        // Adapter eventos
        eventoAdapter =
                new EventoAdapter(listaEventos);

        // RecyclerView eventos
        rvEventosHome.setLayoutManager(
                new LinearLayoutManager(this)
        );

        rvEventosHome.setAdapter(eventoAdapter);

        // Cargar datos
        cargarDatos();

        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_inicio);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if (id == R.id.nav_inicio) {

                return true;
            }

            // Mascotas
            if (id == R.id.nav_mascotas) {

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
            if (id == R.id.nav_eventos) {

                Intent intent =
                        new Intent(
                                HomeActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Saúde
            if (id == R.id.nav_saude) {

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
            if (id == R.id.nav_perfil) {

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

    // Cargar datos desde Room
    private void cargarDatos() {

        // Cargar mascotas
        listaMascotas.clear();
        listaMascotas.addAll(mascotaDAO.listar());

        // Cargar eventos
        listaEventos.clear();
        listaEventos.addAll(eventoDAO.listar());

        // Actualizar RecyclerView
        mascotaAdapter.notifyDataSetChanged();
        eventoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Actualizar datos ao volver
        cargarDatos();
    }
}