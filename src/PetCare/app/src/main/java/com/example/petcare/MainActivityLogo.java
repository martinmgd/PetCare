package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityLogo extends AppCompatActivity {

    private static final int DURACION_SPLASH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ocultar barra superior (temporal)
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_logo);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Intent intent =
                    new Intent(
                            MainActivityLogo.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();

        }, DURACION_SPLASH);
    }
}