# DESEÑO

## Guía do documento

1. [Modelo conceptual do dominio da aplicación](#modelo-conceptual-do-dominio-da-aplicación)
2. [Diagrama de clases](#diagrama-de-clases)
3. [Casos de uso](#casos-de-uso)
4. [Deseño de interfaces de usuario](#deseño-de-interfaces-de-usuario)
5. [Diagrama de Base de Datos](#diagrama-de-base-de-datos)
6. [Diagrama de compoñentes software](#diagrama-de-compoñentes-software)
7. [Diagrama de despregamento](#diagrama-de-despregamento)
8. [Calidade](#calidade)

---

## Deseño do proxecto

Nesta fase descríbese a estrutura xeral do proxecto PetCare, definindo os principais elementos do sistema, a organización dos compoñentes e os modelos necesarios para comprender o funcionamento da aplicación antes da implementación.

---

## Modelo conceptual do dominio da aplicación

O dominio da aplicación organízase tomando como elemento principal a mascota, xa que a maior parte das accións realizadas polos usuarios estarán asociadas a ela.

A partir desta entidade estrutúranse os diferentes rexistros relacionados coa actividade diaria, alimentación, medicación e colaboración entre usuarios.

```mermaid
flowchart TB

Usuario[Usuario]
Mascota[Mascota]

subgraph Rexistros
A[Actividade]
AL[Alimentación]
P[Peso]
E[Eventos]
ME[Medicación]
I[Invitacións]
end

Usuario <-->|xestiona| Mascota

Mascota --> A
Mascota --> AL
Mascota --> P
Mascota --> E
Mascota --> ME

Mascota --> I
Usuario --> I
```

Este modelo conceptual sitúa a mascota como entidade central do sistema. Ao seu redor organízanse os distintos rexistros relacionados co coidado diario, permitindo ademais a colaboración entre varios usuarios mediante invitacións.

---

## Diagrama de clases

A organización das clases permite representar os distintos elementos necesarios para o funcionamento da aplicación e definir a relación existente entre eles.

Esta estrutura servirá como base para a implementación das entidades e o almacenamento posterior da información.

```mermaid
classDiagram
direction TB

class Usuario{
+int id
+String supabaseId
+String nome
+String email
+String foto
}

class Mascota{
+int id
+String supabaseId
+String propietarioId
+String nome
+String especie
+String raza
+int idade
+double pesoActual
+String fechaNacemento
+String sexo
}

class UsuarioMascota{
+int id
+String usuarioId
+int mascotaId
+String rol
+String estado
}

class Invitacion{
+int id
+int mascotaId
+String nomeMascota
+String usuarioOrixeId
+String usuarioDestinoId
+String estado
}

class Comida{
+int id
+int mascotaId
+String fecha
+boolean almorzoComido
+boolean comidaComida
+boolean ceaComida
}

class Actividade{
+int id
+int mascotaId
+String usuarioId
+String tipo
+int duracionMinutos
+String fecha
+String zona
+String tempoResumo
}

class Peso{
+int id
+int mascotaId
+String usuarioId
+double peso
+String fecha
+String hora
}

class Evento{
+int id
+String usuarioId
+int mascotaId
+String titulo
+String descripcion
+String fecha
+String hora
+String tipo
+String mascota
}

class Vacina{
+int id
+int mascotaId
+String nome
+String fecha
+String notas
}

class Medicacion{
+int id
+int mascotaId
+String usuarioId
+String nome
+String dose
+String fechaInicio
+String fechaFin
+String notas
}

Usuario "1" --> "*" Mascota
Usuario "1" --> "*" UsuarioMascota
Usuario "1" --> "*" Invitacion
Usuario "1" --> "*" Actividade
Usuario "1" --> "*" Peso
Usuario "1" --> "*" Evento
Usuario "1" --> "*" Medicacion

Mascota "1" --> "*" UsuarioMascota
Mascota "1" --> "*" Invitacion
Mascota "1" --> "*" Comida
Mascota "1" --> "*" Actividade
Mascota "1" --> "*" Peso
Mascota "1" --> "*" Evento
Mascota "1" --> "*" Vacina
Mascota "1" --> "*" Medicacion
```

Este diagrama representa a organización das entidades principais da aplicación, definindo a estrutura de datos e as relacións necesarias para o funcionamento do sistema.

---

## Casos de uso

Os casos de uso permiten identificar as accións dispoñibles dentro da aplicación desde o punto de vista dos usuarios.

Ademais, facilitan distinguir os permisos adicionais asociados ao propietario da mascota respecto ao resto de usuarios.

```mermaid
flowchart LR

U[Usuario]
P[Usuario propietario]

P -.->|herda| U

subgraph FX["Funcións xerais"]
A([Rexistrarse])
B([Iniciar sesión])
C([Consultar mascota])
D([Aceptar invitación])
E([Rexeitar invitación])
F([Rexistrar alimentación])
G([Rexistrar actividade])
H([Rexistrar peso])
I([Rexistrar medicación])
J([Crear eventos])
K([Consultar historial])
end

subgraph FP["Funcións propietario"]
M([Crear mascota])
N([Modificar ficha mascota])
O([Eliminar mascota])
Q([Enviar invitación])
R([Retirar acceso usuario])
end

U --- A
U --- B
U --- C
U --- D
U --- E
U --- F
U --- G
U --- H
U --- I
U --- J
U --- K

P --- M
P --- N
P --- O
P --- Q
P --- R
```

Todos os usuarios poden rexistrar e consultar información asociada ás mascotas ás que teñan acceso. O usuario propietario dispón ademais de permisos adicionais para administrar a mascota creada.

---

## Deseño de interfaces de usuario

A continuación móstranse as principais pantallas implementadas na aplicación PetCare. Estas interfaces permiten realizar a xestión das mascotas, eventos, saúde, usuarios compartidos e copias de seguridade de forma sinxela e intuitiva.

### Autenticación

| Inicio de sesión | Rexistro |
|------------------|-----------|
| <img src="../img/pantalla_login.png" width="180"> | <img src="../img/pantalla_register.png" width="180"> |

O sistema incorpora pantallas independentes para o inicio de sesión e o rexistro de novos usuarios mediante autenticación xestionada por Supabase.

---

### Pantalla principal

| Inicio |
|---------|
| <img src="../img/pantalla_home.png" width="180"> |

A pantalla principal mostra un resumo das mascotas rexistradas e dos eventos pendentes, permitindo acceder rapidamente ás funcionalidades máis utilizadas da aplicación.

---

### Xestión de mascotas

| Listado de mascotas | Nova mascota | Ficha da mascota | Xestión de usuarios |
|---------------------|--------------|------------------|---------------------|
| <img src="../img/pantalla_mascotas.png" width="185"> | <img src="../img/pantalla_addmascotas.png" width="180"> | <img src="../img/pantalla_datosmascota.png" width="185"> | <img src="../img/pantalla_gestionusuarios.png" width="185"> |

Estas pantallas permiten crear novas mascotas, consultar a súa información detallada, modificar os seus datos e compartir o acceso con outros usuarios mediante invitacións.

---

### Xestión de eventos

| Eventos | Novo evento | Detalle do evento |
|----------|-------------|-------------------|
| <img src="../img/pantalla_eventos.png" width="180"> | <img src="../img/pantalla_addeventos.png" width="180"> | <img src="../img/pantallas_datosevento.png" width="180"> |

O módulo de eventos permite rexistrar citas veterinarias, vacinas, actividades ou calquera outro evento importante relacionado coa mascota.

---

### Módulo de saúde

| Saúde | Actividade | Peso |
|--------|------------|------|
| <img src="../img/pantalla_saude.png" width="180"> | <img src="../img/pantalla_actividade.png" width="180"> | <img src="../img/pantalla_peso.png" width="180"> |

| Vacinas | Medicación | Historial de saúde |
|----------|------------|-------------------|
| <img src="../img/pantalla_vacinas.png" width="180"> | <img src="../img/pantalla_medicacion.png" width="180"> | <img src="../img/pantalla_historialsaude.png" width="200"> |

O apartado de saúde reúne toda a información relacionada coa actividade física, evolución do peso, vacinas administradas, tratamentos médicos e historial sanitario da mascota.

---

### Perfil e copias de seguridade

| Perfil | Copias de seguridade |
|---------|----------------------|
| <img src="../img/pantalla_perfil.png" width="180"> | <img src="../img/pantalla_copiaseguridade.png" width="180"> |

Desde o perfil o usuario pode consultar os seus datos persoais, copiar o seu identificador único, pechar sesión e acceder ao sistema de copias de seguridade locais para restaurar información previamente gardada.

---

As interfaces foron desenvolvidas seguindo criterios de simplicidade, accesibilidade e facilidade de uso, empregando compoñentes de Material Design e unha identidade visual común baseada en tons violetas, mantendo unha experiencia coherente en toda a aplicación.

---

## Diagrama de Base de Datos

A estrutura da base de datos deseñouse co obxectivo de almacenar e organizar a información empregada pola aplicación, permitindo relacionar os distintos elementos do sistema de forma coherente.

As entidades definidas permiten xestionar información asociada ás mascotas, usuarios, rexistros diarios, eventos e colaboración entre distintos usuarios.

![Diagrama Base de Datos](../img/diagrama_bd.png)

A aplicación empregará unha arquitectura híbrida de almacenamento, utilizando SQLite/Room para a persistencia local dos datos no dispositivo e Supabase como servizo de sincronización, autenticación e almacenamento na nube.

Esta solución permitirá manter a información dispoñible incluso en ausencia de conexión, sincronizando automaticamente os cambios cando exista acceso á rede.

---

## Diagrama de compoñentes software

A aplicación distribúe as súas funcionalidades en diferentes módulos co obxectivo de separar responsabilidades e simplificar o desenvolvemento.

Esta organización permite incorporar novas funcionalidades no futuro sen afectar ao funcionamento xeral do sistema.

```mermaid
flowchart LR

subgraph Android
UI[Interfaces]
LOGICA[Lóxica]
REPO[Repositorios]
ROOM[SQLite / Room]

UI --> LOGICA
LOGICA --> REPO
REPO --> ROOM
end

subgraph Supabase
AUTH[Supabase Auth]
DB[Base datos]
STORAGE[Storage]
end

subgraph Externos
METEO[API meteorolóxica]
end

REPO --> AUTH
REPO --> DB
REPO --> STORAGE

REPO --> METEO
```

O sistema estrutúrase mediante unha arquitectura baseada en capas, separando interface, lóxica e almacenamento para facilitar o mantemento e a escalabilidade.

---

## Diagrama de despregamento

```mermaid
flowchart LR

Usuario[Dispositivo Android]

subgraph Aplicación
APP[App Android Java]
ROOM[SQLite / Room]
end

subgraph Supabase
AUTH[Supabase Auth]
DB[PostgreSQL]
STORAGE[Storage imaxes]
end

subgraph Externos
METEO[API meteorolóxica]
end

Usuario --> APP

APP --> ROOM
APP --> AUTH
APP --> DB
APP --> STORAGE
APP --> METEO
```

Este diagrama mostra a distribución física dos compoñentes da aplicación e a súa comunicación cos servizos externos empregados.

---

## Calidade

Co obxectivo de garantir un funcionamento correcto da aplicación PetCare estableceranse diferentes medidas orientadas a asegurar a estabilidade do sistema, a integridade da información e unha experiencia de uso adecuada.

### Seguimento do funcionamento

* Revisión periódica das funcionalidades implementadas.
* Comprobación do comportamento xeral da aplicación durante o desenvolvemento.
* Verificación do cumprimento dos requisitos definidos inicialmente.

### Validación do sistema

* Realización de probas sobre os distintos módulos da aplicación.
* Comprobación do correcto intercambio de datos entre compoñentes.
* Identificación e corrección de posibles incidencias detectadas.

### Mantemento do código

* Uso de Git e GitLab para o seguimento do desenvolvemento.
* Organización do código de forma estruturada e modular.
* Rexistro continuo dos cambios realizados no proxecto.

### Consistencia dos datos

* Sincronización entre almacenamento local mediante SQLite/Room e servizos na nube.
* Conservación da información incluso en situacións sen conexión.
* Verificación da coherencia dos datos sincronizados.

### Protección e acceso

* Autenticación de usuarios mediante Supabase.
* Validación dos datos introducidos na aplicación.
* Limitación do acceso ás mascotas segundo os permisos asignados.

### Documentación

* Actualización progresiva da documentación do proxecto.
* Uso de diagramas e modelos para representar a estrutura do sistema.
* Mantemento dunha descrición clara das decisións de deseño adoptadas.

---

## Ferramentas empregadas para os diagramas

A maior parte dos diagramas incluídos na documentación foron elaborados mediante [Mermaid](https://mermaid.js.org), empregando o editor [Mermaid Live Editor](https://mermaid.live) para a súa creación e validación.

Para o deseño do modelo de base de datos empregouse [DBDiagram](https://dbdiagram.io), ferramenta especializada na definición visual de estruturas relacionais e na representación das relacións entre entidades.

Ambas ferramentas permitiron xerar diagramas facilmente modificables e integrables na documentación do proxecto en formato Markdown.