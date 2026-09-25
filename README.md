# Proyecto S3 · Guía de ejecución paso a paso

Este documento explica cómo ejecutar el proyecto completo, tanto **en local** como **con Docker Compose**.

## 1. ¿Qué contiene el proyecto?

El repositorio está compuesto por 5 microservicios Spring Boot:

- `auth-service` → autenticación y validación de token
- `estudiantes-service` → gestión de estudiantes
- `cursos-service` → gestión de cursos, con cupo máximo
- `matriculas-service` → gestión de matrículas
- `calificaciones-service` → notas por corte y promedio ponderado

El proyecto ya incluye las cuatro extensiones del proyecto final (E1–E4). Cada una tiene una guía de análisis
paso a paso en `instrucciones/` (ver la sección 15.1).

## 2. Requisitos previos

Antes de ejecutar el proyecto, verifica que tengas instalado:

- **Java 21**
- **Maven 3.9+**
- **Docker Desktop** o Docker Engine + Docker Compose
- Un IDE como IntelliJ IDEA o VS Code

Comandos para validar el entorno:

```bash
java -version
mvn -version
docker --version
docker compose version
```

## 3. Estructura general del proyecto

```text
proyecto-s3/
├── auth-service/
├── estudiantes-service/
├── cursos-service/
├── matriculas-service/
├── calificaciones-service/
├── docker-compose.yml
├── index.html
└── instrucciones/
```

## 4. Puertos de los servicios

| Servicio | Puerto |
|---|---:|
| auth-service | 8081 |
| estudiantes-service | 8082 |
| cursos-service | 8083 |
| matriculas-service | 8084 |
| calificaciones-service | 8085 |

## 5. Ejecución en local, paso a paso

### Paso 1. Abrir el proyecto

Abre la carpeta raíz del proyecto en tu IDE.

### Paso 2. Compilar y probar cada servicio

Desde la carpeta raíz, ejecuta en cada servicio:

```bash
cd auth-service
mvn clean package
cd ..
```

Repite con `estudiantes-service`, `cursos-service`, `matriculas-service` y `calificaciones-service`.
`mvn clean package` ejecuta también las pruebas automatizadas; para omitirlas agrega `-DskipTests`.

> Este paso es importante incluso si luego usarás Docker, porque los `Dockerfile` copian el `.jar` desde la carpeta `target/`.

### Paso 3. Levantar los servicios en este orden

Cada uno en su propia terminal:

```bash
cd auth-service && mvn spring-boot:run
cd estudiantes-service && mvn spring-boot:run
cd cursos-service && mvn spring-boot:run
cd matriculas-service && mvn spring-boot:run
cd calificaciones-service && mvn spring-boot:run
```

## 6. URLs de Swagger

Una vez levantados los servicios, puedes abrir Swagger en:

- Auth: `http://localhost:8081/swagger-ui.html`
- Estudiantes: `http://localhost:8082/swagger-ui.html`
- Cursos: `http://localhost:8083/swagger-ui.html`
- Matrículas: `http://localhost:8084/swagger-ui.html`
- Calificaciones: `http://localhost:8085/swagger-ui.html`

## 7. Consola H2

Si necesitas revisar la base de datos en memoria de cada servicio, abre `http://localhost:<puerto>/h2-console`.

Parámetros generales de conexión:

- **Driver:** `org.h2.Driver`
- **User Name:** `sa`
- **Password:** vacío

Cada servicio tiene su propia URL JDBC definida en su `application.yaml`. Como las bases están en memoria,
**los datos se pierden al reiniciar un servicio**.

## 8. Usuarios de prueba

El `auth-service` carga automáticamente estos usuarios:

| Usuario | Contraseña | Rol | Estudiante asociado |
|---|---|---|---|
| admin | admin123 | ADMIN | — |
| docente | docente123 | DOCENTE | — |
| estudiante | estudiante123 | ESTUDIANTE | 1 (el token lleva `estudianteId: 1`) |

### Matriz de permisos

| Recurso | ADMIN | DOCENTE | ESTUDIANTE |
|---|---|---|---|
| Estudiantes: consultar | ✔ | ✔ | ✘ |
| Estudiantes: crear, actualizar, eliminar | ✔ | ✘ | ✘ |
| Cursos: consultar | ✔ | ✔ | ✔ |
| Cursos: crear, actualizar, eliminar | ✔ | ✘ | ✘ |
| Matrículas: consultar (incluye el conteo de activas) | ✔ | ✔ | ✘ |
| Matrículas: registrar, anular | ✔ | ✘ | ✘ |
| Matrículas: `GET /api/matriculas/mias` | ✘ | ✘ | ✔ |
| Calificaciones: registrar, corregir, consultar por matrícula | ✔ | ✔ | ✘ |
| Calificaciones: `GET /api/calificaciones/mias` | ✘ | ✘ | ✔ |

