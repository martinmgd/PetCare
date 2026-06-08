package com.example.petcare.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.petcare.dto.PesoDTO;

import java.util.ArrayList;

public class GraficoPesoView extends View {

    private ArrayList<PesoDTO> pesos;

    private Paint paintLinea;
    private Paint paintPunto;
    private Paint paintTexto;
    private Paint paintEjes;
    private Paint paintGrid;

    private int anchoMinimo;
    private int altoGrafico;
    private int espaciadoHorizontal;

    public GraficoPesoView(Context context) {
        super(context);
        iniciar();
    }

    public GraficoPesoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniciar();
    }

    // Configuración inicial
    private void iniciar() {

        pesos = new ArrayList<>();

        anchoMinimo = dp(320);
        altoGrafico = dp(210);
        espaciadoHorizontal = dp(45);

        paintLinea = new Paint();
        paintLinea.setColor(Color.parseColor("#8F61FF"));
        paintLinea.setStrokeWidth(dp(3));
        paintLinea.setStyle(Paint.Style.STROKE);
        paintLinea.setAntiAlias(true);

        paintPunto = new Paint();
        paintPunto.setColor(Color.parseColor("#8F61FF"));
        paintPunto.setStyle(Paint.Style.FILL);
        paintPunto.setAntiAlias(true);

        paintTexto = new Paint();
        paintTexto.setColor(Color.parseColor("#555555"));
        paintTexto.setTextSize(dp(10));
        paintTexto.setAntiAlias(true);

        paintEjes = new Paint();
        paintEjes.setColor(Color.parseColor("#999999"));
        paintEjes.setStrokeWidth(dp(1));
        paintEjes.setAntiAlias(true);

        paintGrid = new Paint();
        paintGrid.setColor(Color.parseColor("#DDDDDD"));
        paintGrid.setStrokeWidth(dp(1));
        paintGrid.setAntiAlias(true);
    }

    // Recibir datos
    public void setPesos(ArrayList<PesoDTO> pesos) {

        this.pesos = pesos;

        int anchoCalculado =
                dp(90) +
                        ((pesos.size() - 1) * espaciadoHorizontal);

        if (anchoCalculado < anchoMinimo) {

            anchoCalculado = anchoMinimo;
        }

        if (getLayoutParams() != null) {

            getLayoutParams().width = anchoCalculado;
        }

        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(
            int widthMeasureSpec,
            int heightMeasureSpec
    ) {

        int ancho = anchoMinimo;

        if (pesos != null && pesos.size() > 0) {

            ancho =
                    dp(90) +
                            ((pesos.size() - 1) * espaciadoHorizontal);

            if (ancho < anchoMinimo) {

                ancho = anchoMinimo;
            }
        }

        setMeasuredDimension(
                ancho,
                altoGrafico
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (pesos == null || pesos.size() < 2) {

            canvas.drawText(
                    "O gráfico mostrarase cando existan varios rexistros de peso.",
                    dp(24),
                    dp(105),
                    paintTexto
            );

            return;
        }

        int ancho = getWidth();
        int alto = getHeight();

        int margenIzquierdo = dp(38);
        int margenDerecho = dp(18);
        int margenSuperior = dp(24);
        int margenInferior = dp(48);

        int altoDisponible =
                alto - margenSuperior - margenInferior;

        double pesoMin = pesos.get(0).getPeso();
        double pesoMax = pesos.get(0).getPeso();

        for (PesoDTO pesoDTO : pesos) {

            if (pesoDTO.getPeso() < pesoMin) {

                pesoMin = pesoDTO.getPeso();
            }

            if (pesoDTO.getPeso() > pesoMax) {

                pesoMax = pesoDTO.getPeso();
            }
        }

        pesoMin = Math.floor(pesoMin - 1);
        pesoMax = Math.ceil(pesoMax + 1);

        if (pesoMax <= pesoMin) {

            pesoMax = pesoMin + 1;
        }

        // Liñas horizontais
        int lineas = 4;

        for (int i = 0; i <= lineas; i++) {

            float y =
                    margenSuperior +
                            ((float) altoDisponible / lineas) * i;

            canvas.drawLine(
                    margenIzquierdo,
                    y,
                    ancho - margenDerecho,
                    y,
                    paintGrid
            );

            double valor =
                    pesoMax -
                            ((pesoMax - pesoMin) / lineas) * i;

            canvas.drawText(
                    String.format("%.1f", valor),
                    dp(3),
                    y + dp(4),
                    paintTexto
            );
        }

        // Eixe vertical
        canvas.drawLine(
                margenIzquierdo,
                margenSuperior,
                margenIzquierdo,
                alto - margenInferior,
                paintEjes
        );

        // Eixe horizontal
        canvas.drawLine(
                margenIzquierdo,
                alto - margenInferior,
                ancho - margenDerecho,
                alto - margenInferior,
                paintEjes
        );

        Path path = new Path();

        for (int i = 0; i < pesos.size(); i++) {

            PesoDTO pesoDTO = pesos.get(i);

            float x =
                    margenIzquierdo +
                            (i * espaciadoHorizontal);

            float y =
                    margenSuperior +
                            (float) ((pesoMax - pesoDTO.getPeso()) /
                                    (pesoMax - pesoMin) * altoDisponible);

            if (i == 0) {

                path.moveTo(x, y);

            } else {

                path.lineTo(x, y);
            }

            // Punto
            canvas.drawCircle(
                    x,
                    y,
                    dp(4),
                    paintPunto
            );

            // Texto peso
            canvas.drawText(
                    String.valueOf(pesoDTO.getPeso()),
                    x - dp(12),
                    y - dp(10),
                    paintTexto
            );

            // Fecha
            canvas.save();

            canvas.rotate(
                    -45,
                    x,
                    alto - margenInferior + dp(25)
            );

            canvas.drawText(
                    pesoDTO.getFecha(),
                    x - dp(25),
                    alto - margenInferior + dp(25),
                    paintTexto
            );

            canvas.restore();
        }

        // Liña do gráfico
        canvas.drawPath(
                path,
                paintLinea
        );

        // Etiqueta peso
        canvas.drawText(
                "Peso (kg)",
                dp(3),
                dp(16),
                paintTexto
        );

        // Etiqueta data
        canvas.drawText(
                "Data",
                ancho - dp(45),
                alto - dp(8),
                paintTexto
        );
    }

    // Converter dp a px
    private int dp(int valor) {

        return (int) (
                valor *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }
}