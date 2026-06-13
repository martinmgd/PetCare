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

    // READ por usuario creador
    @Query("SELECT * FROM eventos WHERE usuarioId = :usuarioId ORDER BY id DESC")
    List<EventoDTO> listarPorUsuario(String usuarioId);

    // READ por mascota
    @Query("SELECT * FROM eventos WHERE mascotaId = :mascotaId ORDER BY fecha ASC, hora ASC")
    List<EventoDTO> listarPorMascota(int mascotaId);

    // READ por varias mascotas
    @Query("SELECT * FROM eventos WHERE mascotaId IN (:mascotasIds) ORDER BY fecha ASC, hora ASC")
    List<EventoDTO> listarPorMascotas(List<Integer> mascotasIds);

    // READ individual
    @Query("SELECT * FROM eventos WHERE id = :id")
    EventoDTO obtenerPorId(int id);

    // READ por fecha
    @Query("SELECT * FROM eventos WHERE fecha = :fecha ORDER BY hora ASC")
    List<EventoDTO> listarPorFecha(String fecha);

    // READ por fecha e mascotas permitidas
    @Query("SELECT * FROM eventos WHERE fecha = :fecha AND mascotaId IN (:mascotasIds) ORDER BY hora ASC")
    List<EventoDTO> listarPorFechaYMascotas(
            String fecha,
            List<Integer> mascotasIds);

    // READ pendientes de sincronizar
    @Query("SELECT * FROM eventos WHERE pendienteSync = 1")
    List<EventoDTO> listarPendientesSync();

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