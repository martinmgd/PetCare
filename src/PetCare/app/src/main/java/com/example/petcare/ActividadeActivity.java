package com.example.petcare;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dao.ActividadeDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.dto.ActividadeDTO;
import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.petcare.util.MascotasUtils;
import com.example.petcare.supabase.SupabaseSyncManager;

public class ActividadeActivity extends AppCompatActivity {

    private Toolbar toolbarActividade;

    private MaterialAutoCompleteTextView txtMascotaActividade;
    private MaterialAutoCompleteTextView txtTipoActividade;

    private TextInputEditText txtZonaActividade;
    private TextInputEditText txtDuracionActividade;
    private TextInputEditText txtFechaActividade;
    private TextView txtZonaActual;
    private TextView txtIconoTempo;
    private TextView txtTemperatura;
    private TextView txtEstadoTempo;
    private TextView txtDetallesTempo;
    private TextView txtRecomendacionTempo;
    private TextView txtHistorialActividade;

    private MaterialButton btnConsultarTempo;
    private MaterialButton btnGuardarActividade;

    private ActividadeDAO actividadeDAO;

    private ArrayList<MascotaDTO> listaMascotas;
    private MascotaDTO mascotaSeleccionada;

    private String resumoTempoActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actividade);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        actividadeDAO = db.actividadeDAO();

        // Referencias XML
        toolbarActividade = findViewById(R.id.toolbarActividade);

        txtMascotaActividade = findViewById(R.id.txtMascotaActividade);
        txtTipoActividade = findViewById(R.id.txtTipoActividade);

        txtZonaActividade = findViewById(R.id.txtZonaActividade);
        txtDuracionActividade = findViewById(R.id.txtDuracionActividade);
        txtFechaActividade = findViewById(R.id.txtFechaActividade);

        txtZonaActual = findViewById(R.id.txtZonaActual);
        txtIconoTempo = findViewById(R.id.txtIconoTempo);
        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtEstadoTempo = findViewById(R.id.txtEstadoTempo);
        txtDetallesTempo = findViewById(R.id.txtDetallesTempo);
        txtRecomendacionTempo = findViewById(R.id.txtRecomendacionTempo);
        txtHistorialActividade = findViewById(R.id.txtHistorialActividade);

        btnConsultarTempo = findViewById(R.id.btnConsultarTempo);
        btnGuardarActividade = findViewById(R.id.btnGuardarActividade);

        // Botón volver
        toolbarActividade.setNavigationOnClickListener(v -> finish());

        // Valores iniciales
        listaMascotas = new ArrayList<>();
        txtZonaActividade.setText("Ribeira");

        cargarMascotasPermitidas();
        configurarSelectorMascota();
        configurarSelectorTipoActividade();
        configurarSelectorFecha();

        // Consultar tempo inicial
        consultarTempo();

        // Botones
        btnConsultarTempo.setOnClickListener(v -> consultarTempo());
        btnGuardarActividade.setOnClickListener(v -> guardarActividade());
    }

    // Cargar mascotas propias y compartidas aceptadas
    private void cargarMascotasPermitidas() {

        listaMascotas =
                MascotasUtils.obtenerMascotasDisponibles(
                        this
                );
    }

    // Configurar selector de mascota
    private void configurarSelectorMascota() {

        ArrayList<String> nombresMascotas =
                new ArrayList<>();

        for (MascotaDTO mascota : listaMascotas) {

            nombresMascotas.add(
                    "   " + mascota.getNombre()
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombresMascotas
                );

        txtMascotaActividade.setAdapter(adapter);

        txtMascotaActividade.setFocusable(false);
        txtMascotaActividade.setClickable(true);
        txtMascotaActividade.setCursorVisible(false);

        txtMascotaActividade.setOnItemClickListener((parent, view, position, id) -> {

            mascotaSeleccionada =
                    listaMascotas.get(position);

            cargarHistorialActividade();
        });

        if (listaMascotas.isEmpty()) {

            txtHistorialActividade.setText("Non hai mascotas dispoñibles");

            Toast.makeText(
                    this,
                    "Non hai mascotas dispoñibles",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            mascotaSeleccionada =
                    listaMascotas.get(0);

            txtMascotaActividade.setText(
                    "   " + mascotaSeleccionada.getNombre(),
                    false
            );

            cargarHistorialActividade();
        }
    }

    // Configurar selector de tipo de actividade
    private void configurarSelectorTipoActividade() {

        String[] tipos = {
                "   Paseo",
                "   Xogo",
                "   Adestramento",
                "   Carreira",
                "   Outro"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        tipos
                );

        txtTipoActividade.setAdapter(adapter);

        txtTipoActividade.setFocusable(false);
        txtTipoActividade.setClickable(true);
        txtTipoActividade.setCursorVisible(false);

        txtTipoActividade.setText("   Paseo", false);
    }

    // Configurar selector de fecha
    private void configurarSelectorFecha() {

        ponerFechaActual();

        txtFechaActividade.setFocusable(false);
        txtFechaActividade.setClickable(true);
        txtFechaActividade.setCursorVisible(false);

        txtFechaActividade.setOnClickListener(v -> mostrarCalendario());
    }

    // Poner fecha actual
    private void ponerFechaActual() {

        Calendar calendario =
                Calendar.getInstance();

        int anoActual =
                calendario.get(Calendar.YEAR);

        int mesActual =
                calendario.get(Calendar.MONTH);

        int diaActual =
                calendario.get(Calendar.DAY_OF_MONTH);

        String fechaActual =
                String.format(
                        "%02d/%02d/%04d",
                        diaActual,
                        mesActual + 1,
                        anoActual
                );

        txtFechaActividade.setText(fechaActual);
    }

    // Mostrar calendario
    private void mostrarCalendario() {

        Calendar calendario =
                Calendar.getInstance();

        int ano =
                calendario.get(Calendar.YEAR);

        int mes =
                calendario.get(Calendar.MONTH);

        int dia =
                calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogoFecha =
                new DatePickerDialog(
                        ActividadeActivity.this,
                        (view, year, month, dayOfMonth) -> {

                            String fecha =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                    );

                            txtFechaActividade.setText(fecha);
                        },
                        ano,
                        mes,
                        dia
                );

        dialogoFecha.show();
    }

    // Consultar tempo usando Open-Meteo
    private void consultarTempo() {

        String zona;

        if (txtZonaActividade.getText() != null) {

            zona =
                    txtZonaActividade
                            .getText()
                            .toString()
                            .trim();

        } else {

            zona = "";
        }

        if (zona.isEmpty()) {

            txtZonaActividade.setError("Introduce unha zona");
            return;
        }

        txtEstadoTempo.setText("Consultando tempo...");
        txtRecomendacionTempo.setText("Agarda un momento");

        new Thread(() -> {

            try {

                String zonaCodificada =
                        URLEncoder.encode(
                                zona,
                                "UTF-8"
                        );

                String urlGeocoding =
                        "https://geocoding-api.open-meteo.com/v1/search?name="
                                + zonaCodificada
                                + "&count=1&language=es&format=json";

                JSONObject respostaGeocoding =
                        new JSONObject(
                                lerUrl(urlGeocoding)
                        );

                if (!respostaGeocoding.has("results")) {

                    runOnUiThread(() -> {

                        txtEstadoTempo.setText("Zona non atopada");
                        txtRecomendacionTempo.setText("Proba con outra cidade");
                    });

                    return;
                }

                JSONArray resultados =
                        respostaGeocoding.getJSONArray("results");

                if (resultados.length() == 0) {

                    runOnUiThread(() -> {

                        txtEstadoTempo.setText("Zona non atopada");
                        txtRecomendacionTempo.setText("Proba con outra cidade");
                    });

                    return;
                }

                JSONObject cidade =
                        resultados.getJSONObject(0);

                double latitude =
                        cidade.getDouble("latitude");

                double longitude =
                        cidade.getDouble("longitude");

                String nomeCidade =
                        cidade.getString("name");

                String urlTempo =
                        "https://api.open-meteo.com/v1/forecast?latitude="
                                + latitude
                                + "&longitude="
                                + longitude
                                + "&current=temperature_2m,weather_code,wind_speed_10m,precipitation&timezone=auto";

                JSONObject respostaTempo =
                        new JSONObject(
                                lerUrl(urlTempo)
                        );

                JSONObject current =
                        respostaTempo.getJSONObject("current");

                double temperatura =
                        current.getDouble("temperature_2m");

                double vento =
                        current.getDouble("wind_speed_10m");

                double choiva =
                        current.getDouble("precipitation");

                int codigoTempo =
                        current.getInt("weather_code");

                String estado =
                        obterEstadoTempo(codigoTempo);

                String icono =
                        obterIconoTempo(codigoTempo);

                String recomendacion =
                        obterRecomendacion(
                                codigoTempo,
                                choiva,
                                vento
                        );

                String resumo =
                        estado
                                + " · "
                                + Math.round(temperatura)
                                + " ºC"
                                + " · Vento "
                                + Math.round(vento)
                                + " km/h"
                                + " · Choiva "
                                + choiva
                                + " mm";

                resumoTempoActual = resumo;

                runOnUiThread(() -> {

                    txtZonaActual.setText("📍 " + nomeCidade);
                    txtIconoTempo.setText(icono);
                    txtTemperatura.setText(Math.round(temperatura) + " ºC");
                    txtEstadoTempo.setText(estado);
                    txtDetallesTempo.setText(
                            "Vento: "
                                    + Math.round(vento)
                                    + " km/h · Choiva: "
                                    + choiva
                                    + " mm"
                    );
                    txtRecomendacionTempo.setText(recomendacion);
                });

            } catch (Exception e) {

                runOnUiThread(() -> {

                    txtEstadoTempo.setText("Non se puido consultar o tempo");
                    txtRecomendacionTempo.setText("Revisa a conexión ou a zona");
                });
            }

        }).start();
    }

    // Leer contenido dunha URL
    private String lerUrl(String urlTexto) throws Exception {

        URL url =
                new URL(urlTexto);

        HttpURLConnection conexion =
                (HttpURLConnection)
                        url.openConnection();

        conexion.setRequestMethod("GET");
        conexion.setConnectTimeout(8000);
        conexion.setReadTimeout(8000);

        BufferedReader lector =
                new BufferedReader(
                        new InputStreamReader(
                                conexion.getInputStream()
                        )
                );

        StringBuilder resposta =
                new StringBuilder();

        String linea;

        while ((linea = lector.readLine()) != null) {

            resposta.append(linea);
        }

        lector.close();
        conexion.disconnect();

        return resposta.toString();
    }

    // Convertir código meteorológico a texto
    private String obterEstadoTempo(int codigo) {

        if (codigo == 0) {
            return "Soleado";
        }

        if (codigo == 1 || codigo == 2) {
            return "Pouco nubrado";
        }

        if (codigo == 3) {
            return "Nubrado";
        }

        if (codigo == 45 || codigo == 48) {
            return "Néboa";
        }

        if ((codigo >= 51 && codigo <= 67)
                || (codigo >= 80 && codigo <= 82)) {
            return "Chuvia";
        }

        if (codigo >= 71 && codigo <= 77) {
            return "Neve";
        }

        if (codigo >= 95 && codigo <= 99) {
            return "Treboada";
        }

        return "Tempo variable";
    }

    // Convertir código meteorológico a icono
    private String obterIconoTempo(int codigo) {

        if (codigo == 0) {
            return "☀️";
        }

        if (codigo == 1 || codigo == 2) {
            return "🌤️";
        }

        if (codigo == 3) {
            return "☁️";
        }

        if (codigo == 45 || codigo == 48) {
            return "🌫️";
        }

        if ((codigo >= 51 && codigo <= 67)
                || (codigo >= 80 && codigo <= 82)) {
            return "🌧️";
        }

        if (codigo >= 71 && codigo <= 77) {
            return "❄️";
        }

        if (codigo >= 95 && codigo <= 99) {
            return "⛈️";
        }

        return "⛅";
    }

    // Recomendación para pasear
    private String obterRecomendacion(
            int codigo,
            double choiva,
            double vento
    ) {

        if (codigo >= 95 && codigo <= 99) {

            return "Mellor evitar o paseo agora";
        }

        if (choiva > 0.5) {

            return "Mellor paseo curto ou esperar";
        }

        if (vento > 35) {

            return "Hai bastante vento, paseo con precaución";
        }

        return "Bo momento para pasear";
    }

    // Guardar actividade
    private void guardarActividade() {

        if (mascotaSeleccionada == null) {

            Toast.makeText(
                    this,
                    "Selecciona unha mascota",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String tipo;

        if (txtTipoActividade.getText() != null) {

            tipo =
                    txtTipoActividade
                            .getText()
                            .toString()
                            .trim();

        } else {

            tipo = "";
        }

        String textoDuracion;

        if (txtDuracionActividade.getText() != null) {

            textoDuracion =
                    txtDuracionActividade
                            .getText()
                            .toString()
                            .trim();

        } else {

            textoDuracion = "";
        }

        String fecha;

        if (txtFechaActividade.getText() != null) {

            fecha =
                    txtFechaActividade
                            .getText()
                            .toString()
                            .trim();

        } else {

            fecha = "";
        }

        String zona;

        if (txtZonaActividade.getText() != null) {

            zona =
                    txtZonaActividade
                            .getText()
                            .toString()
                            .trim();

        } else {

            zona = "";
        }

        if (tipo.isEmpty()) {

            txtTipoActividade.setError("Selecciona o tipo");
            return;
        }

        if (textoDuracion.isEmpty()) {

            txtDuracionActividade.setError("Introduce a duración");
            return;
        }

        int duracion;

        try {

            duracion =
                    Integer.parseInt(textoDuracion);

        } catch (Exception e) {

            txtDuracionActividade.setError("Introduce unha duración válida");
            return;
        }

        if (duracion <= 0) {

            txtDuracionActividade.setError("A duración debe ser maior ca 0");
            return;
        }

        if (fecha.isEmpty()) {

            txtFechaActividade.setError("Introduce a data");
            return;
        }

        ActividadeDTO actividade =
                new ActividadeDTO(
                        mascotaSeleccionada.getId(),
                        SesionUsuario.obtenerUsuarioId(this),
                        tipo,
                        duracion,
                        fecha,
                        zona,
                        resumoTempoActual
                );

        actividadeDAO.insertar(actividade);

        SupabaseSyncManager.sincronizar(this);

        txtDuracionActividade.setText("");

        cargarHistorialActividade();

        Toast.makeText(
                this,
                "Actividade gardada correctamente",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Cargar historial de actividades
    private void cargarHistorialActividade() {

        if (mascotaSeleccionada == null) {

            txtHistorialActividade.setText("Selecciona unha mascota");
            return;
        }

        ArrayList<ActividadeDTO> actividades =
                new ArrayList<>(
                        actividadeDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        if (actividades.isEmpty()) {

            txtHistorialActividade.setText("Sen actividades rexistradas");
            return;
        }

        StringBuilder texto =
                new StringBuilder();

        for (int i = 0; i < actividades.size(); i++) {

            ActividadeDTO actividade =
                    actividades.get(i);

            texto.append(actividade.getFecha())
                    .append("  Actividade: ")
                    .append(actividade.getTipo())
                    .append(" - ")
                    .append(actividade.getDuracionMinutos())
                    .append(" min");

            if (actividade.getZona() != null
                    && !actividade.getZona().isEmpty()) {

                texto.append("\nZona: ")
                        .append(actividade.getZona());
            }

            if (actividade.getTiempoResumen() != null
                    && !actividade.getTiempoResumen().isEmpty()) {

                texto.append("\nTempo: ")
                        .append(actividade.getTiempoResumen());
            }

            if (i < actividades.size() - 1) {

                texto.append("\n\n");
            }
        }

        txtHistorialActividade.setText(
                texto.toString()
        );
    }
}