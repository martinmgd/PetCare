package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "usuarios_mascotas")
public class UsuarioMascotaDTO implements Serializable {

    // Clave primaria local Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Relación usuario-mascota
    private String usuarioId;
    private int mascotaId;

    // Permisos
    private String rol;
    private String estado;

    // Datos para Supabase
    private String supabaseId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    // Constructor baleiro necesario para Room
    public UsuarioMascotaDTO() {
    }

    // Constructor para crear relación
    public UsuarioMascotaDTO(
            String usuarioId,
            int mascotaId,
            String rol,
            String estado
    ) {

        this.usuarioId = usuarioId;
        this.mascotaId = mascotaId;
        this.rol = rol;
        this.estado = estado;

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

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {

        this.usuarioId = usuarioId;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(int mascotaId) {

        this.mascotaId = mascotaId;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {

        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {

        this.estado = estado;
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
}