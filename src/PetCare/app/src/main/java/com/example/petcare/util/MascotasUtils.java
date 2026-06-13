package com.example.petcare.util;

import android.content.Context;

import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;

import java.util.ArrayList;

public class MascotasUtils {

    // Obtener mascotas propias y compartidas
    public static ArrayList<MascotaDTO> obtenerMascotasDisponibles(
            Context context
    ) {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        MascotaDAO mascotaDAO =
                db.mascotaDAO();

        UsuarioMascotaDAO usuarioMascotaDAO =
                db.usuarioMascotaDAO();

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(context);

        ArrayList<MascotaDTO> mascotas =
                new ArrayList<>();

        // Mascotas propias
        mascotas.addAll(
                mascotaDAO.listarPorPropietario(
                        usuarioActualId
                )
        );

        // Mascotas compartidas aceptadas
        ArrayList<UsuarioMascotaDTO> relaciones =
                new ArrayList<>(
                        usuarioMascotaDAO
                                .listarAceptadasPorUsuario(
                                        usuarioActualId
                                )
                );

        for (UsuarioMascotaDTO relacion : relaciones) {

            MascotaDTO mascota =
                    mascotaDAO.obtenerPorId(
                            relacion.getMascotaId()
                    );

            if (mascota != null) {

                boolean existe = false;

                for (MascotaDTO mascotaLista : mascotas) {

                    if (mascotaLista.getId()
                            == mascota.getId()) {

                        existe = true;
                        break;
                    }
                }

                if (!existe) {

                    mascotas.add(mascota);
                }
            }
        }

        return mascotas;
    }
}