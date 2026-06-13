package com.example.petcare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.dao.InvitacionDAO;
import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.InvitacionDTO;
import com.example.petcare.dto.UsuarioDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;
import com.example.petcare.util.SwipeNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import com.example.petcare.supabase.SupabaseSyncManager;

public class PerfilActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private TextView txtNomePerfil;
    private TextView txtCorreoPerfil;

    private TextView txtNomeConta;
    private TextView txtCorreoConta;
    private TextView txtUsuarioId;

    private TextView tvInvitacionesPendientes;
    private MaterialCardView cardInvitaciones;
    private LinearLayout contenedorInvitaciones;

    private MaterialButton btnCopiarId;
    private MaterialButton btnEditarPerfil;
    private MaterialButton btnCerrarSesion;

    private UsuarioDAO usuarioDAO;
    private InvitacionDAO invitacionDAO;
    private UsuarioMascotaDAO usuarioMascotaDAO;

    private UsuarioDTO usuarioActual;

    private SwipeNavigationHelper swipeNavigationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        usuarioDAO = db.usuarioDAO();
        invitacionDAO = db.invitacionDAO();
        usuarioMascotaDAO = db.usuarioMascotaDAO();

        // Referencias XML
        bottomNavigation = findViewById(R.id.bottomNavigation);

        txtNomePerfil = findViewById(R.id.txtNomePerfil);
        txtCorreoPerfil = findViewById(R.id.txtCorreoPerfil);

        txtNomeConta = findViewById(R.id.txtNomeConta);
        txtCorreoConta = findViewById(R.id.txtCorreoConta);
        txtUsuarioId = findViewById(R.id.txtUsuarioId);

        tvInvitacionesPendientes = findViewById(R.id.tvInvitacionesPendientes);
        cardInvitaciones = findViewById(R.id.cardInvitaciones);
        contenedorInvitaciones = findViewById(R.id.contenedorInvitaciones);

        btnCopiarId = findViewById(R.id.btnCopiarId);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Cargar datos reais do usuario
        cargarUsuario();

        // Cargar invitacións pendentes
        cargarInvitacionesPendientes();

        // Copiar ID
        btnCopiarId.setOnClickListener(v -> copiarUsuarioId());

        // Editar perfil
        btnEditarPerfil.setOnClickListener(v -> {

            Toast.makeText(
                    PerfilActivity.this,
                    "Edición de perfil pendente",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Pechar sesión
        btnCerrarSesion.setOnClickListener(v -> {

            SesionUsuario.cerrarSesion(PerfilActivity.this);

            Intent intent =
                    new Intent(
                            PerfilActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);
            finish();
        });

        configurarBottomNavigation();

        // Navegación entre apartados mediante deslizamiento
        swipeNavigationHelper =
                new SwipeNavigationHelper(
                        this,
                        HomeActivity.class,
                        SaudeActivity.class
                );
    }

    // Cargar usuario gardado na sesión
    private void cargarUsuario() {

        String usuarioId =
                SesionUsuario.obtenerUsuarioId(this);

        if (usuarioId.isEmpty()) {

            volverLogin();
            return;
        }

        int idUsuario;

        try {

            idUsuario = Integer.parseInt(usuarioId);

        } catch (Exception e) {

            volverLogin();
            return;
        }

        usuarioActual =
                usuarioDAO.obtenerPorId(idUsuario);

        if (usuarioActual == null) {

            volverLogin();
            return;
        }

        String nombre = usuarioActual.getNombre();
        String email = usuarioActual.getEmail();

        txtNomePerfil.setText(nombre);
        txtCorreoPerfil.setText(email);

        txtNomeConta.setText("Nome: " + nombre);
        txtCorreoConta.setText("Correo: " + email);

        if (usuarioActual.getSupabaseId() != null
                && !usuarioActual.getSupabaseId().isEmpty()) {

            txtUsuarioId.setText(
                    "ID usuario: "
                            + usuarioActual.getSupabaseId()
            );

        } else {

            txtUsuarioId.setText(
                    "ID usuario: pendente de sincronización"
            );
        }
    }

    // Cargar invitacións pendentes
    private void cargarInvitacionesPendientes() {

        if (usuarioActual == null) {

            tvInvitacionesPendientes.setVisibility(View.GONE);
            cardInvitaciones.setVisibility(View.GONE);
            return;
        }

        String usuarioId =
                usuarioActual.getSupabaseId();

        if (usuarioId == null || usuarioId.isEmpty()) {

            tvInvitacionesPendientes.setVisibility(View.GONE);
            cardInvitaciones.setVisibility(View.GONE);
            return;
        }

        ArrayList<InvitacionDTO> invitaciones =
                new ArrayList<>(
                        invitacionDAO.listarPendientesPorUsuario(usuarioId)
                );

        contenedorInvitaciones.removeAllViews();

        if (invitaciones.isEmpty()) {

            tvInvitacionesPendientes.setVisibility(View.GONE);
            cardInvitaciones.setVisibility(View.GONE);
            return;
        }

        tvInvitacionesPendientes.setVisibility(View.VISIBLE);
        cardInvitaciones.setVisibility(View.VISIBLE);

        tvInvitacionesPendientes.setText(
                "Invitacións pendentes (" + invitaciones.size() + ")"
        );

        for (InvitacionDTO invitacion : invitaciones) {

            crearVistaInvitacion(invitacion);
        }
    }

    // Crear vista dunha invitación
    private void crearVistaInvitacion(InvitacionDTO invitacion) {

        LinearLayout layoutInvitacion = new LinearLayout(this);
        layoutInvitacion.setOrientation(LinearLayout.VERTICAL);
        layoutInvitacion.setPadding(0, 0, 0, 22);

        TextView txtMascota = new TextView(this);
        txtMascota.setText("🐾 " + invitacion.getNombreMascota());
        txtMascota.setTextSize(17);
        txtMascota.setTextColor(getColor(android.R.color.black));
        txtMascota.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView txtInfo = new TextView(this);
        txtInfo.setText("Queren compartir esta mascota contigo");
        txtInfo.setTextSize(14);
        txtInfo.setTextColor(0xFF777777);
        txtInfo.setPadding(0, 6, 0, 12);

        LinearLayout layoutBotones = new LinearLayout(this);
        layoutBotones.setOrientation(LinearLayout.HORIZONTAL);

        MaterialButton btnAceptar = new MaterialButton(this);
        btnAceptar.setText("Aceptar");
        btnAceptar.setAllCaps(false);
        btnAceptar.setTextColor(0xFFFFFFFF);
        btnAceptar.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFF8F61FF)
        );

        MaterialButton btnRexeitar = new MaterialButton(this);
        btnRexeitar.setText("Rexeitar");
        btnRexeitar.setAllCaps(false);
        btnRexeitar.setTextColor(0xFF8F61FF);
        btnRexeitar.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFFFFFFFF)
        );
        btnRexeitar.setStrokeColor(
                android.content.res.ColorStateList.valueOf(0xFF8F61FF)
        );
        btnRexeitar.setStrokeWidth(1);

        LinearLayout.LayoutParams paramsBoton =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        paramsBoton.setMargins(0, 0, 8, 0);

        LinearLayout.LayoutParams paramsBoton2 =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        paramsBoton2.setMargins(8, 0, 0, 0);

        layoutBotones.addView(btnAceptar, paramsBoton);
        layoutBotones.addView(btnRexeitar, paramsBoton2);

        btnAceptar.setOnClickListener(v -> aceptarInvitacion(invitacion));
        btnRexeitar.setOnClickListener(v -> rexeitarInvitacion(invitacion));

        layoutInvitacion.addView(txtMascota);
        layoutInvitacion.addView(txtInfo);
        layoutInvitacion.addView(layoutBotones);

        contenedorInvitaciones.addView(layoutInvitacion);
    }

    // Aceptar invitación
    private void aceptarInvitacion(InvitacionDTO invitacion) {

        if (usuarioActual == null) {

            return;
        }

        String usuarioId =
                String.valueOf(usuarioActual.getId());

        UsuarioMascotaDTO relacionExistente =
                usuarioMascotaDAO.obtenerRelacion(
                        usuarioId,
                        invitacion.getMascotaId()
                );

        if (relacionExistente == null) {

            UsuarioMascotaDTO relacion =
                    new UsuarioMascotaDTO(
                            usuarioId,
                            invitacion.getMascotaId(),
                            "CUIDADOR",
                            "ACEPTADA"
                    );

            usuarioMascotaDAO.insertar(relacion);
        }

        invitacionDAO.actualizarEstado(
                invitacion.getId(),
                "ACEPTADA",
                System.currentTimeMillis()
        );

        SupabaseSyncManager.sincronizar(this);

        Toast.makeText(
                this,
                "Invitación aceptada",
                Toast.LENGTH_SHORT
        ).show();

        cargarInvitacionesPendientes();
    }
    // Rexeitar invitación
    private void rexeitarInvitacion(InvitacionDTO invitacion) {

        invitacionDAO.actualizarEstado(
                invitacion.getId(),
                "REXEITADA",
                System.currentTimeMillis()
        );

        Toast.makeText(
                this,
                "Invitación rexeitada",
                Toast.LENGTH_SHORT
        ).show();

        cargarInvitacionesPendientes();
    }

    // Copiar ID do usuario ao portapapeis
    private void copiarUsuarioId() {

        if (usuarioActual == null) {

            return;
        }

        String usuarioId =
                usuarioActual.getSupabaseId();

        if (usuarioId == null
                || usuarioId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Usuario pendente de sincronización",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        ClipboardManager clipboard =
                (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip =
                ClipData.newPlainText(
                        "UsuarioID",
                        usuarioId
                );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                "ID copiado",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Volver ao login se non hai usuario
    private void volverLogin() {

        SesionUsuario.cerrarSesion(this);

        Intent intent =
                new Intent(
                        PerfilActivity.this,
                        LoginActivity.class
                );

        startActivity(intent);
        finish();
    }

    // Bottom Navigation
    private void configurarBottomNavigation() {

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_inicio) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            if (id == R.id.nav_mascotas) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            if (id == R.id.nav_eventos) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                EventosActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            if (id == R.id.nav_saude) {

                Intent intent =
                        new Intent(
                                PerfilActivity.this,
                                SaudeActivity.class
                        );

                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

                return true;
            }

            if (id == R.id.nav_perfil) {

                return true;
            }

            return false;
        });

        bottomNavigation.setSelectedItemId(R.id.nav_perfil);
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