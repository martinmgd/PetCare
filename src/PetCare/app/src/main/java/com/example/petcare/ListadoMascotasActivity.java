package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.MascotaAdapter;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListadoMascotasActivity extends AppCompatActivity {

    private RecyclerView rvMascotas;
    private ImageButton btnAddMascota;
    private BottomNavigationView bottomNavigation;

    private MascotaAdapter miAdaptador;
    private MascotaDAO dao;
    private UsuarioMascotaDAO usuarioMascotaDAO;

    private ArrayList<MascotaDTO> listaMascotas;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            MascotaDTO mascota =
                                    (MascotaDTO) result.getData()
                                            .getSerializableExtra("mascota");

                            if (mascota != null) {

                                // Gardar mascota en Room
                                dao.insertar(mascota);

                                // Recargar lista
                                cargarMascotas();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado_mascotas);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        dao = db.mascotaDAO();
        usuarioMascotaDAO = db.usuarioMascotaDAO();

        // Referencias XML
        rvMascotas = findViewById(R.id.rvMascotas);
        btnAddMascota = findViewById(R.id.btnAddMascota);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Lista de mascotas
        listaMascotas = new ArrayList<>();

        // RecyclerView vertical
        miAdaptador =
                new MascotaAdapter(
                        listaMascotas
                );

        rvMascotas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        rvMascotas.setAdapter(miAdaptador);

        // Cargar mascotas
        cargarMascotas();

        // Añadir mascota
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

            if (id == R.id.nav_inicio) {

                Intent intent =
                        new Intent(
                                ListadoMascotasActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_mascotas) {

                return true;
            }

            if (id == R.id.nav_eventos) {

                Intent intent =
                        new Intent(
                                ListadoMascotasActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_saude) {

                Intent intent =
                        new Intent(
                                ListadoMascotasActivity.this,
                                SaudeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            if (id == R.id.nav_perfil) {

                Intent intent =
                        new Intent(
                                ListadoMascotasActivity.this,
                                PerfilActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }

    // Cargar mascotas desde Room
    private void cargarMascotas() {

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        listaMascotas.clear();

        // Mascotas propias
        listaMascotas.addAll(
                dao.listarPorPropietario(usuarioActualId)
        );

        // Mascotas compartidas aceptadas
        ArrayList<UsuarioMascotaDTO> relaciones =
                new ArrayList<>(
                        usuarioMascotaDAO.listarAceptadasPorUsuario(
                                usuarioActualId
                        )
                );

        for (UsuarioMascotaDTO relacion : relaciones) {

            MascotaDTO mascota =
                    dao.obtenerPorId(
                            relacion.getMascotaId()
                    );

            if (mascota != null) {

                agregarMascotaSiNoExiste(mascota);
            }
        }

        miAdaptador.notifyDataSetChanged();
    }

    // Evitar mascotas duplicadas
    private void agregarMascotaSiNoExiste(MascotaDTO mascota) {

        boolean existe = false;

        for (MascotaDTO mascotaLista : listaMascotas) {

            if (mascotaLista.getId() == mascota.getId()) {

                existe = true;
                break;
            }
        }

        if (!existe) {

            listaMascotas.add(mascota);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (miAdaptador != null) {

            cargarMascotas();
        }
    }
}