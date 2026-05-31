# Fase de implementación

## Entorno de desenvolvemento

Para o desenvolvemento de PetCare empregouse **Android Studio** como contorno de desenvolvemento integrado (IDE), utilizando a linguaxe **Java** para a programación da aplicación e **XML** para o deseño das interfaces gráficas.

Android Studio permitiu a creación, compilación e execución da aplicación tanto en emuladores Android como en dispositivos físicos durante as fases de desenvolvemento e probas.

---

## Tecnoloxías empregadas

As principais tecnoloxías utilizadas durante a implementación foron:

* Java
* Android Studio
* XML
* SQLite / Room
* Supabase
* RecyclerView
* Material Design Components
* Git e GitLab

SQLite e Room están previstos para o almacenamento local da información, mentres que Supabase utilizarase para a sincronización dos datos na nube e a xestión da autenticación dos usuarios.

---

## Estrutura do proxecto

A aplicación organízase en diferentes paquetes segundo a súa funcionalidade:

* **activities**: pantallas principais da aplicación.
* **adapters**: adaptadores utilizados polos RecyclerView.
* **dto**: clases que representan as entidades da aplicación.
* **dao**: acceso e xestión dos datos da aplicación.

Esta organización permite manter o código estruturado e facilitar o mantemento da aplicación durante o seu desenvolvemento.

---

## Interface de usuario

Para a construción das interfaces empregáronse ficheiros XML seguindo as recomendacións de Material Design.

As diferentes pantallas da aplicación foron deseñadas para ofrecer unha navegación sinxela e intuitiva, facilitando o acceso á información das mascotas e aos distintos apartados da aplicación. Ademais, os listados de mascotas e eventos implementáronse mediante RecyclerView, permitindo unha visualización eficiente da información e unha mellor experiencia de usuario.

---

### Selección de datas mediante DatePickerDialog

Co obxectivo de facilitar a introdución de datas e evitar erros de formato, implementouse un selector gráfico de calendario. O usuario pode escoller a data directamente desde unha interface visual proporcionada polo sistema Android.

```java
private void mostrarCalendario() {

    Calendar calendario = Calendar.getInstance();

    int ano = calendario.get(Calendar.YEAR);
    int mes = calendario.get(Calendar.MONTH);
    int dia = calendario.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog dialogoFecha =
            new DatePickerDialog(
                    AddEventoActivity.this,
                    (view, year, month, dayOfMonth) -> {

                        String fecha =
                                String.format(
                                        "%02d/%02d/%04d",
                                        dayOfMonth,
                                        month + 1,
                                        year
                                );

                        txtFechaEvento.setText(fecha);
                    },
                    ano,
                    mes,
                    dia
            );

    dialogoFecha.show();
}
```

---

### Selección de hora mediante TimePickerDialog

Para a introdución da hora dos eventos empregouse un selector visual baseado nun reloxo interactivo. Esta solución resulta máis intuitiva que a introdución manual da hora e permite seleccionar tanto a hora como os minutos de forma sinxela.

```java
private void mostrarRelojHora() {

    Calendar calendario = Calendar.getInstance();

    int horaActual = calendario.get(Calendar.HOUR_OF_DAY);
    int minutoActual = calendario.get(Calendar.MINUTE);

    TimePickerDialog dialogoHora =
            new TimePickerDialog(
                    AddEventoActivity.this,
                    (view, hourOfDay, minute) -> {

                        String horaSeleccionada =
                                String.format(
                                        "%02d:%02d",
                                        hourOfDay,
                                        minute
                                );

                        txtHoraEvento.setText(horaSeleccionada);
                    },
                    horaActual,
                    minutoActual,
                    true
            );

    dialogoHora.show();
}
```

---

### Validación dos formularios

Antes de gardar un evento realízase unha validación dos campos obrigatorios. Desta forma impídese que o usuario poida crear rexistros incompletos e ofrécese unha mensaxe de erro directamente no campo correspondente.

```java
if (titulo.isEmpty()) {

    txtTituloEvento.setError("Introduce o título do evento");
    return;
}

if (fecha.isEmpty()) {

    txtFechaEvento.setError("Introduce a data");
    return;
}

if (hora.isEmpty()) {

    txtHoraEvento.setError("Introduce a hora");
    return;
}
```

---

### Comunicación entre Activities

Para transferir información entre pantallas empregouse o mecanismo de resultados proporcionado por Android. Unha vez creado o evento, este envíase á Activity anterior mediante un Intent.

```java
Intent intent = new Intent();

intent.putExtra("evento", evento);

setResult(RESULT_OK, intent);

finish();
```

Este sistema permite intercambiar información entre Activities sen necesidade de almacenar previamente os datos na base de datos.

---

## Compilación e execución

Para executar o proxecto é necesario dispoñer de:

* Android Studio.
* JDK 17 ou superior.
* Android SDK actualizado.
* Dispositivo Android físico ou emulador.

A compilación da aplicación realízase directamente desde Android Studio mediante:

```text
Run > Run app
```

ou mediante o atallo:

```text
Shift + F10
```

Unha vez compilada, a aplicación instálase automaticamente no dispositivo seleccionado.

