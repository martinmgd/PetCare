package com.example.petcare.dao;

import com.example.petcare.data.Datos;
import com.example.petcare.dto.MascotaDTO;

import java.util.ArrayList;

public class MascotaDAO {

    private ArrayList<MascotaDTO> mascotas;

    public MascotaDAO() {

        mascotas = Datos.getInstance().getMascotas();
    }

    // CREATE
    public void insertar(MascotaDTO mascota) {

        mascotas.add(mascota);
    }

    // READ
    public ArrayList<MascotaDTO> listar() {

        return mascotas;
    }

    // READ individual
    public MascotaDTO obtener(int posicion) {

        return mascotas.get(posicion);
    }

    // UPDATE
    public void actualizar(
            int posicion,
            MascotaDTO mascota
    ) {

        mascotas.set(posicion, mascota);
    }

    // DELETE
    public void borrar(int posicion) {

        mascotas.remove(posicion);
    }
}