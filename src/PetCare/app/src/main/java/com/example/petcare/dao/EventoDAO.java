package com.example.petcare.dao;

import com.example.petcare.dto.EventoDTO;

import java.util.ArrayList;

public class EventoDAO {

    private static final ArrayList<EventoDTO> listaEventos =
            new ArrayList<>();

    // Insertar evento
    public void insertar(EventoDTO evento) {

        listaEventos.add(evento);
    }

    // Listar eventos
    public ArrayList<EventoDTO> listar() {

        return listaEventos;
    }

    // Obtener evento
    public EventoDTO obter(int posicion) {

        return listaEventos.get(posicion);
    }

    // Eliminar evento
    public void eliminar(int posicion) {

        listaEventos.remove(posicion);
    }

    // Actualizar evento
    public void actualizar(int posicion, EventoDTO evento) {

        listaEventos.set(posicion, evento);
    }

    // Buscar posición dun evento dentro da lista principal
    public int buscarPosicion(EventoDTO evento) {

        return listaEventos.indexOf(evento);
    }
}