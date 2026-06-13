package com.example.petcare;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.InvitacionDAO;
import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.InvitacionDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.UsuarioDTO;
import com.example.petcare.supabase.SupabaseSyncManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class GestionUsuariosMascotaActivity extends AppCompatActivity {

    private Toolbar toolbarGestionUsuarios;

    private TextView txtNombreMascotaGestion;
    private TextView txtInfoGestionUsuarios;

    private TextInputEditText txtUsuarioIdInvitar;

    private MaterialButton btnEnviarInvitacion;

    private MascotaDTO mascota;

    private InvitacionDAO invitacionDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gestion_usuarios_mascota);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        invitacionDAO = db.invitacionDAO();
        usuarioDAO = db.usuarioDAO();

        // Referencias XML
        toolbarGestionUsuarios = findViewById(R.id.toolbarGestionUsuarios);
        txtNombreMascotaGestion = findViewById(R.id.txtNombreMascotaGestion);
        txtInfoGestionUsuarios = findViewById(R.id.txtInfoGestionUsuarios);
        txtUsuarioIdInvitar = findViewById(R.id.txtUsuarioIdInvitar);
        btnEnviarInvitacion = findViewById(R.id.btnEnviarInvitacion);

        // Recibir mascota
        mascota =
                (MascotaDTO)
                        getIntent().getSerializableExtra("mascota");

        // Toolbar
        toolbarGestionUsuarios.setNavigationOnClickListener(v -> finish());

        if (mascota != null) {

            txtNombreMascotaGestion.setText(mascota.getNombre());

        } else {

            txtNombreMascotaGestion.setText("Mascota");
        }

        txtInfoGestionUsuarios.setText(
                "Introduce o ID do usuario ao que queres convidar."
        );

        // Enviar invitación
        btnEnviarInvitacion.setOnClickListener(v -> enviarInvitacion());
    }

    // Enviar invitación a outro usuario
    private void enviarInvitacion() {

        if (mascota == null) {

            Toast.makeText(
                    this,
                    "Non se puido cargar a mascota",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String usuarioDestinoId;

        if (txtUsuarioIdInvitar.getText() != null) {

            usuarioDestinoId =
                    txtUsuarioIdInvitar
                            .getText()
                            .toString()
                            .trim();

        } else {

            usuarioDestinoId = "";
        }

        if (usuarioDestinoId.isEmpty()) {

            txtUsuarioIdInvitar.setError("Introduce o ID do usuario");
            return;
        }

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        if (usuarioActualId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Non hai usuario iniciado",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        UsuarioDTO usuarioActual = null;

        try {

            int idActual =
                    Integer.parseInt(usuarioActualId);

            usuarioActual =
                    usuarioDAO.obtenerPorId(idActual);

        } catch (Exception e) {

            usuarioActual = null;
        }

        if (usuarioActual == null) {

            Toast.makeText(
                    this,
                    "Non se puido cargar o usuario actual",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (usuarioActual.getSupabaseId() == null
                || usuarioActual.getSupabaseId().isEmpty()) {

            Toast.makeText(
                    this,
                    "O teu usuario aínda non está sincronizado",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (usuarioDestinoId.equals(usuarioActual.getSupabaseId())) {

            txtUsuarioIdInvitar.setError("Non podes convidarte a ti mesmo");
            return;
        }

        UsuarioDTO usuarioDestino =
                usuarioDAO.obtenerPorSupabaseId(
                        usuarioDestinoId
                );

        if (usuarioDestino == null) {

            txtUsuarioIdInvitar.setError("Non existe un usuario con ese ID");
            return;
        }

        // Evitar invitacións duplicadas pendentes para a mesma mascota e usuario
        InvitacionDTO invitacionExistente =
                invitacionDAO.obtenerPendientePorMascotaYUsuario(
                        mascota.getId(),
                        usuarioDestinoId
                );

        if (invitacionExistente != null) {

            Toast.makeText(
                    this,
                    "Xa existe unha invitación pendente para este usuario",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        InvitacionDTO invitacion =
                new InvitacionDTO(
                        mascota.getId(),
                        mascota.getNombre(),
                        usuarioActualId,
                        usuarioDestinoId
                );

        invitacionDAO.insertar(invitacion);

        SupabaseSyncManager.sincronizar(this);

        txtUsuarioIdInvitar.setText("");

        Toast.makeText(
                this,
                "Invitación enviada",
                Toast.LENGTH_SHORT
        ).show();
    }
}