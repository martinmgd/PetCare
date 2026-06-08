package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "eventos")
public class EventoDTO implements Serializable {

    // Clave primaria
    @PrimaryKey(autoGenerate = true)
    private int id;

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
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
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