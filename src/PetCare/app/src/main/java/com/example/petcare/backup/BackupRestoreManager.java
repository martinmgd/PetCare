package com.example.petcare.backup;

import android.content.Context;
import android.util.Log;

import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.ActividadeDTO;
import com.example.petcare.dto.ComidaDTO;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.InvitacionDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.MedicacionDTO;
import com.example.petcare.dto.PesoDTO;
import com.example.petcare.dto.UsuarioDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;
import com.example.petcare.dto.VacinaDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class BackupRestoreManager {

    // Restaurar unha copia gardada nun arquivo local
    public static void restaurarDesdeArchivo(
            Context context,
            File archivo
    ) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    // Ler todo o contido do arquivo JSON
                    String contenido =
                            new String(
                                    Files.readAllBytes(archivo.toPath()),
                                    StandardCharsets.UTF_8
                            );

                    // Converter o texto lido nun obxecto JSON
                    JSONObject backup =
                            new JSONObject(contenido);

                    // Restaurar os datos na base local Room
                    restaurarDesdeJson(
                            context,
                            backup
                    );

                    Log.d(
                            "BackupRestore",
                            "Backup restaurado correctamente"
                    );

                } catch (Exception e) {

                    Log.e(
                            "BackupRestore",
                            "Erro restaurando backup: " + e.getMessage()
                    );
                }
            }
        }).start();
    }

    // Restaurar unha copia a partir dun JSONObject
    public static void restaurarDesdeJson(
            Context context,
            JSONObject backup
    ) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        // Facemos todo nunha transacción.
        // Se algo falla, Room evita deixar a base de datos a medias.
        db.runInTransaction(new Runnable() {

            @Override
            public void run() {

                try {

                    // Primeiro borramos as táboas dependentes.
                    // Isto evita problemas coas relacións entre mascotas, eventos, saúde, etc.
                    db.actividadeDAO().borrarTodo();
                    db.medicacionDAO().borrarTodo();
                    db.vacinaDAO().borrarTodo();
                    db.pesoDAO().borrarTodo();
                    db.comidaDAO().borrarTodo();
                    db.eventoDAO().borrarTodo();
                    db.invitacionDAO().borrarTodo();
                    db.usuarioMascotaDAO().borrarTodo();
                    db.mascotaDAO().borrarTodo();
                    db.usuarioDAO().borrarTodo();

                    // Despois restauramos na orde correcta.
                    // Primeiro usuarios e mascotas, despois relacións e datos asociados.
                    restaurarUsuarios(db, backup);
                    restaurarMascotas(db, backup);
                    restaurarUsuariosMascotas(db, backup);
                    restaurarInvitaciones(db, backup);
                    restaurarEventos(db, backup);
                    restaurarComidas(db, backup);
                    restaurarPesos(db, backup);
                    restaurarVacinas(db, backup);
                    restaurarMedicaciones(db, backup);
                    restaurarActividades(db, backup);

                } catch (Exception e) {

                    throw new RuntimeException(e);
                }
            }
        });
    }

    // Restaurar usuarios
    private static void restaurarUsuarios(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("usuarios");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            UsuarioDTO usuario =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            UsuarioDTO.class
                    );

            db.usuarioDAO().insertar(usuario);
        }
    }

    // Restaurar mascotas
    private static void restaurarMascotas(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("mascotas");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            MascotaDTO mascota =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            MascotaDTO.class
                    );

            db.mascotaDAO().insertar(mascota);
        }
    }

    // Restaurar eventos
    private static void restaurarEventos(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("eventos");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            EventoDTO evento =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            EventoDTO.class
                    );

            db.eventoDAO().insertar(evento);
        }
    }

    // Restaurar relacións usuario-mascota
    private static void restaurarUsuariosMascotas(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("usuarios_mascotas");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            UsuarioMascotaDTO relacion =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            UsuarioMascotaDTO.class
                    );

            db.usuarioMascotaDAO().insertar(relacion);
        }
    }

    // Restaurar invitacións
    private static void restaurarInvitaciones(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("invitaciones");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            InvitacionDTO invitacion =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            InvitacionDTO.class
                    );

            db.invitacionDAO().insertar(invitacion);
        }
    }

    // Restaurar comidas
    private static void restaurarComidas(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("comidas");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            ComidaDTO comida =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            ComidaDTO.class
                    );

            db.comidaDAO().insertar(comida);
        }
    }

    // Restaurar pesos
    private static void restaurarPesos(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("pesos");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            PesoDTO peso =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            PesoDTO.class
                    );

            db.pesoDAO().insertar(peso);
        }
    }

    // Restaurar vacinas
    private static void restaurarVacinas(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("vacinas");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            VacinaDTO vacina =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            VacinaDTO.class
                    );

            db.vacinaDAO().insertar(vacina);
        }
    }

    // Restaurar medicacións
    private static void restaurarMedicaciones(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("medicaciones");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            MedicacionDTO medicacion =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            MedicacionDTO.class
                    );

            db.medicacionDAO().insertar(medicacion);
        }
    }

    // Restaurar actividades
    private static void restaurarActividades(
            PetCareDatabase db,
            JSONObject backup
    ) throws Exception {

        JSONArray array =
                backup.optJSONArray("actividades");

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {

            ActividadeDTO actividade =
                    jsonAObjeto(
                            array.getJSONObject(i),
                            ActividadeDTO.class
                    );

            db.actividadeDAO().insertar(actividade);
        }
    }

    // Converter un JSONObject nun DTO usando os campos da clase
    private static <T> T jsonAObjeto(
            JSONObject json,
            Class<T> clase
    ) throws Exception {

        // Crear un obxecto baleiro da clase indicada
        T objeto =
                clase.newInstance();

        Field[] campos =
                clase.getDeclaredFields();

        for (Field campo : campos) {

            campo.setAccessible(true);

            String nombreCampo =
                    campo.getName();

            // Se o campo non existe no backup ou é null, saltámolo
            if (!json.has(nombreCampo) || json.isNull(nombreCampo)) {
                continue;
            }

            Class<?> tipo =
                    campo.getType();

            // Restaurar valores int
            if (tipo == int.class) {

                campo.setInt(
                        objeto,
                        json.getInt(nombreCampo)
                );

                // Restaurar valores long
            } else if (tipo == long.class) {

                campo.setLong(
                        objeto,
                        json.getLong(nombreCampo)
                );

                // Restaurar valores double
            } else if (tipo == double.class) {

                campo.setDouble(
                        objeto,
                        json.getDouble(nombreCampo)
                );

                // Restaurar valores boolean
            } else if (tipo == boolean.class) {

                campo.setBoolean(
                        objeto,
                        json.getBoolean(nombreCampo)
                );

                // Restaurar textos
            } else if (tipo == String.class) {

                campo.set(
                        objeto,
                        json.getString(nombreCampo)
                );
            }
        }

        return objeto;
    }
}