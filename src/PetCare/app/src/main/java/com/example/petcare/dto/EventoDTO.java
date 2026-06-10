package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "eventos")
public class EventoDTO implements Serializable {

    // Clave primaria local Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Datos para Supabase
    private String supabaseId;
    private String usuarioId;
    private int mascotaId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    // Datos do evento
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String tipo;
    private String mascota;

    // Constructor baleiro necesario para Room
    public EventoDTO() {
    }

    // Constructor para crear eventos
    public EventoDTO(
            String titulo,
            String descripcion,
            String fecha,
            String hora,
            String tipo,
            String mascota
    ) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.mascota = mascota;

        // Datos iniciais Supabase
        supabaseId = "";
        usuarioId = "";
        mascotaId = 0;
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

    public String getTitulo() {

        return titulo;
    }

    public void setTitulo(String titulo) {

        this.titulo = titulo;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
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

    public String getTipo() {

        return tipo;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }

    public String getMascota() {

        return mascota;
    }

    public void setMascota(String mascota) {

        this.mascota = mascota;
    }
}