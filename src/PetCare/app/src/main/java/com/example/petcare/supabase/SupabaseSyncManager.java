package com.example.petcare.supabase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.UsuarioDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.UsuarioDTO;
import com.example.petcare.dao.ComidaDAO;
import com.example.petcare.dao.InvitacionDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.dto.ComidaDTO;
import com.example.petcare.dto.InvitacionDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Request;
import okhttp3.Response;

public class SupabaseSyncManager {

    // Evitar varias sincronizacións ao mesmo tempo
    private static boolean sincronizando = false;

    // Punto de entrada da sincronización
    public static void sincronizar(Context context) {

        if (sincronizando) {
            return;
        }

        if (!hayInternet(context)) {
            Log.d("SupabaseSync", "Sen conexión. Queda pendente.");
            return;
        }

        sincronizando = true;

        new Thread(() -> {

            try {

                Log.d("SupabaseSync", "Iniciando sincronización...");

                sincronizarUsuarios(context);
                sincronizarMascotas(context);
                sincronizarEventos(context);
                sincronizarUsuariosMascotas(context);
                sincronizarInvitaciones(context);
                sincronizarComidas(context);


                Log.d("SupabaseSync", "Sincronización finalizada.");

            } catch (Exception e) {

                Log.e(
                        "SupabaseSync",
                        "Erro na sincronización: " + e.getMessage()
                );

            } finally {

                sincronizando = false;
            }

        }).start();
    }


    // USUARIOS

    // Sincronizar usuarios pendentes con Supabase
    private static void sincronizarUsuarios(Context context) throws Exception {

        UsuarioDAO usuarioDAO =
                PetCareDatabase
                        .getInstance(context)
                        .usuarioDAO();

        List<UsuarioDTO> usuariosPendientes =
                usuarioDAO.listarPendientesSync();

        for (UsuarioDTO usuario : usuariosPendientes) {

            String json =
                    crearJsonUsuario(usuario);

            if (estaVacio(usuario.getSupabaseId())) {

                String respuesta =
                        insertar(
                                "usuarios",
                                json
                        );

                usuario.setSupabaseId(
                        extraerIdSupabase(respuesta)
                );

            } else {

                actualizarPorId(
                        "usuarios",
                        usuario.getSupabaseId(),
                        json
                );
            }

            usuario.setPendienteSync(false);
            usuario.setFechaActualizacion(System.currentTimeMillis());

            usuarioDAO.actualizar(usuario);
        }
    }

    // Crear JSON para a táboa usuarios de Supabase
    private static String crearJsonUsuario(UsuarioDTO usuario) throws Exception {

        JSONObject json =
                new JSONObject();

        json.put("nombre", textoSeguro(usuario.getNombre()));
        json.put("email", textoSeguro(usuario.getEmail()));
        json.put("foto", textoSeguro(usuario.getFoto()));
        json.put("fecha_creacion", convertirFechaIso(usuario.getFechaCreacion()));
        json.put("fecha_actualizacion", convertirFechaIso(usuario.getFechaActualizacion()));

        return json.toString();
    }


    // MASCOTAS

    // Sincronizar mascotas pendentes con Supabase
    private static void sincronizarMascotas(Context context) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        MascotaDAO mascotaDAO =
                db.mascotaDAO();

        UsuarioDAO usuarioDAO =
                db.usuarioDAO();

        List<MascotaDTO> mascotasPendientes =
                mascotaDAO.listarPendientesSync();

