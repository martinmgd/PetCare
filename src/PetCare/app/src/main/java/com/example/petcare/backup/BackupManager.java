package com.example.petcare.backup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.supabase.SupabaseClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Request;
import okhttp3.Response;

public class BackupManager {

    private static final String PREFS = "petcare_backups";
    private static final String ULTIMO_BACKUP = "ultimo_backup";

    private static final long UN_DIA =
            24 * 60 * 60 * 1000;

    private static final int MAX_BACKUPS_LOCALES =
            3;

    public static void comprobarBackupDiario(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        long ultimoBackup =
                prefs.getLong(
                        ULTIMO_BACKUP,
                        0
                );

        long ahora =
                System.currentTimeMillis();

        if (ahora - ultimoBackup < UN_DIA) {
            return;
        }

        crearBackupCompleto(context);

        prefs.edit()
                .putLong(
                        ULTIMO_BACKUP,
                        ahora
                )
                .apply();
    }

    public static void crearBackupCompleto(Context context) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    JSONObject backup =
                            crearJsonBackup(context);

                    guardarBackupLocal(
                            context,
                            backup
                    );

                    subirBackupSupabase(
                            context,
                            backup
                    );

                    Log.d(
                            "BackupManager",
                            "Backup creado correctamente"
                    );

                } catch (Exception e) {

                    Log.e(
                            "BackupManager",
                            "Erro creando backup: " + e.getMessage()
                    );
                }
            }
        }).start();
    }

    private static JSONObject crearJsonBackup(Context context) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        JSONObject backup =
                new JSONObject();

        backup.put("version", 1);
        backup.put("fecha", System.currentTimeMillis());
        backup.put("usuario_supabase_id", SesionUsuario.obtenerUsuarioSupabaseId(context));
        backup.put("usuario_email", SesionUsuario.obtenerUsuarioEmail(context));

        backup.put("usuarios", listaAJson(db.usuarioDAO().listar()));
        backup.put("mascotas", listaAJson(db.mascotaDAO().listar()));
        backup.put("eventos", listaAJson(db.eventoDAO().listar()));
        backup.put("usuarios_mascotas", listaAJson(db.usuarioMascotaDAO().listar()));
        backup.put("invitaciones", listaAJson(db.invitacionDAO().listar()));
        backup.put("comidas", listaAJson(db.comidaDAO().listar()));
        backup.put("pesos", listaAJson(db.pesoDAO().listar()));
        backup.put("vacinas", listaAJson(db.vacinaDAO().listar()));
        backup.put("medicaciones", listaAJson(db.medicacionDAO().listar()));
        backup.put("actividades", listaAJson(db.actividadeDAO().listar()));

        return backup;
    }

    private static JSONArray listaAJson(List<?> lista) throws Exception {

        JSONArray array =
                new JSONArray();

        for (Object objeto : lista) {

            array.put(
                    objetoAJson(objeto)
            );
        }

        return array;
    }

    private static JSONObject objetoAJson(Object objeto) throws Exception {

        JSONObject json =
                new JSONObject();

        Field[] campos =
                objeto.getClass().getDeclaredFields();

        for (Field campo : campos) {

            campo.setAccessible(true);

            Object valor =
                    campo.get(objeto);

            if (valor == null) {

                json.put(
                        campo.getName(),
                        JSONObject.NULL
                );

            } else {

                json.put(
                        campo.getName(),
                        valor
                );
            }
        }

        return json;
    }

    private static void guardarBackupLocal(
            Context context,
            JSONObject backup
    ) throws Exception {

        File carpeta =
                new File(
                        context.getExternalFilesDir(null),
                        "backups"
                );

        if (!carpeta.exists()) {

            carpeta.mkdirs();
        }

        String nombreArchivo =
                crearNombreBackup();

        File archivo =
                new File(
                        carpeta,
                        nombreArchivo
                );

        FileWriter writer =
                new FileWriter(archivo);

        writer.write(
                backup.toString(4)
        );

        writer.flush();
        writer.close();

        borrarBackupsLocalesAntiguos(carpeta);
    }

    private static void borrarBackupsLocalesAntiguos(File carpeta) {

        File[] archivos =
                carpeta.listFiles();

        if (archivos == null) {
            return;
        }

        if (archivos.length <= MAX_BACKUPS_LOCALES) {
            return;
        }

        for (int i = 0; i < archivos.length - 1; i++) {

            for (int j = i + 1; j < archivos.length; j++) {

                if (archivos[i].lastModified() > archivos[j].lastModified()) {

                    File temporal =
                            archivos[i];

                    archivos[i] =
                            archivos[j];

                    archivos[j] =
                            temporal;
                }
            }
        }

        int cantidadABorrar =
                archivos.length - MAX_BACKUPS_LOCALES;

        for (int i = 0; i < cantidadABorrar; i++) {

            archivos[i].delete();
        }
    }

    private static void subirBackupSupabase(
            Context context,
            JSONObject backup
    ) throws Exception {

        String usuarioSupabaseId =
                SesionUsuario.obtenerUsuarioSupabaseId(context);

        if (usuarioSupabaseId.isEmpty()) {
            throw new Exception("Non hai usuario Supabase na sesión");
        }

        JSONObject json =
                new JSONObject();

        json.put("usuario_id", usuarioSupabaseId);
        json.put("nombre", crearNombreBackup());
        json.put("contenido", backup);

        Request request =
                SupabaseClient
                        .crearRequest("/rest/v1/backups")
                        .post(
                                SupabaseClient.crearBody(json.toString())
                        )
                        .build();

        Response response =
                SupabaseClient
                        .getClient()
                        .newCall(request)
                        .execute();

        String body =
                "";

        if (response.body() != null) {

            body =
                    response.body().string();
        }

        if (!response.isSuccessful()) {

            throw new Exception(
                    "Erro subindo backup a Supabase: "
                            + response.code()
                            + " "
                            + body
            );
        }
    }

    private static String crearNombreBackup() {

        SimpleDateFormat formato =
                new SimpleDateFormat(
                        "yyyy-MM-dd_HH-mm-ss",
                        Locale.getDefault()
                );

        return "backup_"
                + formato.format(new Date())
                + ".json";
    }
}