package com.example.petcare.supabase;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseAuthManager {

    public interface AuthCallback {

        void onSuccess(
                String supabaseUserId,
                String email,
                String accessToken
        );

        void onError(String mensaje);
    }

    public interface SimpleCallback {

        void onSuccess();

        void onError(String mensaje);
    }

    public static void registrarUsuario(
            String email,
            String password,
            AuthCallback callback
    ) {

        try {

            JSONObject json =
                    new JSONObject();

            json.put("email", email);
            json.put("password", password);

            RequestBody body =
                    SupabaseClient.crearBody(json.toString());

            Request request =
                    SupabaseClient
                            .crearRequestAuth("/auth/v1/signup")
                            .post(body)
                            .build();

            SupabaseClient
                    .getClient()
                    .newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {

                            callback.onError("Erro de conexión con Supabase");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String respuesta;

                            if (response.body() != null) {

                                respuesta = response.body().string();

                            } else {

                                respuesta = "";
                            }

                            if (!response.isSuccessful()) {

                                callback.onError(obtenerMensajeErro(respuesta));
                                return;
                            }

                            try {

                                JSONObject jsonRespuesta =
                                        new JSONObject(respuesta);

                                JSONObject user =
                                        jsonRespuesta.optJSONObject("user");

                                // Cando Supabase require verificación por email,
                                // ás veces a resposta pode vir sen access_token
                                if (user == null) {

                                    user =
                                            jsonRespuesta;
                                }

                                String supabaseUserId =
                                        user.optString("id", "");

                                String emailRespuesta =
                                        user.optString("email", email);

                                String accessToken =
                                        jsonRespuesta.optString("access_token", "");

                                if (supabaseUserId.isEmpty()) {

                                    callback.onError("Non se puido obter o id do usuario de Supabase");
                                    return;
                                }

                                callback.onSuccess(
                                        supabaseUserId,
                                        emailRespuesta,
                                        accessToken
                                );

                            } catch (Exception e) {

                                callback.onError("Erro lendo a resposta de Supabase");
                            }
                        }
                    });

        } catch (Exception e) {

            callback.onError("Erro preparando o rexistro");
        }
    }

    public static void iniciarSesion(
            String email,
            String password,
            AuthCallback callback
    ) {

        try {

            JSONObject json =
                    new JSONObject();

            json.put("email", email);
            json.put("password", password);

            RequestBody body =
                    SupabaseClient.crearBody(json.toString());

            Request request =
                    SupabaseClient
                            .crearRequestAuth("/auth/v1/token?grant_type=password")
                            .post(body)
                            .build();

            SupabaseClient
                    .getClient()
                    .newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {

                            callback.onError("Erro de conexión con Supabase");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String respuesta;

                            if (response.body() != null) {

                                respuesta = response.body().string();

                            } else {

                                respuesta = "";
                            }

                            if (!response.isSuccessful()) {

                                String mensaje =
                                        obtenerMensajeErro(respuesta);

                                if (mensaje.toLowerCase().contains("email not confirmed")) {

                                    mensaje = "Tes que verificar o correo electrónico antes de iniciar sesión";
                                }

                                callback.onError(mensaje);
                                return;
                            }

                            try {

                                JSONObject jsonRespuesta =
                                        new JSONObject(respuesta);

                                JSONObject user =
                                        jsonRespuesta.optJSONObject("user");

                                if (user == null) {

                                    callback.onError("Non se puido obter o usuario de Supabase");
                                    return;
                                }

                                String supabaseUserId =
                                        user.optString("id", "");

                                String emailRespuesta =
                                        user.optString("email", email);

                                String accessToken =
                                        jsonRespuesta.optString("access_token", "");

                                if (supabaseUserId.isEmpty()) {

                                    callback.onError("Non se puido obter o id do usuario de Supabase");
                                    return;
                                }

                                callback.onSuccess(
                                        supabaseUserId,
                                        emailRespuesta,
                                        accessToken
                                );

                            } catch (Exception e) {

                                callback.onError("Erro lendo a resposta de Supabase");
                            }
                        }
                    });

        } catch (Exception e) {

            callback.onError("Erro preparando o inicio de sesión");
        }
    }

    public static void recuperarContrasinal(
            String email,
            SimpleCallback callback
    ) {

        try {

            JSONObject json =
                    new JSONObject();

            json.put("email", email);

            RequestBody body =
                    SupabaseClient.crearBody(json.toString());

            Request request =
                    SupabaseClient
                            .crearRequestAuth("/auth/v1/recover")
                            .post(body)
                            .build();

            SupabaseClient
                    .getClient()
                    .newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {

                            callback.onError("Erro de conexión con Supabase");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String respuesta;

                            if (response.body() != null) {

                                respuesta = response.body().string();

                            } else {

                                respuesta = "";
                            }

                            if (!response.isSuccessful()) {

                                callback.onError(obtenerMensajeErro(respuesta));
                                return;
                            }

                            callback.onSuccess();
                        }
                    });

        } catch (Exception e) {

            callback.onError("Erro preparando a recuperación");
        }
    }

    private static String obtenerMensajeErro(String respuesta) {

        try {

            JSONObject json =
                    new JSONObject(respuesta);

            String mensaje =
                    json.optString(
                            "msg",
                            json.optString(
                                    "message",
                                    "Erro de autenticación"
                            )
                    );

            return mensaje;

        } catch (Exception e) {

            return "Erro de autenticación";
        }
    }
}