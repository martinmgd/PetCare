package com.example.petcare.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.InvitacionDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.PesoDAO;
import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.InvitacionDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.PesoDTO;
import com.example.petcare.dto.UsuarioDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;

@Database(
        entities = {
                MascotaDTO.class,
                EventoDTO.class,
                PesoDTO.class,
                UsuarioDTO.class,
                UsuarioMascotaDTO.class,
                InvitacionDTO.class
        },
        version = 5,
        exportSchema = false
)
public abstract class PetCareDatabase extends RoomDatabase {

    private static PetCareDatabase instancia;

    // DAO mascotas
    public abstract MascotaDAO mascotaDAO();

    // DAO eventos
    public abstract EventoDAO eventoDAO();

    // DAO pesos
    public abstract PesoDAO pesoDAO();

    // DAO usuarios
    public abstract UsuarioDAO usuarioDAO();

    // DAO relación usuarios-mascotas
    public abstract UsuarioMascotaDAO usuarioMascotaDAO();

    // DAO invitaciones
    public abstract InvitacionDAO invitacionDAO();

    // Instancia única da base de datos
    public static synchronized PetCareDatabase getInstance(Context context) {

        if (instancia == null) {

            instancia =
                    Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PetCareDatabase.class,
                                    "petcare_db"
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }

        return instancia;
    }
}