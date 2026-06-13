package com.example.petcare.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SesionUsuario {

    private static final String PREFS = "petcare_sesion";

    private static final String USUARIO_ID = "usuario_id";
    private static final String USUARIO_SUPABASE_ID = "usuario_supabase_id";
    private static final String USUARIO_EMAIL = "usuario_email";

    public static void guardarUsuario(
            Context context,
            String usuarioId,
            String supabaseId,
            String email
    ) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        prefs.edit()
                .putString(USUARIO_ID, usuarioId)
                .putString(USUARIO_SUPABASE_ID, supabaseId)
                .putString(USUARIO_EMAIL, email)
                .apply();
    }

    public static String obtenerUsuarioId(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        return prefs.getString(USUARIO_ID, "");
    }

    public static String obtenerUsuarioSupabaseId(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        return prefs.getString(USUARIO_SUPABASE_ID, "");
    }

    public static String obtenerUsuarioEmail(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        return prefs.getString(USUARIO_EMAIL, "");
    }

    public static void cerrarSesion(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREFS,
                        Context.MODE_PRIVATE
                );

        prefs.edit().clear().apply();
    }
}