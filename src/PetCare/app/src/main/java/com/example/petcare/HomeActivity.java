package com.example.petcare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.EventoAdapter;
import com.example.petcare.adapter.MascotaAdapter;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.InvitacionDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMascotasHome;
    private RecyclerView rvEventosHome;

    private BottomNavigationView bottomNavigation;

    private EventoDAO eventoDAO;
    private MascotaDAO mascotaDAO;
    private UsuarioMascotaDAO usuarioMascotaDAO;
    private InvitacionDAO invitacionDAO;

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
        usuarioMascotaDAO = db.usuarioMascotaDAO();
        invitacionDAO = db.invitacionDAO();

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

        rvEventosHome.setLayoutManager(
                new LinearLayoutManager(this)
        );

        rvEventosHome.setAdapter(eventoAdapter);

        // Cargar datos
        cargarDatos();

        // Bottom Navigation
        configurarBottomNavigation();

        // Badge de invitacións pendentes
        actualizarBadgePerfil();
    }

    // Bottom Navigation
    private void configurarBottomNavigation() {

        bottomNavigation.setSelectedItemId(R.id.nav_inicio);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_inicio) {

                return true;
            }

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

    // Mostrar marca vermella se hai invitacións pendentes
    private void actualizarBadgePerfil() {

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        if (usuarioActualId.isEmpty()) {

            bottomNavigation.removeBadge(R.id.nav_perfil);
            return;
        }

        int pendientes =
                invitacionDAO
                        .listarPendientesPorUsuario(usuarioActualId)
                        .size();

        if (pendientes > 0) {

            BadgeDrawable badge =
                    bottomNavigation.getOrCreateBadge(R.id.nav_perfil);

            badge.setVisible(true);
            badge.setBackgroundColor(Color.RED);
            badge.setBadgeTextColor(Color.WHITE);
            badge.setNumber(pendientes);

        } else {

            bottomNavigation.removeBadge(R.id.nav_perfil);
        }
    }

    // Cargar datos desde Room
    private void cargarDatos() {

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        listaMascotas.clear();

        // Mascotas propias
        listaMascotas.addAll(
                mascotaDAO.listarPorPropietario(usuarioActualId)
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
                    mascotaDAO.obtenerPorId(
                            relacion.getMascotaId()
                    );

            if (mascota != null) {

                agregarMascotaSiNoExiste(mascota);
            }
        }

        // Eventos das mascotas ás que o usuario ten acceso
        listaEventos.clear();

        ArrayList<Integer> mascotasIds =
                obtenerIdsMascotasPermitidas();

        if (!mascotasIds.isEmpty()) {

            listaEventos.addAll(
                    eventoDAO.listarPorMascotas(mascotasIds)
            );
        }

        mascotaAdapter.notifyDataSetChanged();
        eventoAdapter.notifyDataSetChanged();
    }

    // Obter ids das mascotas visibles
    private ArrayList<Integer> obtenerIdsMascotasPermitidas() {

        ArrayList<Integer> mascotasIds =
                new ArrayList<>();

        for (MascotaDTO mascota : listaMascotas) {

            mascotasIds.add(
                    mascota.getId()
            );
        }

        return mascotasIds;
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

        cargarDatos();
        actualizarBadgePerfil();
    }
}