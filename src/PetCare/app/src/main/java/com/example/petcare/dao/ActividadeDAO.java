package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.ActividadeDTO;

import java.util.List;

@Dao
public interface ActividadeDAO {

    @Insert
    long insertar(ActividadeDTO actividade);

    @Query("SELECT * FROM actividades ORDER BY id DESC")
    List<ActividadeDTO> listar();

    @Query("SELECT * FROM actividades WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<ActividadeDTO> listarPorMascota(int mascotaId);

    @Query("SELECT * FROM actividades WHERE pendienteSync = 1")
    List<ActividadeDTO> listarPendientesSync();

    @Update
    void actualizar(ActividadeDTO actividade);

    @Delete
    void borrar(ActividadeDTO actividade);

    @Query("DELETE FROM actividades WHERE id = :id")
    void borrarPorId(int id);
}