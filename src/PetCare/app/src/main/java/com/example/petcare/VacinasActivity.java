package com.example.petcare;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dao.EventoDAO;
import com.example.petcare.dao.MascotaDAO;
import com.example.petcare.dao.UsuarioMascotaDAO;
import com.example.petcare.dao.VacinaDAO;
import com.example.petcare.data.PetCareDatabase;
import com.example.petcare.data.SesionUsuario;
import com.example.petcare.dto.EventoDTO;
import com.example.petcare.dto.MascotaDTO;
import com.example.petcare.dto.UsuarioMascotaDTO;
import com.example.petcare.dto.VacinaDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class VacinasActivity extends AppCompatActivity {

    private Toolbar toolbarVacinas;

    private MaterialAutoCompleteTextView txtMascotaVacina;
    private TextInputEditText txtNomeVacina;
    private TextInputEditText txtFechaVacina;
    private TextInputEditText txtNotasVacina;

    private MaterialButton btnGuardarVacina;

    private TextView txtHistorialVacinas;

    private MascotaDAO mascotaDAO;
    private UsuarioMascotaDAO usuarioMascotaDAO;
    private VacinaDAO vacinaDAO;
    private EventoDAO eventoDAO;

    private ArrayList<MascotaDTO> listaMascotas;
    private MascotaDTO mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vacinas);

        // Base de datos
        PetCareDatabase db =
                PetCareDatabase.getInstance(this);

        mascotaDAO = db.mascotaDAO();
        usuarioMascotaDAO = db.usuarioMascotaDAO();
        vacinaDAO = db.vacinaDAO();
        eventoDAO = db.eventoDAO();

        // Referencias XML
        toolbarVacinas = findViewById(R.id.toolbarVacinas);

        txtMascotaVacina = findViewById(R.id.txtMascotaVacina);
        txtNomeVacina = findViewById(R.id.txtNomeVacina);
        txtFechaVacina = findViewById(R.id.txtFechaVacina);
        txtNotasVacina = findViewById(R.id.txtNotasVacina);

        btnGuardarVacina = findViewById(R.id.btnGuardarVacina);

        txtHistorialVacinas = findViewById(R.id.txtHistorialVacinas);

        // Botón volver
        toolbarVacinas.setNavigationOnClickListener(v -> finish());

        // Lista de mascotas accesibles
        listaMascotas = new ArrayList<>();

        cargarMascotasPermitidas();
        configurarSelectorMascota();
        configurarSelectorFecha();

        // Guardar vacina
        btnGuardarVacina.setOnClickListener(v -> guardarVacina());
    }

    // Cargar mascotas propias y compartidas aceptadas
    private void cargarMascotasPermitidas() {

        String usuarioActualId =
                SesionUsuario.obtenerUsuarioId(this);

        listaMascotas.clear();

        // Mascotas propias
        listaMascotas.addAll(
                mascotaDAO.listarPorPropietario(usuarioActualId)
        );

        // Mascotas compartidas aceptadas
        ArrayList<UsuarioMascotaDTO> relaciones =
                new ArrayList<>(
                        usuarioMascotaDAO.listarAceptadasPorUsuario(
                                usuarioActualId
                        )
                );

        for (UsuarioMascotaDTO relacion : relaciones) {

            MascotaDTO mascota =
                    mascotaDAO.obtenerPorId(
                            relacion.getMascotaId()
                    );

            if (mascota != null) {

                agregarMascotaSiNoExiste(mascota);
            }
        }
    }

    // Evitar mascotas duplicadas
    private void agregarMascotaSiNoExiste(MascotaDTO mascota) {

        boolean existe = false;

        for (MascotaDTO mascotaLista : listaMascotas) {

            if (mascotaLista.getId() == mascota.getId()) {

                existe = true;
                break;
            }
        }

        if (!existe) {

            listaMascotas.add(mascota);
        }
    }

    // Configurar selector de mascota
    private void configurarSelectorMascota() {

        ArrayList<String> nombresMascotas =
                new ArrayList<>();

        for (MascotaDTO mascota : listaMascotas) {

            nombresMascotas.add(
                    mascota.getNombre()
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombresMascotas
                );

        txtMascotaVacina.setAdapter(adapter);

        txtMascotaVacina.setFocusable(false);
        txtMascotaVacina.setClickable(true);
        txtMascotaVacina.setCursorVisible(false);

        txtMascotaVacina.setOnItemClickListener((parent, view, position, id) -> {

            mascotaSeleccionada =
                    listaMascotas.get(position);

            cargarHistorialVacinas();
        });

        if (listaMascotas.isEmpty()) {

            txtHistorialVacinas.setText("Non hai mascotas dispoñibles");

            Toast.makeText(
                    this,
                    "Non hai mascotas dispoñibles",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            mascotaSeleccionada =
                    listaMascotas.get(0);

            txtMascotaVacina.setText(
                    mascotaSeleccionada.getNombre(),
                    false
            );

            cargarHistorialVacinas();
        }
    }

    // Configurar selector de fecha
    private void configurarSelectorFecha() {

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

        txtFechaVacina.setText(fechaActual);

        txtFechaVacina.setFocusable(false);
        txtFechaVacina.setClickable(true);
        txtFechaVacina.setCursorVisible(false);

        txtFechaVacina.setOnClickListener(v -> mostrarCalendario());
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
                        VacinasActivity.this,
                        (view, year, month, dayOfMonth) -> {

                            String fecha =
                                    String.format(
                                            "%02d/%02d/%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                    );

                            txtFechaVacina.setText(fecha);
                        },
                        ano,
                        mes,
                        dia
                );

        dialogoFecha.show();
    }

    // Guardar vacina
    private void guardarVacina() {

        if (mascotaSeleccionada == null) {

            Toast.makeText(
                    this,
                    "Selecciona unha mascota",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String nome;

        if (txtNomeVacina.getText() != null) {

            nome =
                    txtNomeVacina
                            .getText()
                            .toString()
                            .trim();

        } else {

            nome = "";
        }

        String fecha;

        if (txtFechaVacina.getText() != null) {

            fecha =
                    txtFechaVacina
                            .getText()
                            .toString()
                            .trim();

        } else {

            fecha = "";
        }

        String notas;

        if (txtNotasVacina.getText() != null) {

            notas =
                    txtNotasVacina
                            .getText()
                            .toString()
                            .trim();

        } else {

            notas = "";
        }

        if (nome.isEmpty()) {

            txtNomeVacina.setError("Introduce o nome da vacina");
            return;
        }

        if (fecha.isEmpty()) {

            txtFechaVacina.setError("Introduce a data");
            return;
        }

        VacinaDTO vacina =
                new VacinaDTO(
                        mascotaSeleccionada.getId(),
                        nome,
                        fecha,
                        notas
                );

        vacinaDAO.insertar(vacina);

        crearEventoVacina(
                nome,
                fecha,
                notas
        );

        txtNomeVacina.setText("");
        txtNotasVacina.setText("");

        cargarHistorialVacinas();

        Toast.makeText(
                this,
                "Vacina gardada correctamente",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Crear evento automático para que la vacuna aparezca en el calendario
    private void crearEventoVacina(
            String nome,
            String fecha,
            String notas
    ) {

        EventoDTO evento =
                new EventoDTO(
                        nome,
                        notas,
                        fecha,
                        "09:00",
                        "Vacuna",
                        mascotaSeleccionada.getNombre()
                );

        evento.setUsuarioId(
                SesionUsuario.obtenerUsuarioId(this)
        );

        evento.setMascotaId(
                mascotaSeleccionada.getId()
        );

        evento.setFechaActualizacion(
                System.currentTimeMillis()
        );

        evento.setPendienteSync(true);

        eventoDAO.insertar(evento);
    }

    // Cargar historial de vacinas da mascota seleccionada
    private void cargarHistorialVacinas() {

        if (mascotaSeleccionada == null) {

            txtHistorialVacinas.setText("Selecciona unha mascota");
            return;
        }

        ArrayList<VacinaDTO> vacinas =
                new ArrayList<>(
                        vacinaDAO.listarPorMascota(
                                mascotaSeleccionada.getId()
                        )
                );

        if (vacinas.isEmpty()) {

            txtHistorialVacinas.setText("Sen vacinas rexistradas");
            return;
        }

        StringBuilder texto =
                new StringBuilder();

        for (int i = 0; i < vacinas.size(); i++) {

            VacinaDTO vacina =
                    vacinas.get(i);

            texto.append(vacina.getFecha())
                    .append("  Vacuna: ")
                    .append(vacina.getNombre());

            if (vacina.getNotas() != null
                    && !vacina.getNotas().isEmpty()) {

                texto.append("\n")
                        .append(vacina.getNotas());
            }

            if (i < vacinas.size() - 1) {

                texto.append("\n\n");
            }
        }

        txtHistorialVacinas.setText(
                texto.toString()
        );
    }
}