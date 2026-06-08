package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "usuarios")
public class UsuarioDTO implements Serializable {

    // Clave primaria local Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Datos para Supabase
    private String supabaseId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    // Datos do usuario
    private String nombre;
    private String email;
    private String foto;

    // Constructor baleiro necesario para Room
    public UsuarioDTO() {
    }

    // Constructor para crear usuarios
    public UsuarioDTO(
            String nombre,
            String email
    ) {

        this.nombre = nombre;
        this.email = email;
        this.foto = "";

        supabaseId = "";
        fechaCreacion = System.currentTimeMillis();
        fechaActualizacion = System.currentTimeMillis();
        pendienteSync = true;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getSupabaseId() {
        return supabaseId;
    }

    public void setSupabaseId(String supabaseId) {

        this.supabaseId = supabaseId;
    }

    public long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(long fechaCreacion) {

        this.fechaCreacion = fechaCreacion;
    }

    public long getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(long fechaActualizacion) {

        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean isPendienteSync() {
        return pendienteSync;
    }

    public void setPendienteSync(boolean pendienteSync) {

        this.pendienteSync = pendienteSync;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {

        this.foto = foto;
    }
}