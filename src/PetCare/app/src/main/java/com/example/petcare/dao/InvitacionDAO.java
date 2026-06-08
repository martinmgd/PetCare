package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.InvitacionDTO;

import java.util.List;

@Dao
public interface InvitacionDAO {

    // CREATE
    @Insert
    long insertar(InvitacionDTO invitacion);

    // READ
    @Query("SELECT * FROM invitaciones ORDER BY id DESC")
    List<InvitacionDTO> listar();

    // READ por usuario destino
    @Query("SELECT * FROM invitaciones WHERE usuarioDestinoId = :usuarioDestinoId ORDER BY id DESC")
    List<InvitacionDTO> listarPorUsuarioDestino(String usuarioDestinoId);

    // READ pendientes por usuario destino
    @Query("SELECT * FROM invitaciones WHERE usuarioDestinoId = :usuarioDestinoId AND estado = 'PENDIENTE' ORDER BY id DESC")
    List<InvitacionDTO> listarPendientesPorUsuario(String usuarioDestinoId);

    // READ por mascota
    @Query("SELECT * FROM invitaciones WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<InvitacionDTO> listarPorMascota(int mascotaId);

    // READ pendientes de sincronizar
    @Query("SELECT * FROM invitaciones WHERE pendienteSync = 1")
    List<InvitacionDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(InvitacionDTO invitacion);

    // DELETE
    @Delete
    void borrar(InvitacionDTO invitacion);

    // DELETE por id
    @Query("DELETE FROM invitaciones WHERE id = :id")
    void borrarPorId(int id);
}