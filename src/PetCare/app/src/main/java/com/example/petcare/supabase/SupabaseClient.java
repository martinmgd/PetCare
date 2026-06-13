package com.example.petcare.supabase;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SupabaseClient {

    private static final OkHttpClient client =
            new OkHttpClient();

    public static OkHttpClient getClient() {

        return client;
    }

    public static Request.Builder crearRequest(String endpoint) {

        return new Request.Builder()
                .url(
                        SupabaseConfig.SUPABASE_URL
                                + endpoint
                )
                .addHeader(
                        "apikey",
                        SupabaseConfig.SUPABASE_API_KEY
                )
                .addHeader(
                        "Authorization",
                        "Bearer "
                                + SupabaseConfig.SUPABASE_API_KEY
                )
                .addHeader(
                        "Content-Type",
                        "application/json"
                )
                .addHeader(
                        "Prefer",
                        "return=representation"
                );
    }

    public static RequestBody crearBody(String json) {

        return RequestBody.create(
                json,
                MediaType.parse(
                        "application/json"
                )
        );
    }
}