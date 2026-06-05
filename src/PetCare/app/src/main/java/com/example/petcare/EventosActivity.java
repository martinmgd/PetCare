package com.example.petcare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcare.adapter.EventoAdapter;
import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dto.EventoDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView rvEventos;
    private ImageButton btnAddEvento;
    private BottomNavigationView bottomNavigation;

    private TextView tvMesEventos;
    private GridLayout gridCalendarioEventos;

    private EventoDAO dao;
    private EventoAdapter miAdaptador;

    private Calendar calendarioActual;
    private int diaSeleccionado;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            EventoDTO evento =
                                    (EventoDTO) result.getData().getSerializableExtra("evento");

                            if (evento != null) {

                                dao.insertar(evento);

                                cargarCalendario();
                                cargarEventosDelDiaSeleccionado();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eventos);

        dao = new EventoDAO();

        // Inicializa o calendario coa data actual
        calendarioActual = Calendar.getInstance();
        diaSeleccionado = calendarioActual.get(Calendar.DAY_OF_MONTH);

        // Referencias XML
        rvEventos = findViewById(R.id.rvEventos);
        btnAddEvento = findViewById(R.id.btnAddEvento);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        tvMesEventos = findViewById(R.id.tvMesEventos);
        gridCalendarioEventos = findViewById(R.id.gridCalendarioEventos);

        // RecyclerView de eventos
        rvEventos.setLayoutManager(new LinearLayoutManager(this));

        // Carga inicial do calendario e dos eventos do día actual
        cargarCalendario();
        cargarEventosDelDiaSeleccionado();

        // Botón para engadir un novo evento
        btnAddEvento.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            EventosActivity.this,
                            AddEventoActivity.class
                    );

            launcher.launch(intent);
        });

        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_eventos);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // Inicio
            if (id == R.id.nav_inicio) {

                Intent intent =
                        new Intent(
                                EventosActivity.this,
                                HomeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Mascotas
            if (id == R.id.nav_mascotas) {

                Intent intent =
                        new Intent(
                                EventosActivity.this,
                                ListadoMascotasActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Eventos
            if (id == R.id.nav_eventos) {

                return true;
            }

            // Saúde
            if (id == R.id.nav_saude) {

                Intent intent =
                        new Intent(
                                EventosActivity.this,
                                SaudeActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            // Perfil
            if(id == R.id.nav_perfil){

                Intent intent =
                        new Intent(
                                EventosActivity.this,
                                PerfilActivity.class
                        );

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }

    // Carga o calendario do mes actual e marca os días con eventos
    private void cargarCalendario() {

        gridCalendarioEventos.removeAllViews();

        String[] diasSemana = {
                "L", "M", "M", "X", "V", "S", "D"
        };

        // Engadir os días da semana
        for (String diaSemana : diasSemana) {

            gridCalendarioEventos.addView(
                    crearTextoDiaSemana(diaSemana)
            );
        }

        int ano = calendarioActual.get(Calendar.YEAR);
        int mes = calendarioActual.get(Calendar.MONTH);

        String[] nomesMeses = {
                "Xaneiro", "Febreiro", "Marzo", "Abril",
                "Maio", "Xuño", "Xullo", "Agosto",
                "Setembro", "Outubro", "Novembro", "Decembro"
        };

        // Mostrar mes e ano seleccionados
        tvMesEventos.setText(
                nomesMeses[mes] + " " + ano
        );

        Calendar calendarioMes = Calendar.getInstance();

        calendarioMes.set(Calendar.YEAR, ano);
        calendarioMes.set(Calendar.MONTH, mes);
        calendarioMes.set(Calendar.DAY_OF_MONTH, 1);

        int primeiroDiaSemana =
                calendarioMes.get(Calendar.DAY_OF_WEEK);

        int ocosAntesDoDiaUn =
                (primeiroDiaSemana + 5) % 7;

        int diasDoMes =
                calendarioMes.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                );

        HashSet<Integer> diasConEventos =
                obterDiasConEventos(ano, mes);

        // Engadir ocos antes do día 1
        for (int i = 0; i < ocosAntesDoDiaUn; i++) {

            gridCalendarioEventos.addView(
                    crearTextoBaleiro()
            );
        }

        // Engadir os días do mes
        for (int dia = 1; dia <= diasDoMes; dia++) {

            boolean seleccionado =
                    dia == diaSeleccionado;

            boolean tenEventos =
                    diasConEventos.contains(dia);

            TextView txtDia =
                    crearTextoDia(
                            dia,
                            seleccionado,
                            tenEventos
                    );

            int diaFinal = dia;

            // Seleccionar un día do calendario
            txtDia.setOnClickListener(v -> {

                diaSeleccionado = diaFinal;

                cargarCalendario();
                cargarEventosDelDiaSeleccionado();
            });

            gridCalendarioEventos.addView(txtDia);
        }
    }

    // Obtén os días do mes que teñen eventos rexistrados
    private HashSet<Integer> obterDiasConEventos(int ano, int mes) {

        HashSet<Integer> diasConEventos =
                new HashSet<>();

        ArrayList<EventoDTO> eventos =
                dao.listar();

        for (EventoDTO evento : eventos) {

            String fecha = evento.getFecha();

            if (fecha != null) {

                String[] partes = fecha.split("/");

                if (partes.length == 3) {

                    int diaEvento =
                            Integer.parseInt(partes[0]);

                    int mesEvento =
                            Integer.parseInt(partes[1]) - 1;

                    int anoEvento =
                            Integer.parseInt(partes[2]);

                    if (anoEvento == ano &&
                            mesEvento == mes) {

                        diasConEventos.add(diaEvento);
                    }
                }
            }
        }

        return diasConEventos;
    }

    // Filtra e mostra os eventos do día seleccionado
    private void cargarEventosDelDiaSeleccionado() {

        ArrayList<EventoDTO> eventosFiltrados =
                new ArrayList<>();

        ArrayList<Integer> posicionesReais =
                new ArrayList<>();

        String fechaSeleccionada =
                String.format(
                        "%02d/%02d/%04d",
                        diaSeleccionado,
                        calendarioActual.get(Calendar.MONTH) + 1,
                        calendarioActual.get(Calendar.YEAR)
                );

        ArrayList<EventoDTO> eventos =
                dao.listar();

        for (int i = 0; i < eventos.size(); i++) {

            EventoDTO evento = eventos.get(i);

            if (evento.getFecha() != null
                    && evento.getFecha().equals(fechaSeleccionada)) {

                eventosFiltrados.add(evento);
                posicionesReais.add(i);
            }
        }

        miAdaptador =
                new EventoAdapter(
                        eventosFiltrados,
                        posicionesReais
                );

        rvEventos.setAdapter(miAdaptador);
    }

    // Crea a cabeceira cos días da semana
    private TextView crearTextoDiaSemana(String texto) {

        TextView textView = new TextView(this);

        textView.setText(texto);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#777777"));
        textView.setTypeface(null, Typeface.BOLD);

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;
        params.height = dp(34);
        params.columnSpec =
                GridLayout.spec(
                        GridLayout.UNDEFINED,
                        1f
                );

        textView.setLayoutParams(params);

        return textView;
    }

    // Crea unha cela baleira para aliñar correctamente os días
    private TextView crearTextoBaleiro() {

        TextView textView = new TextView(this);

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;
        params.height = dp(38);
        params.columnSpec =
                GridLayout.spec(
                        GridLayout.UNDEFINED,
                        1f
                );

        textView.setLayoutParams(params);

        return textView;
    }

    // Crea unha cela do calendario
    private TextView crearTextoDia(
            int dia,
            boolean seleccionado,
            boolean tenEventos
    ) {

        TextView textView = new TextView(this);

        // Mostrar un punto se existen eventos nese día
        if (tenEventos) {

            textView.setText(dia + "\n•");

        } else {

            textView.setText(
                    String.valueOf(dia)
            );
        }

        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
        textView.setClickable(true);

        // Destacar o día seleccionado
        if (seleccionado) {

            textView.setTextColor(Color.WHITE);
            textView.setTypeface(null, Typeface.BOLD);

            GradientDrawable fondoSeleccionado =
                    new GradientDrawable();

            fondoSeleccionado.setColor(
                    Color.parseColor("#8F61FF")
            );

            fondoSeleccionado.setCornerRadius(
                    dp(4)
            );

            textView.setBackground(
                    fondoSeleccionado
            );

        } else {

            textView.setTextColor(
                    Color.parseColor("#333333")
            );
        }

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;
        params.height = dp(38);

        params.columnSpec =
                GridLayout.spec(
                        GridLayout.UNDEFINED,
                        1f
                );

        params.setMargins(
                0,
                dp(2),
                0,
                dp(2)
        );

        textView.setLayoutParams(params);

        return textView;
    }

    // Converte unha medida en dp a píxeles
    private int dp(int valor) {

        return (int) (
                valor *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    // Actualiza o calendario e os eventos ao volver á pantalla
    @Override
    protected void onResume() {

        super.onResume();

        if (gridCalendarioEventos != null) {

            cargarCalendario();
            cargarEventosDelDiaSeleccionado();
        }
    }
}