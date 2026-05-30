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
PA[Pautas]
I[Invitacións]
end

Usuario <-->|xestiona| Mascota

Mascota --> A
Mascota --> AL
Mascota --> P
Mascota --> E
Mascota --> ME
ME --> PA

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

class Mascota{
+int id
+String nome
+String especie
+String raza
+String sexo
+LocalDate nacemento
+double peso
+String foto
}

class Usuario{
+int id
+String nome
+String email
+String foto
}

class UsuarioMascota{
+int id
+boolean propietario
}

class Invitacion{
+int id
+String estado
+LocalDateTime dataEnvio
}

class Alimentacion{
+int id
+LocalDateTime dataHora
+String tipoComida
+double cantidade
+String notas
}

class Actividade{
+int id
+LocalDateTime dataHora
+String tipo
+int duracion
+String notas
}

class Peso{
+int id
+LocalDateTime dataHora
+double valor
+String notas
}

class Evento{
+int id
+String titulo
+String tipo
+LocalDateTime dataHora
+String notas
}

class Medicacion{
+int id
+String nome
+LocalDate dataInicio
+LocalDate dataFin
+boolean recordatorioActivo
+String notas
}

class Pauta{
+int id
+String dose
+int frecuenciaHoras
+LocalDate dataInicio
+LocalDate dataFin
}

Mascota "1" --> "*" UsuarioMascota
Usuario "1" --> "*" UsuarioMascota

Mascota "1" --> "*" Invitacion
Usuario "1" --> "*" Invitacion

Mascota "1" --> "*" Alimentacion
Mascota "1" --> "*" Actividade
Mascota "1" --> "*" Peso
Mascota "1" --> "*" Evento
Mascota "1" --> "*" Medicacion

Medicacion "1" --> "*" Pauta
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

P -. herda .-> U

subgraph Funcións xerais
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
L([Consultar recomendacións IA])
end

subgraph Funcións propietario
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
U --- L

P --- M
P --- N
P --- O
P --- Q
P --- R
```

Todos os usuarios poden rexistrar e consultar información asociada ás mascotas ás que teñan acceso. O usuario propietario dispón ademais de permisos adicionais para administrar a mascota creada.

---

## Deseño de interfaces de usuario

Debido a que a interface da aplicación poderá evolucionar durante o desenvolvemento do proxecto, nesta fase non se inclúen mockups definitivos.

Inicialmente prevese a existencia das seguintes pantallas:

* Inicio de sesión e rexistro
* Pantalla principal de mascotas
* Detalle da mascota
* Alimentación
* Actividade
* Rexistro de peso
* Medicación
* Eventos e citas
* Invitacións
* Perfil de usuario
* Recomendacións IA

As capturas reais da aplicación incorporaranse unha vez implementadas.

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
IA[Sistema IA]
METEO[API meteorolóxica]
end

REPO --> AUTH
REPO --> DB
REPO --> STORAGE

REPO --> IA
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
IA[API IA]
METEO[API meteorolóxica]
end

Usuario --> APP

APP --> ROOM
APP --> AUTH
APP --> DB
APP --> STORAGE
APP --> IA
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