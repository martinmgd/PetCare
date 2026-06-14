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
* **backup**: creación e restauración das copias de seguridade da aplicación.
* **dao**: acceso e xestión dos datos da aplicación.
* **data**: configuración da base de datos e sesión de usuario.
* **dto**: clases que representan as entidades da aplicación.
* **supabase**: configuración da conexión con Supabase, autenticación e sincronización coa nube.
* **util**: clases auxiliares empregadas por diferentes módulos da aplicación.
* **view**: vistas personalizadas empregadas na aplicación, como o gráfico de evolución do peso.

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

### Autenticación de usuarios mediante Supabase

Para garantir que cada usuario dispoña dunha conta propia, implementouse un sistema de autenticación mediante Supabase Auth.

O rexistro realízase mediante correo electrónico e contrasinal, xerándose automaticamente un identificador único para cada usuario. Ademais, o sistema incorpora verificación de correo electrónico para validar a identidade do usuario antes de permitir o acceso á aplicación.

```java
supabase.auth.signUp(
        email,
        password
);
```

Esta funcionalidade permite asociar toda a información almacenada a un usuario concreto e facilita o uso compartido das mascotas entre distintos propietarios.

---

### Recuperación de contrasinal

Co fin de mellorar a experiencia de usuario, incorporouse un sistema de recuperación de contrasinal mediante correo electrónico.

No caso de esquecer o contrasinal, o usuario pode solicitar un enlace de recuperación que lle permitirá establecer un novo contrasinal de forma segura.

```java
supabase.auth.resetPasswordForEmail(
        email
);
```

---

### Sistema de copias de seguridade

Implementouse un sistema de copias de seguridade locais para protexer a información almacenada na aplicación.

As copias gárdanse en formato JSON e inclúen os datos asociados ás mascotas, eventos, rexistros de saúde e relacións entre usuarios.

```java
File backupFile =
        new File(
                directorio,
                nomeBackup
        );
```

A aplicación conserva automaticamente as tres copias máis recentes, eliminando as máis antigas para evitar un uso excesivo do almacenamento.

Tamén se incorporou unha pantalla específica que permite restaurar copias de seguridade previamente gardadas.

---

### Sincronización coa nube mediante Supabase

PetCare emprega unha arquitectura híbrida baseada en almacenamento local mediante Room e sincronización remota mediante Supabase.

Os datos permanecen dispoñibles incluso cando non existe conexión a internet. Unha vez recuperada a conexión, a aplicación sincroniza a información coa nube.

```java
supabase
        .from("mascotas")
        .insert(datos);
```

Esta aproximación combina a velocidade do almacenamento local coa seguridade e dispoñibilidade ofrecidas polos servizos na nube.

Actualmente sincronízanse os seguintes elementos:

* Usuarios
* Mascotas
* Eventos
* Alimentación
* Actividade
* Peso
* Vacinas
* Medicación
* Invitacións
* Relacións entre usuarios e mascotas compartidas

A sincronización realízase empregando identificadores únicos para cada rexistro, permitindo manter a consistencia da información entre a base de datos local e a almacenada en Supabase.

Deste xeito, os cambios realizados nun dispositivo poden ser recuperados posteriormente desde outro dispositivo asociado á mesma conta de usuario.

As copias almacenadas localmente permiten a recuperación inmediata dos datos por parte do usuario. As copias almacenadas en Supabase consérvanse como medida adicional de seguridade e para futuras ampliacións do sistema.

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