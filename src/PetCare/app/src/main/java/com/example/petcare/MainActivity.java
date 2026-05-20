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

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    MascotaAdapter miAdaptador;
    MascotaDAO dao;

    Button btnAddMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dao = new MascotaDAO();

        // Referencias
        rv = findViewById(R.id.rvMascotas);
        btnAddMascota = findViewById(R.id.btnAddMascota);

        // Adaptador
        miAdaptador = new MascotaAdapter(dao.listar());

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(miAdaptador);

        // Abrir formulario
        btnAddMascota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddMascotaActivity.class);

                launcher.launch(intent);
            }
        });
    }

    // Recibir mascota
    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == RESULT_OK){

                    Intent data = result.getData();

                    MascotaDTO mascota = (MascotaDTO) data.getSerializableExtra("mascota");

                    boolean editar = data.getBooleanExtra("editar", false);
                    int posicion = data.getIntExtra("posicion", -1);

                    if(editar && posicion != -1){

                        // Actualizar mascota
                        dao.actualizar(posicion, mascota);

                    }else{

                        // Añadir mascota
                        dao.insertar(mascota);

                    }

                    // Actualizar lista
                    miAdaptador.notifyDataSetChanged();
                }
            });
    @Override
    protected void onResume() {
        super.onResume();

        if(miAdaptador != null){
            miAdaptador.notifyDataSetChanged();
        }
    }
}