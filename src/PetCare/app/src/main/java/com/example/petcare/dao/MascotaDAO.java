package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.MascotaDTO;

import java.util.List;

@Dao
public interface MascotaDAO {

    // CREATE
    @Insert
    long insertar(MascotaDTO mascota);

    // READ
    @Query("SELECT * FROM mascotas ORDER BY id DESC")
    List<MascotaDTO> listar();

    // READ por propietario
    @Query("SELECT * FROM mascotas WHERE propietarioId = :propietarioId ORDER BY id DESC")
    List<MascotaDTO> listarPorPropietario(String propietarioId);

    // READ individual
    @Query("SELECT * FROM mascotas WHERE id = :id")
    MascotaDTO obtenerPorId(int id);

    // READ pendientes de sincronizar
    @Query("SELECT * FROM mascotas WHERE pendienteSync = 1")
    List<MascotaDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(MascotaDTO mascota);

    // DELETE
    @Delete
    void borrar(MascotaDTO mascota);

    // DELETE por id
    @Query("DELETE FROM mascotas WHERE id = :id")
    void borrarPorId(int id);
}