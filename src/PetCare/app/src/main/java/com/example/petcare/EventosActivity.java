package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.EventoAdapter;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dto.EventoDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private MaterialButton btnAddEvento;
    private BottomNavigationView bottomNavigation;

    private EventoDAO dao;
    private EventoAdapter miAdaptador;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == RESULT_OK && result.getData() != null){

                    EventoDTO evento =
                            (EventoDTO) result.getData().getSerializableExtra("evento");

                    if(evento != null){

                        dao.insertar(evento);
                        miAdaptador.notifyDataSetChanged();

                    }

                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eventos);

        dao = new EventoDAO();

        // Referencias XML
        rvEventos = findViewById(R.id.rvEventos);
        btnAddEvento = findViewById(R.id.btnAddEvento);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // RecyclerView vertical
        miAdaptador = new EventoAdapter(dao.listar());

        rvEventos.setLayoutManager(new LinearLayoutManager(this));
        rvEventos.setAdapter(miAdaptador);

        // Botón añadir evento
        btnAddEvento.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            EventosActivity.this,
                            AddEventoActivity.class
                    );

            launcher.launch(intent);

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

    @Override
    protected void onResume() {

        super.onResume();

        if(miAdaptador != null){

            miAdaptador.notifyDataSetChanged();

        }
    }
}