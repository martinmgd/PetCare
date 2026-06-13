package com.example.petcare.util;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;

public class SwipeNavigationHelper {

    private static final int DISTANCIA_MINIMA = 160;
    private static final int DIFERENCIA_VERTICAL_MAXIMA = 120;

    private final Activity activity;
    private final Class<?> activityIzquierda;
    private final Class<?> activityDerecha;

    private float xInicial;
    private float yInicial;

    public SwipeNavigationHelper(
            Activity activity,
            Class<?> activityIzquierda,
            Class<?> activityDerecha
    ) {

        this.activity = activity;
        this.activityIzquierda = activityIzquierda;
        this.activityDerecha = activityDerecha;
    }

    public boolean procesarEvento(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            xInicial = event.getX();
            yInicial = event.getY();

            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {

            float xFinal = event.getX();
            float yFinal = event.getY();

            float diferenciaX = xFinal - xInicial;
            float diferenciaY = yFinal - yInicial;

            if (Math.abs(diferenciaY) > DIFERENCIA_VERTICAL_MAXIMA) {

                return false;
            }

            // Deslizar á dereita
            if (diferenciaX > DISTANCIA_MINIMA) {

                navegar(activityDerecha);
                return true;
            }

            // Deslizar á esquerda
            if (diferenciaX < -DISTANCIA_MINIMA) {

                navegar(activityIzquierda);
                return true;
            }
        }

        return false;
    }

    private void navegar(Class<?> destino) {

        Intent intent =
                new Intent(
                        activity,
                        destino
                );

        activity.startActivity(intent);

        // Evitar animacións por defecto entre pantallas
        activity.overridePendingTransition(0, 0);

        activity.finish();
    }
}