## Proyecto Integrador – Fintech académica con Java y Spring Boot

### Descripción general

Este proyecto consiste en el desarrollo de una aplicación académica de tipo **fintech** orientada a la gestión de usuarios, cuentas y movimientos monetarios. El objetivo principal fue construir una solución aplicando conceptos de **Programación Orientada a Objetos**, validación de reglas de negocio, separación por capas y exposición de una API HTTP con una interfaz web básica para interactuar con el sistema. La aplicación está desarrollada con **Java 17** y **Spring Boot 3.3.5**, e incorpora soporte para web, validaciones y entorno de desarrollo con recarga rápida.

La arquitectura actual se apoya en una separación clara entre **modelo de dominio**, **repositorios**, **servicios**, **controladores**, **DTOs/mappers**, **configuración** y **manejo global de errores**. La persistencia implementada en esta etapa es **en memoria**, lo cual permitió concentrar el esfuerzo en el modelado del dominio, la lógica de negocio y el consumo desde una interfaz web sencilla.

---

## Objetivo del proyecto

El propósito de este trabajo fue construir una base funcional y didáctica para una aplicación financiera simple, poniendo el foco en:

* modelado de entidades relevantes del dominio,
* encapsulamiento de reglas de negocio,
* validaciones de entrada,
* exposición de endpoints REST,
* navegación web básica,
* y organización del código con una estructura mantenible.

La solución intenta priorizar la comprensión de la arquitectura y el flujo completo del sistema antes de incorporar persistencia real, autenticación avanzada o despliegue productivo.

---

## Funcionalidades implementadas

### 1. Gestión de usuarios

El sistema permite registrar usuarios con los datos `nombre`, `apellido`, `email`, `password` y `dni`, y también validar credenciales para iniciar sesión mediante la API. Desde el frontend, el alta y el login se consumen contra los endpoints `/api/auth/register` y `/api/auth/login`.

### 2. Validación de datos de usuario

La entidad `Usuario` implementa validaciones de negocio para nombre, apellido, email, contraseña y DNI. En particular, se valida que nombre y apellido tengan un formato mínimo, que el email tenga estructura correcta, que la contraseña cumpla requisitos de complejidad y que el DNI tenga exactamente 8 dígitos. Además, los DTOs de request incorporan validaciones adicionales con Bean Validation.

### 3. Creación de cuentas

El sistema permite crear cuentas asociadas a un usuario existente mediante `POST /api/cuentas`. El request exige `usuarioId` válido y un `tipoCuenta` obligatorio. Actualmente existen tres tipos de cuenta: **caja de ahorro en pesos**, **en dólares** y **en euros**.

### 4. Manejo de saldos por moneda

Cada cuenta inicializa su saldo en cero y asigna automáticamente la moneda correcta según el tipo de cuenta elegido: `ARS`, `USD` o `EUR`. El modelo impide operar con una moneda distinta a la definida para esa cuenta, reforzando así la coherencia del dominio.

### 5. Depósitos y retiros

La API permite realizar depósitos y retiros mediante los endpoints `POST /api/cuentas/{cuentaId}/depositos` y `POST /api/cuentas/{cuentaId}/retiros`. Ambas operaciones exigen un monto positivo y una moneda válida. El dominio valida que no existan montos nulos, negativos o inconsistentes con la moneda de la cuenta, y en el caso del retiro también controla el saldo suficiente.
### 6. Registro de movimientos

Cada operación sobre una cuenta se representa mediante la clase `Movimiento`, que almacena un identificador propio, el tipo de movimiento, el monto operado, la fecha y la cuenta asociada. La API expone la consulta de movimientos por cuenta con `GET /api/cuentas/{cuentaId}/movimientos`.

### 7. Consulta de cuentas y saldos

La aplicación permite listar todas las cuentas, consultar una cuenta por ID y listar las cuentas asociadas a un usuario mediante:

* `GET /api/cuentas`
* `GET /api/cuentas/{cuentaId}`
* `GET /api/cuentas/usuario/{usuarioId}`.