## 9. Datos semilla

### estudiantes-service

Se registran automáticamente 50 estudiantes generados con Faker. Sus correos siguen el patrón
`estudiante0@demoacademico.edu`, `estudiante1@demoacademico.edu`, … La cantidad se cambia con
`app.seed.cantidad` en `application.yaml`.

### cursos-service

Se registran automáticamente tres cursos:

| Id | Código | Nombre | Créditos | Cupo máximo |
|---:|---|---|---:|---:|
| 1 | `IS3-001` | Ingeniería de Software III | 4 | 30 |
| 2 | `BD2-001` | Bases de Datos II | 3 | 25 |
| 3 | `RED-001` | Redes de Computadores | 3 | 2 |

`RED-001` tiene cupo 2 a propósito, para probar la extensión E2 con pocas matrículas.

## 10. Flujo recomendado de prueba en Swagger

### Paso 1. Hacer login

Abre `http://localhost:8081/swagger-ui.html` y usa `POST /auth/login` con este JSON:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Copia el valor del token que devuelve la respuesta.

### Paso 2. Autorizar Swagger

En el Swagger de cada servicio, haz clic en **Authorize** y pega solo el token (sin la palabra `Bearer`).

### Paso 3. Probar los servicios

| Servicio | Endpoints |
|---|---|
| estudiantes (8082) | `GET /api/estudiantes?page=0&size=10`, `GET /api/estudiantes/{id}`, `POST` (201), `PUT /{id}`, `DELETE /{id}` |
| cursos (8083) | `GET /api/cursos?page=0&size=10`, `GET /api/cursos/{id}`, `POST` (201), `PUT /{id}`, `DELETE /{id}` |
| matrículas (8084) | `GET /api/matriculas?page=0&size=10`, `GET /{id}`, `POST` (201), `PUT /{id}/anular`, `GET /activas/conteo?cursoId=` o `?estudianteId=`, `GET /mias` |
| calificaciones (8085) | `POST /api/calificaciones` (201), `PUT /{id}`, `GET /matricula/{matriculaId}`, `GET /mias` |

Ejemplos de cuerpo:

```json
{ "estudianteId": 1, "cursoId": 1 }
```

```json
{ "matriculaId": 1, "corte": 1, "nota": 4.5 }
```

## 11. Ejecución con Docker Compose

### Paso 1. Compilar primero los `.jar`

```bash
cd auth-service && mvn clean package -DskipTests && cd ..
cd estudiantes-service && mvn clean package -DskipTests && cd ..
cd cursos-service && mvn clean package -DskipTests && cd ..
cd matriculas-service && mvn clean package -DskipTests && cd ..
cd calificaciones-service && mvn clean package -DskipTests && cd ..
```

### Paso 2. Levantar el sistema

Desde la raíz del proyecto:

```bash
docker compose up --build
```

### Paso 3. Verificar que los contenedores estén arriba

```bash
docker ps
```

Deberías ver `auth-service`, `estudiantes-service`, `cursos-service`, `matriculas-service` y `calificaciones-service`.
Las URLs de Swagger son las mismas de la sección 6.

## 12. Variables de entorno usadas por Docker

| Servicio | Variables |
|---|---|
| estudiantes-service | `AUTH_SERVICE_URL`, `MATRICULAS_SERVICE_URL` |
| cursos-service | `AUTH_SERVICE_URL`, `MATRICULAS_SERVICE_URL` |
| matriculas-service | `AUTH_SERVICE_URL`, `ESTUDIANTES_SERVICE_URL`, `CURSOS_SERVICE_URL` |
| calificaciones-service | `AUTH_SERVICE_URL`, `MATRICULAS_SERVICE_URL` |

Dentro de Docker los servicios se llaman por su nombre (por ejemplo `http://matriculas-service:8084`).
`estudiantes-service` y `cursos-service` no declaran `depends_on` hacia `matriculas-service`: matrículas ya
depende de ellos y Compose no acepta ciclos (ver la guía de la extensión E1).

## 13. Respuestas de seguridad esperadas

