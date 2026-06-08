package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "pesos")
public class PesoDTO implements Serializable {

    // Clave primaria local Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Relación coa mascota
    private int mascotaId;

    // Datos do peso
    private double peso;
    private String fecha;
    private String hora;

    // Datos para Supabase
    private String supabaseId;
    private String usuarioId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    // Constructor baleiro necesario para Room
    public PesoDTO() {
    }

    // Constructor para crear rexistros de peso
    public PesoDTO(
            int mascotaId,
            double peso,
            String fecha,
            String hora
    ) {

        this.mascotaId = mascotaId;
        this.peso = peso;
        this.fecha = fecha;
        this.hora = hora;

        supabaseId = "";
        usuarioId = "";
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {

        this.peso = peso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {

        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {

        this.hora = hora;
    }

    public String getSupabaseId() {
        return supabaseId;
    }

    public void setSupabaseId(String supabaseId) {

        this.supabaseId = supabaseId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {

        this.usuarioId = usuarioId;
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