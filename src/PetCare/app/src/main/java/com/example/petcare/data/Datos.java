package com.example.petcare.data;

import com.example.petcare.dto.MascotaDTO;

import java.util.ArrayList;

public class Datos {

    private static Datos datos;

    private ArrayList<MascotaDTO> mascotas;

    private Datos() {

        mascotas = new ArrayList<>();

    }

    public static Datos getInstance(){

        if(datos == null){

            datos = new Datos();

        }

        return datos;
    }

    public ArrayList<MascotaDTO> getMascotas(){

        return mascotas;
    }
}