Si intentas acceder sin token o con token inválido, los servicios protegidos responden con una estructura uniforme:

```json
{
  "success": false,
  "message": "Debe enviar un token Bearer válido",
  "errorCode": "AUTH_HEADER_MISSING",
  "status": 401,
  "path": "/api/recurso",
  "timestamp": "2026-04-11T15:30:00"
}
```

| `errorCode` | Estado | Significado |
|---|---:|---|
| `AUTH_HEADER_MISSING` | 401 | No se envió `Authorization: Bearer ...` |
| `TOKEN_INVALID` | 401 | `auth-service` rechazó el token (firma, expiración o sin rol) |
| `TOKEN_VALIDATION_ERROR` | 401 | No fue posible consultar a `auth-service` |
| `AUTH_FORBIDDEN` | 403 | El rol no tiene permiso para la operación |

### Errores de negocio y de la petición

Los servicios de negocio responden `{"success": false, "message": ..., "data": ...}` con estos códigos:

| Estado | Cuándo |
|---:|---|
| 400 | Validación fallida, JSON mal formado, parámetro con tipo incorrecto o regla de negocio (duplicado, matrícula ya anulada, curso sin cupo, nota repetida) |
| 404 | Recurso o ruta inexistente |
| 405 | Método HTTP no permitido |
| 409 | Conflicto con los datos almacenados (por ejemplo, eliminar un curso con matrículas activas) |
| 502 | Un servicio remoto no respondió correctamente |

Un `401` siempre indica un problema con el token.

## 14. Problemas comunes

### Error: no encuentra el `.jar` al construir Docker

Causa: no compilaste el servicio antes. Solución: `mvn clean package -DskipTests` en cada servicio.

### Error: `Connection refused` o `502` entre servicios

Causa: el servicio al que se llama todavía no está arriba. Verifica que todos estén corriendo, espera unos
segundos y vuelve a probar.

### Error 401 en servicios protegidos

Causa posible: no enviaste el encabezado `Authorization`, el token expiró o no tiene formato `Bearer ...`.

### Error 403

El usuario está autenticado, pero su rol no tiene permiso para ese endpoint (revisa la matriz de la sección 8).

### Los datos desaparecieron

Las bases H2 están en memoria: al reiniciar un servicio, sus datos vuelven a los datos semilla.

## 15. Secuencia mínima de validación

1. Login exitoso con `admin` y login fallido con contraseña incorrecta.
2. Acceso rechazado sin token y acceso exitoso con token válido.
3. Creación de estudiante y de curso con rol `ADMIN`; intento con rol insuficiente (`403`).
4. Registro de matrícula válido, con estudiante inexistente y duplicado.
5. Anulación de una matrícula y segundo intento de anularla (`400`).
6. Petición con JSON mal formado (`400`, no `401`).

## 15.1 Extensiones del proyecto final (análisis)

El proyecto incluye cuatro extensiones implementadas. Cada una tiene una guía de análisis paso a paso que
recorre el código, explica las decisiones de diseño, indica cómo verificarla y plantea preguntas de análisis:

| Orden | Extensión | Guía |
|---:|---|---|
| 1 | E2 · Cupos por curso | `instrucciones/extension-e2-cupos.html` |
| 2 | E1 · Integridad entre servicios | `instrucciones/extension-e1-integridad.html` |
| 3 | E4 · Mis matrículas | `instrucciones/extension-e4-mis-matriculas.html` |
| 4 | E3 · calificaciones-service | `instrucciones/extension-e3-calificaciones.html` |

Las verificaciones continúan con los datos que deja la guía anterior; sígalas en ese orden y sin reiniciar los servicios.

## 16. Comandos útiles

### Ejecutar las pruebas automatizadas

```bash
cd matriculas-service
mvn test
```

### Detener servicios en Docker

```bash
docker compose down
```

### Reconstruir desde cero

```bash
docker compose down
cd auth-service && mvn clean package -DskipTests && cd ..
cd estudiantes-service && mvn clean package -DskipTests && cd ..
cd cursos-service && mvn clean package -DskipTests && cd ..
cd matriculas-service && mvn clean package -DskipTests && cd ..
cd calificaciones-service && mvn clean package -DskipTests && cd ..
docker compose up --build
```

---

La carpeta `instrucciones/` incluye las páginas HTML del proyecto: la guía general, la guía de seguridad,
la explicación de cada módulo y las guías de análisis de las extensiones.
