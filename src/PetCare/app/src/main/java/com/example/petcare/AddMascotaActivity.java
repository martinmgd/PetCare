package com.example.petcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.petcare.dto.MascotaDTO;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Calendar;

public class AddMascotaActivity extends AppCompatActivity {

    EditText txtNombre;
    EditText txtEspecie;
    EditText txtRaza;
    EditText txtFechaNacimiento;

    MaterialAutoCompleteTextView txtSexo;

    Button btnGuardarMascota;

    Toolbar toolbarAddMascota;

    boolean editar;
    int posicion;
    MascotaDTO mascotaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_mascota);

        // Referencias
        toolbarAddMascota = findViewById(R.id.toolbarAddMascota);

        txtNombre = findViewById(R.id.txtNombre);
        txtEspecie = findViewById(R.id.txtEspecie);
        txtRaza = findViewById(R.id.txtRaza);
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
        txtSexo = findViewById(R.id.txtSexo);

        btnGuardarMascota = findViewById(R.id.btnGuardarMascota);

        // Recibir datos
        editar = getIntent().getBooleanExtra("editar", false);
        posicion = getIntent().getIntExtra("posicion", -1);
        mascotaEditar = (MascotaDTO) getIntent().getSerializableExtra("mascota");

        if (editar) {
            toolbarAddMascota.setTitle("Editar mascota");
        } else {
            toolbarAddMascota.setTitle("Nova mascota");
        }

        // Botón de volver da barra superior
        toolbarAddMascota.setNavigationOnClickListener(v -> finish());

        // Configuración da data de nacemento e do despregable de sexo
        configurarSelectorFechaNacimiento();
        configurarSelectorSexo();

        // Cargar datos se estamos editando
        if (editar && mascotaEditar != null) {

            txtNombre.setText(mascotaEditar.getNombre());
            txtEspecie.setText(mascotaEditar.getEspecie());
            txtRaza.setText(mascotaEditar.getRaza());
            txtFechaNacimiento.setText(mascotaEditar.getFechaNacimiento());
            txtSexo.setText(mascotaEditar.getSexo(), false);
        }

        // Guardar mascota
        btnGuardarMascota.setOnClickListener(view -> guardarMascota());
    }

    // Configura o campo de data de nacemento para abrir un selector personalizado
    private void configurarSelectorFechaNacimiento() {

        txtFechaNacimiento.setFocusable(false);
        txtFechaNacimiento.setClickable(true);
        txtFechaNacimiento.setCursorVisible(false);

        txtFechaNacimiento.setOnClickListener(view -> mostrarSelectorFechaNacimiento());
    }

    // Mostra un selector de ano, mes e día para evitar navegar mes a mes
    private void mostrarSelectorFechaNacimiento() {

        Calendar fechaActual = Calendar.getInstance();

        int anoActual = fechaActual.get(Calendar.YEAR);
        int mesActual = fechaActual.get(Calendar.MONTH) + 1;
        int diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(30, 20, 30, 10);

        NumberPicker pickerDia = new NumberPicker(this);
        NumberPicker pickerMes = new NumberPicker(this);
        NumberPicker pickerAno = new NumberPicker(this);

        pickerAno.setMinValue(anoActual - 30);
        pickerAno.setMaxValue(anoActual);
        pickerAno.setValue(anoActual);

        String[] nomesMeses = {
                "Xan", "Feb", "Mar", "Abr",
                "Mai", "Xuñ", "Xul", "Ago",
                "Set", "Out", "Nov", "Dec"
        };

        pickerMes.setMinValue(1);
        pickerMes.setMaxValue(12);
        pickerMes.setDisplayedValues(nomesMeses);
        pickerMes.setValue(mesActual);

        pickerDia.setMinValue(1);
        pickerDia.setMaxValue(obterDiasDoMes(anoActual, mesActual));
        pickerDia.setValue(diaActual);

        NumberPicker.OnValueChangeListener actualizarDias =
                (numberPicker, oldValue, newValue) -> {

                    int anoSeleccionado = pickerAno.getValue();
                    int mesSeleccionado = pickerMes.getValue();
                    int maxDias = obterDiasDoMes(anoSeleccionado, mesSeleccionado);

                    pickerDia.setMaxValue(maxDias);

                    if (pickerDia.getValue() > maxDias) {
                        pickerDia.setValue(maxDias);
                    }
                };

        pickerAno.setOnValueChangedListener(actualizarDias);
        pickerMes.setOnValueChangedListener(actualizarDias);

        layout.addView(pickerDia);
        layout.addView(pickerMes);
        layout.addView(pickerAno);

        AlertDialog.Builder dialogo =
                new AlertDialog.Builder(this);

        dialogo.setTitle("Selecciona a data de nacemento");
        dialogo.setView(layout);

        dialogo.setPositiveButton("Aceptar", (dialogInterface, i) -> {

            int dia = pickerDia.getValue();
            int mes = pickerMes.getValue();
            int ano = pickerAno.getValue();

            if (dataPosteriorActual(dia, mes, ano)) {

                Toast.makeText(
                        AddMascotaActivity.this,
                        "A data non pode ser futura",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            String fecha =
                    String.format(
                            "%02d/%02d/%04d",
                            dia,
                            mes,
                            ano
                    );

            txtFechaNacimiento.setText(fecha);
        });

        dialogo.setNegativeButton("Cancelar", null);

        dialogo.show();
    }

    // Obtén o número de días dun mes concreto tendo en conta os anos bisestos
    private int obterDiasDoMes(int ano, int mes) {

        Calendar calendario = Calendar.getInstance();

        calendario.set(Calendar.YEAR, ano);
        calendario.set(Calendar.MONTH, mes - 1);
        calendario.set(Calendar.DAY_OF_MONTH, 1);

        return calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    // Comproba se a data seleccionada é posterior á data actual
    private boolean dataPosteriorActual(int dia, int mes, int ano) {

        Calendar fechaSeleccionada = Calendar.getInstance();

        fechaSeleccionada.set(Calendar.YEAR, ano);
        fechaSeleccionada.set(Calendar.MONTH, mes - 1);
        fechaSeleccionada.set(Calendar.DAY_OF_MONTH, dia);
        fechaSeleccionada.set(Calendar.HOUR_OF_DAY, 0);
        fechaSeleccionada.set(Calendar.MINUTE, 0);
        fechaSeleccionada.set(Calendar.SECOND, 0);
        fechaSeleccionada.set(Calendar.MILLISECOND, 0);

        Calendar fechaActual = Calendar.getInstance();

        fechaActual.set(Calendar.HOUR_OF_DAY, 0);
        fechaActual.set(Calendar.MINUTE, 0);
        fechaActual.set(Calendar.SECOND, 0);
        fechaActual.set(Calendar.MILLISECOND, 0);

        return fechaSeleccionada.after(fechaActual);
    }

    // Configura o despregable de sexo da mascota
    private void configurarSelectorSexo() {

        String[] sexos = {
                "Macho",
                "Femia"
        };

        ArrayAdapter<String> adapterSexo =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        sexos
                );

        txtSexo.setAdapter(adapterSexo);

        txtSexo.setFocusable(false);
        txtSexo.setClickable(true);
        txtSexo.setCursorVisible(false);
    }

    // Calcula a idade da mascota a partir da data de nacemento
    private int calcularEdad(String fechaNacimiento) {

        String[] datosFecha = fechaNacimiento.split("/");

        int dia = Integer.parseInt(datosFecha[0]);
        int mes = Integer.parseInt(datosFecha[1]);
        int ano = Integer.parseInt(datosFecha[2]);

        Calendar fechaActual = Calendar.getInstance();

        int edad = fechaActual.get(Calendar.YEAR) - ano;

        int mesActual = fechaActual.get(Calendar.MONTH) + 1;
        int diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);

        if (mesActual < mes || (mesActual == mes && diaActual < dia)) {

            edad--;
        }

        return edad;
    }

    // Recolle os datos, valida o formulario e devolve a mascota creada ou editada
    private void guardarMascota() {

        String nombre =
                txtNombre.getText() != null
                        ? txtNombre.getText().toString().trim()
                        : "";

        String especie =
                txtEspecie.getText() != null
                        ? txtEspecie.getText().toString().trim()
                        : "";

        String raza =
                txtRaza.getText() != null
                        ? txtRaza.getText().toString().trim()
                        : "";

        String fechaNacimiento =
                txtFechaNacimiento.getText() != null
                        ? txtFechaNacimiento.getText().toString().trim()
                        : "";

        String sexo =
                txtSexo.getText() != null
                        ? txtSexo.getText().toString().trim()
                        : "";

        // Validar datos
        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty()
                || fechaNacimiento.isEmpty() || sexo.isEmpty()) {

            Toast.makeText(
                    AddMascotaActivity.this,
                    "Cubre todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int edad;

        try {
            edad = calcularEdad(fechaNacimiento);
        } catch (Exception e) {

            Toast.makeText(
                    AddMascotaActivity.this,
                    "Introduce unha data de nacemento válida",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Crear mascota coa idade calculada automaticamente
        MascotaDTO mascota = new MascotaDTO(nombre, especie, raza, edad);

        mascota.setFechaNacimiento(fechaNacimiento);
        mascota.setSexo(sexo);

        // Enviar datos á Activity anterior
        Intent intent = new Intent();

        intent.putExtra("mascota", mascota);
        intent.putExtra("posicion", posicion);
        intent.putExtra("editar", editar);

        setResult(RESULT_OK, intent);

        finish();
    }
}