### 8. Eliminación de cuentas

El sistema incluye la posibilidad de eliminar una cuenta mediante `DELETE /api/cuentas/{cuentaId}`. Esta funcionalidad está disponible tanto desde la API como desde la interfaz web.

### 9. Interfaz web básica

La aplicación cuenta con una capa web estática servida por Spring Boot desde `resources/static`. Actualmente incluye páginas para:

* login,
* registro,
* dashboard,
* gestión de cuentas,
* consulta de movimientos.
  La navegación se apoya en JavaScript del lado cliente y en llamadas `fetch` contra la API.

### 10. Operación desde modal emergente

En la pantalla de cuentas se incorporó un flujo de operación mediante **modal emergente**, donde el usuario puede seleccionar la cuenta, elegir el tipo de movimiento (depósito o retiro), ingresar el monto y confirmar la operación, todo desde la misma interfaz.

### 11. Visualización del historial

La vista de movimientos muestra los datos de la cuenta seleccionada y el historial completo de movimientos realizados sobre ella, con actualización manual desde la interfaz.

### 12. Manejo unificado de errores

El proyecto incluye un `GlobalExceptionHandler` que centraliza errores de validación, errores de negocio (`IllegalArgumentException`) y errores inesperados, devolviendo respuestas HTTP estructuradas con `timestamp`, `status`, `error`, `message`, `details` y `path`. El frontend interpreta esas respuestas para mostrar mensajes legibles al usuario.

---

## Enfoque técnico adoptado

Se adoptó un enfoque **incremental** y **didáctico**, priorizando primero el diseño del dominio y las reglas de negocio, luego la exposición de la API y finalmente una interfaz web funcional. En lugar de comenzar por una base de datos o por una solución de seguridad completa, se optó por una implementación con **repositorios en memoria**, lo que permitió validar el comportamiento del sistema con menor complejidad inicial y una curva de aprendizaje más razonable.

La decisión de separar el código en capas responde a una necesidad de orden y mantenibilidad:

* el **modelo** concentra el dominio,
* los **repositorios** abstraen el acceso a datos,
* los **servicios** orquestan la lógica,
* los **controladores** exponen la API,
* los **DTOs** y **mappers** desacoplan el contrato HTTP del modelo interno,
* y el **frontend estático** consume la API sin mezclar lógica de negocio en la vista.

---

## Decisiones de diseño y justificación

### Repositorios en memoria

Se eligió una persistencia en memoria para esta primera versión porque reduce la complejidad técnica inicial y permite enfocarse en el aprendizaje de arquitectura, POO y diseño de API antes de incorporar una base de datos real. Además, el uso de interfaces para los repositorios deja preparada la base para una futura migración a persistencia con JPA/Hibernate o MySQL sin reescribir toda la lógica de negocio.

### DTOs y validaciones

Se decidió utilizar DTOs de request y response para mantener más claro el contrato de la API y evitar exponer directamente el modelo completo. Esto también permitió agregar validaciones declarativas con Bean Validation para reforzar la robustez de entrada.

### Frontend estático en lugar de templates server-side

La capa web actual fue construida con HTML, CSS y JavaScript estático servido por Spring Boot. Esta decisión simplificó la integración con los `@RestController` ya existentes y permitió reutilizar directamente los endpoints JSON mediante `fetch`, sin necesidad de introducir una capa adicional con Thymeleaf o un framework frontend más complejo.

### Generación interna de IDs

La generación de identificadores se resolvió mediante secuencias en memoria con `AtomicInteger`. Esta elección es coherente con la ausencia de una base de datos en esta etapa y facilita mantener un flujo funcional completo mientras no exista un motor de persistencia que gestione claves primarias.

---

## Virtudes del proyecto

Este proyecto tiene varias fortalezas para su nivel de desarrollo actual.

