package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "comidas")
public class ComidaDTO implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int mascotaId;
    private String fecha;

    private boolean almorzoComido;
    private boolean comidaComida;
    private boolean ceaComida;

    private String supabaseId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    public ComidaDTO() {
    }

    public ComidaDTO(int mascotaId, String fecha) {

        this.mascotaId = mascotaId;
        this.fecha = fecha;

        almorzoComido = false;
        comidaComida = false;
        ceaComida = false;

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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {

        this.fecha = fecha;
    }

    public boolean isAlmorzoComido() {
        return almorzoComido;
    }

    public void setAlmorzoComido(boolean almorzoComido) {

        this.almorzoComido = almorzoComido;
    }

    public boolean isComidaComida() {
        return comidaComida;
    }

    public void setComidaComida(boolean comidaComida) {

        this.comidaComida = comidaComida;
    }

    public boolean isCeaComida() {
        return ceaComida;
    }

    public void setCeaComida(boolean ceaComida) {

        this.ceaComida = ceaComida;
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