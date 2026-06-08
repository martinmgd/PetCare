package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.PesoDTO;

import java.util.List;

@Dao
public interface PesoDAO {

    // CREATE
    @Insert
    long insertar(PesoDTO peso);

    // READ
    @Query("SELECT * FROM pesos ORDER BY id DESC")
    List<PesoDTO> listar();

    // READ por mascota
    @Query("SELECT * FROM pesos WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<PesoDTO> listarPorMascota(int mascotaId);

    // READ por mascota para gráfico
    @Query("SELECT * FROM pesos WHERE mascotaId = :mascotaId ORDER BY id ASC")
    List<PesoDTO> listarPorMascotaAsc(int mascotaId);

    // READ pendientes de sincronizar
    @Query("SELECT * FROM pesos WHERE pendienteSync = 1")
    List<PesoDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(PesoDTO peso);

    // DELETE
    @Delete
    void borrar(PesoDTO peso);
}