La primera es que **modela correctamente conceptos centrales del dominio**: usuario, cuenta, dinero y movimiento no están tratados como simples estructuras anémicas, sino como objetos con reglas propias y validaciones incorporadas. Esto hace que el sistema sea más coherente y más cercano al paradigma orientado a objetos.

La segunda es la **separación por capas**, que mejora la legibilidad del código y deja una base razonable para futuras extensiones, como persistencia real, seguridad o testing más profundo. La existencia de interfaces de repositorio y su inyección desde configuración es una decisión saludable desde el punto de vista del desacoplamiento.

La tercera es que ya existe un **flujo funcional completo**, desde el registro y login hasta la creación de cuentas, la operación sobre saldos y la visualización de movimientos, todo accesible tanto por API como por interfaz web.

---

## Falencias y limitaciones actuales

Como toda primera versión, el proyecto también presenta limitaciones importantes que deben ser explicitadas.

La más relevante es que la persistencia es **volátil**: los datos viven únicamente en memoria. Esto implica que al reiniciar la aplicación se pierden usuarios, cuentas y movimientos, ya que no existe integración con una base de datos real ni dependencias de Spring Data/JPA en el proyecto actual.

Otra limitación importante es la **seguridad**. Actualmente la contraseña del usuario se almacena como `String` dentro del modelo y el frontend conserva el usuario autenticado en `localStorage`, lo cual es aceptable para una práctica académica inicial, pero no para un sistema real. En una evolución del proyecto, esto debería migrar a contraseñas hasheadas, autenticación segura y manejo de sesión o tokens.

También debe señalarse que el manejo monetario usa `double` en la clase `Dinero`. Aunque se aplican redondeos y validaciones, en un contexto financiero real sería más adecuado utilizar `BigDecimal` para evitar problemas de precisión numérica.

A nivel de producto, todavía no se implementan características avanzadas como:

* persistencia con base de datos,
* autenticación/autorización robusta,
* control de concurrencia,
* auditoría avanzada,
* despliegue productivo,
* ni endurecimiento general de seguridad.
  Estas ausencias son coherentes con el alcance formativo de la etapa actual, pero representan pasos naturales de evolución.

---

## Consideraciones académicas y transparencia sobre el proceso

La **primera entrega final** de este proyecto fue realizada con asistencia de **inteligencia artificial**, especialmente en la construcción de la API con Spring Boot y en la integración inicial de la capa web. Esta decisión no tuvo como fin reemplazar el aprendizaje, sino **acompañar un proceso de formación** en un área sobre la cual todavía no contaba con conocimientos suficientes para desarrollar una solución completa de forma autónoma.

Lejos de ocultarlo, considero importante dejarlo explícito: la asistencia de IA funcionó como una herramienta de apoyo para poder comprender estructura, flujo, integración y buenas prácticas básicas, y a partir de ello continuar iterando, corrigiendo, adaptando y entendiendo el proyecto en mayor profundidad. El objetivo principal fue aprender, no solo “hacer que funcione”.

---

## Estado actual

El proyecto se encuentra en una etapa **funcional académica**, con backend operativo, frontend básico navegable y reglas de negocio principales implementadas. En su estado actual permite demostrar un flujo completo de uso y una arquitectura razonablemente ordenada, aunque todavía requiere mejoras para aproximarse a una solución más robusta y cercana a un entorno real.

---

## Posibles mejoras futuras

Como evolución natural de este trabajo, los próximos pasos más razonables serían:

* reemplazar los repositorios en memoria por persistencia real con base de datos,
* migrar el manejo de contraseñas a hashing seguro,
* incorporar autenticación basada en sesión o JWT,
* mejorar el tratamiento de dinero utilizando `BigDecimal`,
* ampliar el testing automático,
* agregar documentación formal de endpoints,
* y reforzar la experiencia de usuario de la capa web.

---

## Ejecución local

La aplicación está configurada para ejecutarse con **Java 17** y expone el servidor en el puerto **8080**. El proyecto utiliza Maven y puede iniciarse localmente con el comando habitual de Spring Boot.


