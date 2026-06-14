# Fase de produción

# Manual técnico e de administración

## Información relativa á instalación ou despregamento

PetCare é unha aplicación móbil desenvolvida para dispositivos Android. Para a súa utilización será necesario dispoñer dun teléfono intelixente ou tableta con Android 8.0 ou superior.

A aplicación pode instalarse mediante un arquivo APK xerado desde Android Studio.

PetCare emprega unha base de datos local SQLite mediante a biblioteca Room para almacenar a información das mascotas, eventos e datos de seguimento das mascotas.

Ademais, Supabase está destinado a proporcionar os servizos de autenticación, recuperación de contrasinais e sincronización da información entre distintos dispositivos.

O sistema segue unha arquitectura local first. Os datos almacénanse inicialmente no dispositivo mediante Room e sincronízanse automaticamente con Supabase cando existe conexión a internet, permitindo conservar a información actualizada entre diferentes dispositivos asociados ao mesmo usuario.

## Información relativa á administración do sistema

O código fonte da aplicación almacénase nun repositorio GitLab, permitindo manter un control de versións e recuperar versións anteriores en caso de erro.

Os datos almacénanse tanto localmente mediante Room como na nube mediante Supabase, permitindo recuperar a información en caso de cambio de dispositivo ou reinstalación da aplicación.

A xestión dos usuarios realízase mediante Supabase Authentication, encargándose da autenticación, recuperación de contrasinais e control das sesións activas.

Para garantir a seguridade, cada usuario só pode acceder á súa propia información mediante políticas RLS (Row Level Security) configuradas en Supabase.

## Información relativa ao mantemento do sistema

As incidencias detectadas serán corrixidas mediante novas versións da aplicación.

O control de versións realízase mediante Git, permitindo identificar e corrixir erros de forma rápida.

Os usuarios poderán comunicar incidencias mediante correo electrónico. Nunha versión futura poderá incorporarse unha opción específica dentro da propia aplicación.

# Manual de usuario

## Formación de usuarios

PetCare foi deseñada cunha interface sinxela e intuitiva, polo que non será necesaria formación específica para os usuarios.

As diferentes funcionalidades están organizadas mediante unha barra de navegación inferior que facilita o acceso aos distintos apartados da aplicación.

## Instrucións iniciais

Ao abrir a aplicación por primeira vez, o usuario poderá rexistrarse introducindo os seus datos persoais. Unha vez completado o rexistro, poderá iniciar sesión utilizando o seu correo electrónico e contrasinal.

A pantalla principal mostra un resumo das mascotas rexistradas e dos próximos eventos.

A barra de navegación inferior permite acceder aos seguintes apartados:

* Inicio.
* Mascotas.
* Eventos.
* Saúde.
* Perfil.

Desde o apartado Mascotas pódense engadir, modificar e eliminar mascotas.

Desde Eventos pódense crear eventos asociados ás mascotas e visualizalos nun calendario.

Desde Saúde pódense consultar e rexistrar datos relacionados coa actividade, peso, vacinas, medicación e historial das mascotas.

Desde Perfil pódense consultar os datos básicos do usuario e pechar sesión.

# FAQ

### Podo rexistrar máis dunha mascota?

Si. A aplicación permite rexistrar tantas mascotas como sexa necesario.

### Podo modificar os datos dunha mascota?

Si. Desde a ficha completa da mascota pódense editar os seus datos.

### Como podo recuperar o meu contrasinal?

A pantalla de inicio de sesión incorpora unha opción para recuperar o contrasinal mediante correo electrónico.

### Podo utilizar a aplicación en máis dun dispositivo?

Si. Os datos sincronízanse mediante Supabase, permitindo acceder á mesma información desde diferentes dispositivos asociados á mesma conta.

### Necesito conexión a internet para utilizar PetCare?

As funcións principais da aplicación poden utilizarse sen conexión grazas ao almacenamento local mediante Room. A conexión a internet será necesaria para a autenticación e sincronización dos datos coa nube.

### Podo compartir unha mascota con outro usuario?

Si. O propietario pode enviar unha invitación mediante o identificador único doutro usuario.

# Protección de datos de carácter persoal

PetCare almacena información relacionada cos usuarios e as súas mascotas, incluíndo datos identificativos, eventos e información de seguimento das mascotas.

A información almacénase de forma segura mediante SQLite/Room no dispositivo e sincronízase con Supabase utilizando conexións seguras HTTPS.

Cada usuario só pode acceder á súa propia información grazas ás políticas de seguridade configuradas en Supabase.

Os usuarios poden modificar ou eliminar os seus datos desde a propia aplicación, garantindo o cumprimento da normativa de protección de datos de carácter persoal.
