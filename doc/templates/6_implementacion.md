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

SQLite e Room empréganse como sistema principal de almacenamento local da información, permitindo o funcionamento da aplicación sen conexión a internet. Supabase intégrase como servizo de sincronización, autenticación e almacenamento dos datos na nube.

---

## Estrutura do proxecto

A aplicación organízase en diferentes paquetes segundo a súa funcionalidade:

* **activities**: pantallas principais da aplicación.
* **adapters**: adaptadores utilizados polos RecyclerView.
* **dto**: clases que representan as entidades da aplicación.
* **dao**: acceso e xestión dos datos da aplicación.
* **data**: configuración da base de datos e sesión de usuario.
* **util**: clases auxiliares empregadas por diferentes módulos da aplicación.

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

### Persistencia local mediante Room

Co obxectivo de garantir a conservación da información incluso cando o dispositivo non dispón de conexión a internet, empregouse Room como capa de acceso á base de datos SQLite. Mediante a utilización de interfaces DAO é posible realizar operacións de inserción, actualización e consulta dos datos de forma sinxela e estruturada.

```java
@Dao
public interface MascotaDAO {

    @Insert
    long insertar(MascotaDTO mascota);

    @Query("SELECT * FROM mascotas")
    List<MascotaDTO> listar();
}
```

Este mecanismo permite abstraer o acceso á base de datos, facilitando o mantemento do código e reducindo a complexidade das operacións de persistencia.

---

### Configuración da base de datos Room

A base de datos local configúrase mediante unha instancia única (Singleton), garantindo que toda a aplicación utilice a mesma conexión e evitando a creación innecesaria de múltiples instancias.

```java
instancia =
        Room.databaseBuilder(
                        context.getApplicationContext(),
                        PetCareDatabase.class,
                        "petcare_db"
                )
                .fallbackToDestructiveMigration()
                .build();
```

Esta configuración permite crear automaticamente a base de datos local da aplicación e xestionar futuras modificacións na súa estrutura durante o desenvolvemento do proxecto.

---

### Comunicación entre Activities

Para transferir información entre pantallas empregouse o mecanismo de resultados proporcionado por Android. Unha vez creado o evento, este envíase á Activity anterior mediante un Intent.

```java
Intent intent = new Intent();

intent.putExtra("evento", evento);

setResult(RESULT_OK, intent);

finish();
```

Este sistema permite intercambiar información entre Activities de forma directa. Posteriormente, os datos poden almacenarse na base de datos local mediante Room para garantir a súa persistencia.

---

## Compilación e execución

Para executar o proxecto é necesario dispoñer de:

* Android Studio.
* JDK 11 ou superior.
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

