package com.example.petcare.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcare.dto.UsuarioDTO;

import java.util.List;

@Dao
public interface UsuarioDAO {

    // CREATE
    @Insert
    long insertar(UsuarioDTO usuario);

    // READ
    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    List<UsuarioDTO> listar();

    // READ por id
    @Query("SELECT * FROM usuarios WHERE id = :id")
    UsuarioDTO obtenerPorId(int id);

    // READ por email
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    UsuarioDTO obtenerPorEmail(String email);

    // READ por supabaseId
    @Query("SELECT * FROM usuarios WHERE supabaseId = :supabaseId LIMIT 1")
    UsuarioDTO obtenerPorSupabaseId(String supabaseId);

    // READ pendientes de sincronizar
    @Query("SELECT * FROM usuarios WHERE pendienteSync = 1")
    List<UsuarioDTO> listarPendientesSync();

    // UPDATE
    @Update
    void actualizar(UsuarioDTO usuario);

    // DELETE
    @Delete
    void borrar(UsuarioDTO usuario);

    // DELETE todos os usuarios
    @Query("DELETE FROM usuarios")
    void borrarTodo();
}