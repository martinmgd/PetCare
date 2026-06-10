package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.UsuarioMascotaDTO;

import java.util.List;

@Dao
public interface UsuarioMascotaDAO {

    // CREATE
    @Insert
    long insertar(UsuarioMascotaDTO usuarioMascota);

    // READ
    @Query("SELECT * FROM usuarios_mascotas ORDER BY id DESC")
    List<UsuarioMascotaDTO> listar();

    // READ por mascota
    @Query("SELECT * FROM usuarios_mascotas WHERE mascotaId = :mascotaId ORDER BY id DESC")
    List<UsuarioMascotaDTO> listarPorMascota(int mascotaId);

    // READ por usuario
    @Query("SELECT * FROM usuarios_mascotas WHERE usuarioId = :usuarioId ORDER BY id DESC")
    List<UsuarioMascotaDTO> listarPorUsuario(String usuarioId);

    // READ mascotas compartidas aceptadas dun usuario
    @Query("SELECT * FROM usuarios_mascotas WHERE usuarioId = :usuarioId AND estado = 'ACEPTADA' ORDER BY id DESC")
    List<UsuarioMascotaDTO> listarAceptadasPorUsuario(String usuarioId);

    // READ relación concreta
    @Query("SELECT * FROM usuarios_mascotas WHERE usuarioId = :usuarioId AND mascotaId = :mascotaId LIMIT 1")
    UsuarioMascotaDTO obtenerRelacion(
            String usuarioId,
            int mascotaId
    );

    // READ pendientes de sincronizar
    @Query("SELECT * FROM usuarios_mascotas WHERE pendienteSync = 1")
    List<UsuarioMascotaDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(UsuarioMascotaDTO usuarioMascota);

    // DELETE
    @Delete
    void borrar(UsuarioMascotaDTO usuarioMascota);

    // DELETE por id
    @Query("DELETE FROM usuarios_mascotas WHERE id = :id")
    void borrarPorId(int id);
}