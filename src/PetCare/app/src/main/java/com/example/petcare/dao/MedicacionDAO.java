package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.MedicacionDTO;

import java.util.List;

@Dao
public interface MedicacionDAO {

    // CREATE
    @Insert
    long insertar(MedicacionDTO medicacion);

    // READ
    @Query("SELECT * FROM medicaciones ORDER BY id DESC")
    List<MedicacionDTO> listar();

    // READ por mascota
    @Query("SELECT * FROM medicaciones WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<MedicacionDTO> listarPorMascota(int mascotaId);

    // READ por usuario creador
    @Query("SELECT * FROM medicaciones WHERE usuarioId = :usuarioId ORDER BY id DESC")
    List<MedicacionDTO> listarPorUsuario(String usuarioId);

    // READ pendientes de sincronizar
    @Query("SELECT * FROM medicaciones WHERE pendienteSync = 1")
    List<MedicacionDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(MedicacionDTO medicacion);

    // DELETE
    @Delete
    void borrar(MedicacionDTO medicacion);

    // DELETE por id
    @Query("DELETE FROM medicaciones WHERE id = :id")
    void borrarPorId(int id);

    // DELETE todas as medicacións
    @Query("DELETE FROM medicaciones")
    void borrarTodo();
}