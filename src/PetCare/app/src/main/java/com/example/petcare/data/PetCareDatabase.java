package com.example.petcare.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;

@Database(
        entities = {
                MascotaDTO.class,
                EventoDTO.class
        },
        version = 1,
        exportSchema = false
)
public abstract class PetCareDatabase extends RoomDatabase {

    // Instancia única da base de datos
    private static PetCareDatabase instancia;

    // DAO de mascotas
    public abstract MascotaDAO mascotaDAO();

    // DAO de eventos
    public abstract EventoDAO eventoDAO();

    // Obtener instancia da base de datos
    public static synchronized PetCareDatabase getInstance(Context context) {

        if (instancia == null) {

            instancia =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PetCareDatabase.class,
                                    "petcare_database"
                            )

                            // Permite consultas no fío principal
                            .allowMainThreadQueries()

                            .build();
        }

        return instancia;
    }
}