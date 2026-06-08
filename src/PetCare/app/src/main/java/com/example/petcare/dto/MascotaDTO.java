package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mascotas")
public class MascotaDTO implements Serializable {

    // Clave primaria
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Datos básicos
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private boolean favorito;

    // Datos de saúde
    private double pesoActual;
    private String fechaNacimiento;
    private String sexo;

    // Constructor baleiro necesario para Room
    public MascotaDTO() {
    }

    // Constructor para crear mascotas
    public MascotaDTO(String nombre, String especie, String raza, int edad) {

        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;

        favorito = false;

        // Valores iniciales
        pesoActual = 0;
        fechaNacimiento = "";
        sexo = "";
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {

        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {

        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {

        this.edad = edad;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {

        this.favorito = favorito;
    }

    public double getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(double pesoActual) {

        this.pesoActual = pesoActual;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {

        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {

        this.sexo = sexo;
    }
}