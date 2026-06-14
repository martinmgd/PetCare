package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.VacinaDTO;

import java.util.List;

@Dao
public interface VacinaDAO {

    @Insert
    long insertar(VacinaDTO vacina);

    @Query("SELECT * FROM vacinas ORDER BY id DESC")
    List<VacinaDTO> listar();

    @Query("SELECT * FROM vacinas WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<VacinaDTO> listarPorMascota(int mascotaId);

    @Query("SELECT * FROM vacinas WHERE pendienteSync = 1")
    List<VacinaDTO> listarPendientesSync();

    @Update
    void actualizar(VacinaDTO vacina);

    @Delete
    void borrar(VacinaDTO vacina);

    @Query("DELETE FROM vacinas WHERE id = :id")
    void borrarPorId(int id);

    // DELETE todas as vacinas
    @Query("DELETE FROM vacinas")
    void borrarTodo();
}