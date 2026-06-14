package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.ComidaDTO;

import java.util.List;

@Dao
public interface ComidaDAO {

    @Insert
    long insertar(ComidaDTO comida);

    @Query("SELECT * FROM comidas ORDER BY id DESC")
    List<ComidaDTO> listar();

    @Query("SELECT * FROM comidas WHERE mascotaId = :mascotaId AND fecha = :fecha LIMIT 1")
    ComidaDTO obtenerPorMascotaYFecha(
            int mascotaId,
            String fecha
    );

    @Query("SELECT * FROM comidas WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<ComidaDTO> listarPorMascota(int mascotaId);

    @Query("SELECT * FROM comidas WHERE pendienteSync = 1")
    List<ComidaDTO> listarPendientesSync();

    @Update
    void actualizar(ComidaDTO comida);

    @Delete
    void borrar(ComidaDTO comida);

    @Query("DELETE FROM comidas WHERE id = :id")
    void borrarPorId(int id);

    // DELETE todos os rexistros de comidas
    @Query("DELETE FROM comidas")
    void borrarTodo();
}