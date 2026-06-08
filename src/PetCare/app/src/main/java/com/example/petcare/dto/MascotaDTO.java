package com.example.petcare.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mascotas")
public class MascotaDTO implements Serializable {

    // Clave primaria local Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Datos para Supabase
    private String supabaseId;
    private String propietarioId;
    private long fechaCreacion;
    private long fechaActualizacion;
    private boolean pendienteSync;

    // Datos básicos
    private String nombre;
    private String especie;
    private String raza;
    private int edad;

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

        pesoActual = 0;
        fechaNacimiento = "";
        sexo = "";

        // Datos iniciais Supabase
        supabaseId = "";
        propietarioId = "";
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

    public String getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(String propietarioId) {

        this.propietarioId = propietarioId;
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