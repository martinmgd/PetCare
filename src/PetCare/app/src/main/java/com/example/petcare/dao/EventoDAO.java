package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.EventoDTO;

import java.util.List;

@Dao
public interface EventoDAO {

    // CREATE
    @Insert
    long insertar(EventoDTO evento);

    // READ
    @Query("SELECT * FROM eventos ORDER BY id DESC")
    List<EventoDTO> listar();

    // READ individual
    @Query("SELECT * FROM eventos WHERE id = :id")
    EventoDTO obtenerPorId(int id);

    // READ por fecha
    @Query("SELECT * FROM eventos WHERE fecha = :fecha ORDER BY hora ASC")
    List<EventoDTO> listarPorFecha(String fecha);

    // UPDATE
    @Update
    void actualizar(EventoDTO evento);

    // DELETE
    @Delete
    void borrar(EventoDTO evento);

    // DELETE por id
    @Query("DELETE FROM eventos WHERE id = :id")
    void borrarPorId(int id);
}