package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.MascotaAdapter;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dto.MascotaDTO;

public class ListadoMascotasActivity extends AppCompatActivity {

    RecyclerView rv;
    MascotaAdapter miAdaptador;
    MascotaDAO dao;

    Button btnAddMascota;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Intent data = result.getData();

                    MascotaDTO mascota =
                            (MascotaDTO) data.getSerializableExtra("mascota");

                    boolean editar = data.getBooleanExtra("editar", false);
                    int posicion = data.getIntExtra("posicion", -1);

                    if (editar && posicion != -1) {
                        dao.actualizar(posicion, mascota);
                    } else {
                        dao.insertar(mascota);
                    }

                    miAdaptador.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado_mascotas);

        dao = new MascotaDAO();

        rv = findViewById(R.id.rvMascotas);
        btnAddMascota = findViewById(R.id.btnAddMascota);

        miAdaptador = new MascotaAdapter(dao.listar());

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(miAdaptador);

        btnAddMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(
                                ListadoMascotasActivity.this,
                                AddMascotaActivity.class
                        );

                launcher.launch(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (miAdaptador != null) {
            miAdaptador.notifyDataSetChanged();
        }
    }
}