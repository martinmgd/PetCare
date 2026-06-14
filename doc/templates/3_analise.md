# REQUIRIMENTOS DO SISTEMA

## Descrición xeral

PetCare é unha aplicación móbil orientada á xestión do coidado de mascotas. O seu obxectivo principal é facilitar aos usuarios o rexistro e consulta da información relevante dos seus animais, permitindo un seguimento máis organizado do seu estado e das tarefas diarias.

A aplicación está deseñada para un uso sinxelo e accesible desde dispositivos móbiles, integrando nun único sistema aspectos como a alimentación, a actividade diaria, as citas veterinarias ou o control do peso.

Como característica destacada, o sistema permitirá a xestión compartida das mascotas, posibilitando que diferentes usuarios poidan participar no seu coidado e manter información sincronizada dentro da aplicación.

---

## Funcionalidades

### Xestión de usuarios

* Rexistro de usuarios mediante correo electrónico e contrasinal
* Inicio de sesión
* Modificación dos datos persoais

### Xestión de mascotas

* Alta, modificación e eliminación de mascotas
* Rexistro de información básica (nome, idade, tipo ou raza)
* Asociación dunha mascota a un ou varios usuarios

### Xestión compartida

* Participación de varios usuarios na xestión dunha mesma mascota
* Rexistro de accións asociadas a un usuario e a unha data/hora
* Posibilidade de consultar quen realizou cada rexistro
* Consulta da información actualizada dentro da aplicación

### Xestión de eventos e citas

* Rexistro de citas veterinarias
* Control de vacinas, revisións e desparasitacións
* Creación de recordatorios

### Seguimento da actividade

* Rexistro da actividade diaria da mascota
* Control do tempo de paseo
* Consulta do historial de actividade ao longo do tempo

### Control de alimentación

* Rexistro das comidas diarias (mañá, mediodía e noite)
* Sistema de aviso entre usuarios para evitar duplicidade nas tarefas

### Control de saúde

* Rexistro do peso da mascota
* Seguimento da evolución física
* Rexistro de tratamentos ou medicacións

---

## Requirimentos non funcionais

### Rendemento

* A aplicación deberá funcionar de forma fluída en dispositivos Android actuais
* Os tempos de resposta serán reducidos nas operacións principais

### Seguridade

* Protección dos datos persoais dos usuarios
* Validación da información introducida
* Acceso controlado mediante autenticación

### Usabilidade

* Interface clara e intuitiva
* Navegación sinxela entre pantallas
* Deseño adaptado ao uso en dispositivos móbiles

### Fiabilidade

* Persistencia dos datos mediante almacenamento local e sincronización coa nube
* Funcionamento estable sen perda de información
* Sincronización da información entre usuarios asociados a unha mesma mascota
* Posibilidade de realizar copias de seguridade dos datos e restauralos en caso necesario

### Compatibilidade

* Compatible con versións recentes do sistema operativo Android

---

## Tipos de usuarios

### Usuario principal

* Pode xestionar mascotas
* Pode rexistrar e consultar información

### Usuario compartido

* Pode participar na xestión da mascota
* Pode introducir información
* Colabora no coidado diario

---

## Avaliación da viabilidade técnica do proxecto

### Hardware requerido

* Dispositivo móbil (smartphone Android) para o uso da aplicación
* Ordenador persoal para o desenvolvemento

### Software

* Android Studio como contorno de desenvolvemento
* Linguaxe de programación Java
* SQLite/Room como sistema principal de almacenamento e persistencia local da información
* Supabase como sistema de sincronización e almacenamento dos datos na nube

---

## Interfaces externos

### Interfaces de usuario

* Aplicación móbil composta por diferentes pantallas segundo a funcionalidade
* Navegación estruturada e accesible

### Interfaces hardware

* Uso de notificacións do dispositivo
* Posible uso de servizos de localización

### Interfaces software

* Comunicación cun sistema de almacenamento e sincronización de datos mediante Supabase
* Persistencia local da información mediante SQLite/Room
* Posible integración con servizos externos para a obtención de información (como datos meteorolóxicos)

---

## Análise de riscos e interesados

### Interesados

* Usuarios propietarios de mascotas
* Persoas que comparten o coidado dos animais

### Riscos

* Complexidade na implementación da xestión compartida
* Posibles erros durante o desenvolvemento
* Limitación de tempo para completar todas as funcionalidades
* Posible dificultade na usabilidade se o deseño non é adecuado
* Posible perda de datos en caso de erros na aplicación

### Medidas

* Priorización das funcionalidades principais
* Desenvolvemento progresivo
* Realización de probas continuas
* Deseño centrado no usuario

---

## Actividades

* Análise de requisitos do sistema
* Deseño da estrutura da aplicación
* Implementación do sistema de almacenamento e sincronización de datos
* Desenvolvemento das funcionalidades
* Realización de probas
* Elaboración da documentación

---

## Melloras futuras

* Integración con profesionais veterinarios
* Sistema máis avanzado de recomendación baseado en múltiples factores
* Integración de información sobre riscos para as mascotas en función da zona
* Posibilidade de exportar os datos rexistrados
* Notificacións intelixentes baseadas en hábitos de uso
* Incorporación de novas funcionalidades orientadas a mellorar o coidado e seguimento das mascotas
* Integración dun sistema de intelixencia artificial capaz de analizar datos de actividade, alimentación, peso, medicación e saúde da mascota para ofrecer recomendacións personalizadas
* Xeración de suxestións de actividade, alimentación e paseos en función do historial da mascota e das condicións meteorolóxicas da zona
