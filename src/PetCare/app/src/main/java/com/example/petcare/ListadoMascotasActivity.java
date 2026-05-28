package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.MascotaAdapter;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ListadoMascotasActivity extends AppCompatActivity {

    private RecyclerView rvMascotas;
    private MaterialButton btnAddMascota;
    private BottomNavigationView bottomNavigation;

    private MascotaAdapter miAdaptador;
    private MascotaDAO dao;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == RESULT_OK && result.getData() != null){

                    MascotaDTO mascota =
                            (MascotaDTO) result.getData().getSerializableExtra("mascota");

                    if(mascota != null){

                        dao.insertar(mascota);
                        miAdaptador.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado_mascotas);

        dao = new MascotaDAO();

        // Referencias XML
        rvMascotas = findViewById(R.id.rvMascotas);
        btnAddMascota = findViewById(R.id.btnAddMascota);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // RecyclerView vertical
        miAdaptador = new MascotaAdapter(dao.listar());

        rvMascotas.setLayoutManager(new LinearLayoutManager(this));
        rvMascotas.setAdapter(miAdaptador);

        // Botón añadir mascota
        btnAddMascota.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ListadoMascotasActivity.this,
                            AddMascotaActivity.class
                    );

            launcher.launch(intent);
        });

// Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_mascotas);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if(id == R.id.nav_inicio){

                Intent intent =
                        new Intent(
                                ListadoMascotasActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Mascotas
            if(id == R.id.nav_mascotas){

                return true;
            }

            // Eventos
            if(id == R.id.nav_eventos){

//                Intent intent =
//                        new Intent(
//                                ListadoMascotasActivity.this,
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
//                                ListadoMascotasActivity.this,
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
//                                ListadoMascotasActivity.this,
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

    @Override
    protected void onResume() {

        super.onResume();

        if(miAdaptador != null){

            miAdaptador.notifyDataSetChanged();
        }
    }
}