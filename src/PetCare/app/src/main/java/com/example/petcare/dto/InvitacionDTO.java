package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "invitaciones")
public class InvitacionDTO implements Serializable {

    // Clave primaria local Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Relación coa mascota
    private int mascotaId;
    private String nombreMascota;

    // Usuario que envía a invitación
    private String usuarioOrigenId;

    // Usuario que recibe a invitación
    private String usuarioDestinoId;

    // Estado da invitación
    private String estado;

    // Datos para Supabase
    private String supabaseId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    // Constructor baleiro necesario para Room
    public InvitacionDTO() {
    }

    // Constructor para crear invitacións
    public InvitacionDTO(
            int mascotaId,
            String nombreMascota,
            String usuarioOrigenId,
            String usuarioDestinoId
    ) {

        this.mascotaId = mascotaId;
        this.nombreMascota = nombreMascota;
        this.usuarioOrigenId = usuarioOrigenId;
        this.usuarioDestinoId = usuarioDestinoId;

        estado = "PENDIENTE";

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

    public int getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(int mascotaId) {

        this.mascotaId = mascotaId;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {

        this.nombreMascota = nombreMascota;
    }

    public String getUsuarioOrigenId() {
        return usuarioOrigenId;
    }

    public void setUsuarioOrigenId(String usuarioOrigenId) {

        this.usuarioOrigenId = usuarioOrigenId;
    }

    public String getUsuarioDestinoId() {
        return usuarioDestinoId;
    }

    public void setUsuarioDestinoId(String usuarioDestinoId) {

        this.usuarioDestinoId = usuarioDestinoId;
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