package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcare.backup.BackupRestoreManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class BackupsActivity extends AppCompatActivity {

    private LinearLayout contenedorBackups;
    private TextView txtSinBackups;
    private MaterialButton btnRestaurarBackup;
    private MaterialButton btnVolverPerfil;

    private File backupSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_backups);

        // Referencias XML
        contenedorBackups = findViewById(R.id.contenedorBackups);
        txtSinBackups = findViewById(R.id.txtSinBackups);
        btnRestaurarBackup = findViewById(R.id.btnRestaurarBackup);
        btnVolverPerfil = findViewById(R.id.btnVolverPerfil);

        // Cargar copias locais
        cargarBackupsLocales();

        // Restaurar copia seleccionada
        btnRestaurarBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                confirmarRestauracion();
            }
        });

        // Volver ao perfil
        btnVolverPerfil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                volverPerfil();
            }
        });
    }

    // Cargar arquivos de backup locais
    private void cargarBackupsLocales() {

        contenedorBackups.removeAllViews();
        backupSeleccionado = null;

        File carpeta =
                new File(
                        getExternalFilesDir(null),
                        "backups"
                );

        if (!carpeta.exists()) {

            txtSinBackups.setVisibility(View.VISIBLE);
            btnRestaurarBackup.setEnabled(false);
            return;
        }

        File[] archivos =
                carpeta.listFiles();

        if (archivos == null || archivos.length == 0) {

            txtSinBackups.setVisibility(View.VISIBLE);
            btnRestaurarBackup.setEnabled(false);
            return;
        }

        // Ordenar do máis novo ao máis antigo
        Arrays.sort(
                archivos,
                (archivo1, archivo2) ->
                        Long.compare(
                                archivo2.lastModified(),
                                archivo1.lastModified()
                        )
        );

        txtSinBackups.setVisibility(View.GONE);
        btnRestaurarBackup.setEnabled(false);

        for (File archivo : archivos) {

            if (archivo.isFile()
                    && archivo.getName().endsWith(".json")) {

                crearVistaBackup(archivo);
            }
        }
    }

    // Crear tarxeta visual dunha copia de seguridade
    private void crearVistaBackup(File archivo) {

        MaterialCardView card =
                new MaterialCardView(this);

        LinearLayout.LayoutParams paramsCard =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        paramsCard.setMargins(0, 0, 0, 18);

        card.setLayoutParams(paramsCard);
        card.setCardBackgroundColor(0xFFFFFFFF);
        card.setRadius(24);
        card.setCardElevation(2);
        card.setStrokeWidth(1);
        card.setStrokeColor(0xFFE1D8F0);
        card.setUseCompatPadding(true);

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(22, 18, 22, 18);

        RadioButton radioButton =
                new RadioButton(this);

        radioButton.setClickable(false);

        LinearLayout textos =
                new LinearLayout(this);

        textos.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams paramsTextos =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        textos.setLayoutParams(paramsTextos);

        TextView txtNombre =
                new TextView(this);

        txtNombre.setText(archivo.getName());
        txtNombre.setTextColor(0xFF222222);
        txtNombre.setTextSize(16);
        txtNombre.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView txtFecha =
                new TextView(this);

        txtFecha.setText(
                "Creada: "
                        + formatearFecha(archivo.lastModified())
        );

        txtFecha.setTextColor(0xFF777777);
        txtFecha.setTextSize(14);
        txtFecha.setPadding(0, 6, 0, 0);

        textos.addView(txtNombre);
        textos.addView(txtFecha);

        layout.addView(radioButton);
        layout.addView(textos);

        card.addView(layout);

        // Seleccionar copia ao tocar na tarxeta
        card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                seleccionarBackup(
                        archivo
                );
            }
        });

        contenedorBackups.addView(card);
    }

    // Gardar copia seleccionada e actualizar a lista
    private void seleccionarBackup(File archivo) {

        backupSeleccionado =
                archivo;

        btnRestaurarBackup.setEnabled(true);

        // Recargar a lista para actualizar o radio seleccionado
        pintarSeleccion();
    }

    // Actualizar visualmente a copia seleccionada
    private void pintarSeleccion() {

        int cantidad =
                contenedorBackups.getChildCount();

        for (int i = 0; i < cantidad; i++) {

            View vista =
                    contenedorBackups.getChildAt(i);

            if (vista instanceof MaterialCardView) {

                MaterialCardView card =
                        (MaterialCardView) vista;

                LinearLayout layout =
                        (LinearLayout) card.getChildAt(0);

                RadioButton radioButton =
                        (RadioButton) layout.getChildAt(0);

                LinearLayout textos =
                        (LinearLayout) layout.getChildAt(1);

                TextView txtNombre =
                        (TextView) textos.getChildAt(0);

                boolean seleccionado =
                        backupSeleccionado != null
                                && txtNombre.getText().toString().equals(
                                backupSeleccionado.getName()
                        );

                radioButton.setChecked(seleccionado);

                if (seleccionado) {

                    card.setStrokeColor(0xFF8F61FF);
                    card.setStrokeWidth(3);

                } else {

                    card.setStrokeColor(0xFFE1D8F0);
                    card.setStrokeWidth(1);
                }
            }
        }
    }

    // Confirmar antes de restaurar
    private void confirmarRestauracion() {

        if (backupSeleccionado == null) {

            Toast.makeText(
                    this,
                    "Selecciona unha copia de seguridade",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Restaurar copia");

        builder.setMessage(
                "Estás seguro de restaurar o sistema?\n\n"
                        + "Borrarás todos os datos gardados actualmente."
        );

        builder.setNegativeButton(
                "Cancelar",
                null
        );

        builder.setPositiveButton(
                "Restaurar",
                (dialogInterface, i) -> restaurarBackup()
        );

        builder.show();
    }

    // Restaurar a copia seleccionada
    private void restaurarBackup() {

        BackupRestoreManager.restaurarDesdeArchivo(
                this,
                backupSeleccionado
        );

        Toast.makeText(
                this,
                "Restauración iniciada",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        BackupsActivity.this,
                        HomeActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        );

        startActivity(intent);
        finish();
    }

    // Volver á pantalla de perfil
    private void volverPerfil() {

        Intent intent =
                new Intent(
                        BackupsActivity.this,
                        PerfilActivity.class
                );

        startActivity(intent);
        finish();
    }

    // Formatear data do arquivo
    private String formatearFecha(long fechaMillis) {

        SimpleDateFormat formato =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                );

        return formato.format(
                new Date(fechaMillis)
        );
    }
}