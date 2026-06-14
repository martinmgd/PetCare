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

    // READ invitación pendiente concreta
    @Query("SELECT * FROM invitaciones WHERE mascotaId = :mascotaId AND usuarioDestinoId = :usuarioDestinoId AND estado = 'PENDIENTE' LIMIT 1")
    InvitacionDTO obtenerPendientePorMascotaYUsuario(
            int mascotaId,
            String usuarioDestinoId
    );

    // READ pendientes de sincronizar
    @Query("SELECT * FROM invitaciones WHERE pendienteSync = 1")
    List<InvitacionDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(InvitacionDTO invitacion);

    // UPDATE estado
    @Query("UPDATE invitaciones SET estado = :estado, fechaActualizacion = :fechaActualizacion, pendienteSync = 1 WHERE id = :id")
    void actualizarEstado(
            int id,
            String estado,
            long fechaActualizacion
    );

    // DELETE
    @Delete
    void borrar(InvitacionDTO invitacion);

    // DELETE por id
    @Query("DELETE FROM invitaciones WHERE id = :id")
    void borrarPorId(int id);

    // DELETE todas as invitacións
    @Query("DELETE FROM invitaciones")
    void borrarTodo();
}