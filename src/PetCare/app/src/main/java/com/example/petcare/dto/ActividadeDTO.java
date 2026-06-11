package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "actividades")
public class ActividadeDTO implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int mascotaId;
    private String usuarioId;

    private String tipo;
    private int duracionMinutos;
    private String fecha;
    private String zona;
    private String tiempoResumen;

    private String supabaseId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    public ActividadeDTO() {
    }

    public ActividadeDTO(
            int mascotaId,
            String usuarioId,
            String tipo,
            int duracionMinutos,
            String fecha,
            String zona,
            String tiempoResumen
    ) {

        this.mascotaId = mascotaId;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.duracionMinutos = duracionMinutos;
        this.fecha = fecha;
        this.zona = zona;
        this.tiempoResumen = tiempoResumen;

        supabaseId = "";
        fechaCreacion = System.currentTimeMillis();
        fechaActualizacion = System.currentTimeMillis();
        pendienteSync = true;
    }

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

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {

        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {

        this.duracionMinutos = duracionMinutos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {

        this.fecha = fecha;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {

        this.zona = zona;
    }

    public String getTiempoResumen() {
        return tiempoResumen;
    }

    public void setTiempoResumen(String tiempoResumen) {

        this.tiempoResumen = tiempoResumen;
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