        for (MascotaDTO mascota : mascotasPendientes) {

            String propietarioSupabaseId =
                    obtenerSupabaseIdUsuario(
                            usuarioDAO,
                            mascota.getPropietarioId()
                    );

            String json =
                    crearJsonMascota(
                            mascota,
                            propietarioSupabaseId
                    );

            if (estaVacio(mascota.getSupabaseId())) {

                String respuesta =
                        insertar(
                                "mascotas",
                                json
                        );

                mascota.setSupabaseId(
                        extraerIdSupabase(respuesta)
                );

            } else {

                actualizarPorId(
                        "mascotas",
                        mascota.getSupabaseId(),
                        json
                );
            }

            mascota.setPendienteSync(false);
            mascota.setFechaActualizacion(System.currentTimeMillis());

            mascotaDAO.actualizar(mascota);
        }
    }

    // Crear JSON para a táboa mascotas de Supabase
    private static String crearJsonMascota(
            MascotaDTO mascota,
            String propietarioSupabaseId
    ) throws Exception {

        JSONObject json =
                new JSONObject();

        json.put("propietario_id", propietarioSupabaseId);
        json.put("nombre", textoSeguro(mascota.getNombre()));
        json.put("especie", textoSeguro(mascota.getEspecie()));
        json.put("raza", textoSeguro(mascota.getRaza()));
        json.put("edad", mascota.getEdad());
        json.put("peso_actual", mascota.getPesoActual());
        json.put("fecha_nacimiento", convertirFechaSimple(mascota.getFechaNacimiento()));
        json.put("sexo", textoSeguro(mascota.getSexo()));
        json.put("foto", "");
        json.put("fecha_creacion", convertirFechaIso(mascota.getFechaCreacion()));
        json.put("fecha_actualizacion", convertirFechaIso(mascota.getFechaActualizacion()));

        return json.toString();
    }


    // EVENTOS

    // Sincronizar eventos pendentes con Supabase
    private static void sincronizarEventos(Context context) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        EventoDAO eventoDAO =
                db.eventoDAO();

        UsuarioDAO usuarioDAO =
                db.usuarioDAO();

        MascotaDAO mascotaDAO =
                db.mascotaDAO();

        List<EventoDTO> eventosPendientes =
                eventoDAO.listarPendientesSync();

        for (EventoDTO evento : eventosPendientes) {

            String usuarioSupabaseId =
                    obtenerSupabaseIdUsuario(
                            usuarioDAO,
                            evento.getUsuarioId()
                    );

            String mascotaSupabaseId =
                    obtenerSupabaseIdMascota(
                            mascotaDAO,
                            evento.getMascotaId()
                    );

            String json =
                    crearJsonEvento(
                            evento,
                            usuarioSupabaseId,
                            mascotaSupabaseId
                    );

            if (estaVacio(evento.getSupabaseId())) {

                String respuesta =
                        insertar(
                                "eventos",
                                json
                        );

                evento.setSupabaseId(
                        extraerIdSupabase(respuesta)
                );

            } else {

                actualizarPorId(
                        "eventos",
                        evento.getSupabaseId(),
                        json
                );
            }

            evento.setPendienteSync(false);
            evento.setFechaActualizacion(System.currentTimeMillis());

            eventoDAO.actualizar(evento);
        }
    }
    // Crear JSON para a táboa eventos de Supabase
    private static String crearJsonEvento(
            EventoDTO evento,
            String usuarioSupabaseId,
            String mascotaSupabaseId
    ) throws Exception {

        JSONObject json =
                new JSONObject();

        json.put("usuario_id", usuarioSupabaseId);
        json.put("mascota_id", mascotaSupabaseId);
        json.put("titulo", textoSeguro(evento.getTitulo()));
        json.put("descripcion", textoSeguro(evento.getDescripcion()));
        json.put("fecha", convertirFechaSimple(evento.getFecha()));
        json.put("hora", convertirHoraSupabase(evento.getHora()));
        json.put("tipo", textoSeguro(evento.getTipo()));
        json.put("mascota", textoSeguro(evento.getMascota()));
        json.put("fecha_creacion", convertirFechaIso(evento.getFechaCreacion()));
        json.put("fecha_actualizacion", convertirFechaIso(evento.getFechaActualizacion()));

        return json.toString();
    }

    // USUARIOS_MASCOTAS

    // Sincronizar relacións usuario-mascota pendentes con Supabase
    private static void sincronizarUsuariosMascotas(Context context) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        UsuarioMascotaDAO usuarioMascotaDAO =
                db.usuarioMascotaDAO();

        UsuarioDAO usuarioDAO =
                db.usuarioDAO();

        MascotaDAO mascotaDAO =
                db.mascotaDAO();

        List<UsuarioMascotaDTO> relacionesPendientes =
                usuarioMascotaDAO.listarPendientesSync();

        for (UsuarioMascotaDTO relacion : relacionesPendientes) {

            String usuarioSupabaseId =
                    resolverUsuarioSupabaseId(
                            usuarioDAO,
                            relacion.getUsuarioId()
                    );

            String mascotaSupabaseId =
                    obtenerSupabaseIdMascota(
                            mascotaDAO,
                            relacion.getMascotaId()
                    );

            String json =
                    crearJsonUsuarioMascota(
                            relacion,
                            usuarioSupabaseId,
                            mascotaSupabaseId
                    );

            if (estaVacio(relacion.getSupabaseId())) {

                String respuesta =
                        insertar(
                                "usuarios_mascotas",
                                json
                        );

                relacion.setSupabaseId(
                        extraerIdSupabase(respuesta)
                );

            } else {

                actualizarPorId(
                        "usuarios_mascotas",
                        relacion.getSupabaseId(),
                        json
                );
            }

            relacion.setPendienteSync(false);
            relacion.setFechaActualizacion(System.currentTimeMillis());

            usuarioMascotaDAO.actualizar(relacion);
        }
    }

    // Crear JSON para a táboa usuarios_mascotas de Supabase
    private static String crearJsonUsuarioMascota(
            UsuarioMascotaDTO relacion,
            String usuarioSupabaseId,
            String mascotaSupabaseId
    ) throws Exception {

        JSONObject json =
                new JSONObject();

        json.put("usuario_id", usuarioSupabaseId);
        json.put("mascota_id", mascotaSupabaseId);
        json.put("rol", textoSeguro(relacion.getRol()));
        json.put("estado", textoSeguro(relacion.getEstado()));
        json.put("fecha_creacion", convertirFechaIso(relacion.getFechaCreacion()));

        return json.toString();
    }


    // INVITACIONES

    // Sincronizar invitacións pendentes con Supabase
    private static void sincronizarInvitaciones(Context context) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        InvitacionDAO invitacionDAO =
                db.invitacionDAO();

        UsuarioDAO usuarioDAO =
                db.usuarioDAO();

        MascotaDAO mascotaDAO =
                db.mascotaDAO();

        List<InvitacionDTO> invitacionesPendientes =
                invitacionDAO.listarPendientesSync();

        for (InvitacionDTO invitacion : invitacionesPendientes) {

            String mascotaSupabaseId =
                    obtenerSupabaseIdMascota(
                            mascotaDAO,
                            invitacion.getMascotaId()
                    );

            String usuarioOrigenSupabaseId =
                    resolverUsuarioSupabaseId(
                            usuarioDAO,
                            invitacion.getUsuarioOrigenId()
                    );

            String usuarioDestinoSupabaseId =
                    resolverUsuarioSupabaseId(
                            usuarioDAO,
                            invitacion.getUsuarioDestinoId()
                    );

            String json =
                    crearJsonInvitacion(
                            invitacion,
                            mascotaSupabaseId,
                            usuarioOrigenSupabaseId,
                            usuarioDestinoSupabaseId
                    );

            if (estaVacio(invitacion.getSupabaseId())) {

                String respuesta =
                        insertar(
                                "invitaciones",
                                json
                        );

                invitacion.setSupabaseId(
                        extraerIdSupabase(respuesta)
                );

            } else {

                actualizarPorId(
                        "invitaciones",
                        invitacion.getSupabaseId(),
                        json
                );
            }

            invitacion.setPendienteSync(false);
            invitacion.setFechaActualizacion(System.currentTimeMillis());

            invitacionDAO.actualizar(invitacion);
        }
    }

    // Crear JSON para a táboa invitaciones de Supabase
    private static String crearJsonInvitacion(
            InvitacionDTO invitacion,
            String mascotaSupabaseId,
            String usuarioOrigenSupabaseId,
            String usuarioDestinoSupabaseId
    ) throws Exception {

        JSONObject json =
                new JSONObject();

        json.put("mascota_id", mascotaSupabaseId);
        json.put("usuario_origen_id", usuarioOrigenSupabaseId);
        json.put("usuario_destino_id", usuarioDestinoSupabaseId);
        json.put("estado", textoSeguro(invitacion.getEstado()));
        json.put("fecha_creacion", convertirFechaIso(invitacion.getFechaCreacion()));

        return json.toString();
    }


    // COMIDAS

    // Sincronizar comidas pendentes con Supabase
    private static void sincronizarComidas(Context context) throws Exception {

        PetCareDatabase db =
                PetCareDatabase.getInstance(context);

        ComidaDAO comidaDAO =
                db.comidaDAO();

        MascotaDAO mascotaDAO =
                db.mascotaDAO();

        List<ComidaDTO> comidasPendientes =
                comidaDAO.listarPendientesSync();

        for (ComidaDTO comida : comidasPendientes) {

            String mascotaSupabaseId =
                    obtenerSupabaseIdMascota(
                            mascotaDAO,
                            comida.getMascotaId()
                    );

            String json =
                    crearJsonComida(
                            comida,
                            mascotaSupabaseId
                    );

            if (estaVacio(comida.getSupabaseId())) {

                String respuesta =
                        insertar(
                                "comidas",
                                json
                        );

                comida.setSupabaseId(
                        extraerIdSupabase(respuesta)
                );

            } else {

                actualizarPorId(
                        "comidas",
                        comida.getSupabaseId(),
                        json
                );
            }

            comida.setPendienteSync(false);
            comida.setFechaActualizacion(System.currentTimeMillis());

            comidaDAO.actualizar(comida);
        }
    }

    // Crear JSON para a táboa comidas de Supabase
    private static String crearJsonComida(
            ComidaDTO comida,
            String mascotaSupabaseId
    ) throws Exception {

        JSONObject json =
                new JSONObject();

        json.put("mascota_id", mascotaSupabaseId);
        json.put("fecha", convertirFechaSimple(comida.getFecha()));
        json.put("almorzo", comida.isAlmorzoComido());
        json.put("comida", comida.isComidaComida());
        json.put("cea", comida.isCeaComida());
        json.put("fecha_creacion", convertirFechaIso(comida.getFechaCreacion()));
        json.put("fecha_actualizacion", convertirFechaIso(comida.getFechaActualizacion()));

        return json.toString();
    }


    // Metodos para relacions

    // Obter UUID de Supabase dun usuario local
    private static String obtenerSupabaseIdUsuario(
            UsuarioDAO usuarioDAO,
            String usuarioIdLocal
    ) throws Exception {

        if (estaVacio(usuarioIdLocal)) {
            throw new Exception("Non hai usuario local asignado");
        }

        int idLocal =
                Integer.parseInt(usuarioIdLocal);

        UsuarioDTO usuario =
                usuarioDAO.obtenerPorId(idLocal);

        if (usuario == null) {
            throw new Exception("Non se atopou o usuario local");
        }

        if (estaVacio(usuario.getSupabaseId())) {
            throw new Exception("O usuario aínda non ten supabaseId");
        }

        return usuario.getSupabaseId();
    }

    // Obter UUID de Supabase dunha mascota local
    private static String obtenerSupabaseIdMascota(
            MascotaDAO mascotaDAO,
            int mascotaIdLocal
    ) throws Exception {

        if (mascotaIdLocal <= 0) {
            throw new Exception("Non hai mascota local asignada");
        }

        MascotaDTO mascota =
                mascotaDAO.obtenerPorId(mascotaIdLocal);

        if (mascota == null) {
            throw new Exception("Non se atopou a mascota local");
        }

        if (estaVacio(mascota.getSupabaseId())) {
            throw new Exception("A mascota aínda non ten supabaseId");
        }

        return mascota.getSupabaseId();
    }

    // Resolver usuario local ou UUID de Supabase
    private static String resolverUsuarioSupabaseId(
            UsuarioDAO usuarioDAO,
            String usuarioId
    ) throws Exception {

        if (estaVacio(usuarioId)) {
            throw new Exception("Non hai usuario asignado");
        }

        if (usuarioId.contains("-")) {
            return usuarioId;
        }

        return obtenerSupabaseIdUsuario(
                usuarioDAO,
                usuarioId
        );
    }

    // Metodos de Supabase

    // Inserir rexistros en Supabase
    public static String insertar(
            String tabla,
            String json
    ) throws Exception {

        Request request =
                SupabaseClient
                        .crearRequest("/rest/v1/" + tabla)
                        .post(
                                SupabaseClient.crearBody(json)
                        )
                        .build();

        Response response =
                SupabaseClient
                        .getClient()
                        .newCall(request)
                        .execute();

        String body = "";

        if (response.body() != null) {
            body = response.body().string();
        }

        if (!response.isSuccessful()) {
            throw new Exception(
                    "Erro insertando en "
                            + tabla
                            + ": "
                            + response.code()
                            + " "
                            + body
            );
        }

        return body;
    }

    // Actualizar rexistros en Supabase por UUID
    public static String actualizarPorId(
            String tabla,
            String supabaseId,
            String json
    ) throws Exception {

        Request request =
                SupabaseClient
                        .crearRequest(
                                "/rest/v1/"
                                        + tabla
                                        + "?id=eq."
                                        + supabaseId
                        )
                        .patch(
                                SupabaseClient.crearBody(json)
                        )
                        .build();

        Response response =
                SupabaseClient
                        .getClient()
                        .newCall(request)
                        .execute();

        String body = "";

        if (response.body() != null) {
            body = response.body().string();
        }

        if (!response.isSuccessful()) {
            throw new Exception(
                    "Erro actualizando en "
                            + tabla
                            + ": "
                            + response.code()
                            + " "
                            + body
            );
        }

        return body;
    }

    // Extraer UUID devolto por Supabase
    private static String extraerIdSupabase(String respuesta) throws Exception {

        JSONArray array =
                new JSONArray(respuesta);

        if (array.length() == 0) {
            throw new Exception("Supabase non devolveu o rexistro insertado");
        }

        JSONObject objeto =
                array.getJSONObject(0);

        return objeto.getString("id");
    }

    // Comprobar se hai conexión a internet
    private static boolean hayInternet(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager)
                        context.getSystemService(
                                Context.CONNECTIVITY_SERVICE
                        );

        if (connectivityManager == null) {
            return false;
        }

        Network network =
                connectivityManager.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(network);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
        );
    }




    // Metodos auxiliares

    // Comprobar se un texto está baleiro
    private static boolean estaVacio(String texto) {

        return texto == null || texto.trim().isEmpty();
    }

    // Evitar valores null en textos
    private static String textoSeguro(String texto) {

        if (texto == null) {
            return "";
        }

        return texto;
    }

    // Converter timestamp local a formato ISO para Supabase
    private static String convertirFechaIso(long fechaMillis) {

        SimpleDateFormat formato =
                new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        Locale.getDefault()
                );

        formato.setTimeZone(
                TimeZone.getTimeZone("UTC")
        );

        return formato.format(
                new Date(fechaMillis)
        );
    }

    // Converter dd/MM/yyyy a yyyy-MM-dd
    private static Object convertirFechaSimple(String fecha) {

        if (estaVacio(fecha)) {
            return JSONObject.NULL;
        }

        try {

            SimpleDateFormat formatoEntrada =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    );

            SimpleDateFormat formatoSalida =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );

            Date date =
                    formatoEntrada.parse(fecha);

            if (date == null) {
                return JSONObject.NULL;
            }

            return formatoSalida.format(date);

        } catch (Exception e) {

            return JSONObject.NULL;
        }
    }

    // Converter HH:mm a HH:mm:ss
    private static Object convertirHoraSupabase(String hora) {

        if (estaVacio(hora)) {
            return JSONObject.NULL;
        }

        if (hora.length() == 5) {
            return hora + ":00";
        }

        return hora;
    }
}