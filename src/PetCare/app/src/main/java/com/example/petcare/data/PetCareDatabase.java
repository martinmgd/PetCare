package com.example.petcare.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.petcare.dao.ComidaDAO;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.InvitacionDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.PesoDAO;
import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.dto.ComidaDTO;
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
                InvitacionDTO.class,
                ComidaDTO.class
        },
        version = 7,
        exportSchema = false
)
public abstract class PetCareDatabase extends RoomDatabase {

    private static PetCareDatabase instancia;

    public abstract MascotaDAO mascotaDAO();

    public abstract EventoDAO eventoDAO();

    public abstract PesoDAO pesoDAO();

    public abstract UsuarioDAO usuarioDAO();

    public abstract UsuarioMascotaDAO usuarioMascotaDAO();

    public abstract InvitacionDAO invitacionDAO();

    public abstract ComidaDAO comidaDAO();

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