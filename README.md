# Proyecto S3 · Guía de ejecución paso a paso

Este documento explica cómo ejecutar el proyecto completo, tanto **en local** como **con Docker Compose**.

## 1. ¿Qué contiene el proyecto?

El repositorio está compuesto por 4 microservicios Spring Boot:

- `auth-service` → autenticación y validación de token
- `estudiantes-service` → gestión de estudiantes
- `cursos-service` → gestión de cursos
- `matriculas-service` → gestión de matrículas

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

## 5. Ejecución en local, paso a paso

### Paso 1. Abrir el proyecto

Abre la carpeta raíz del proyecto en tu IDE.

### Paso 2. Compilar cada servicio

Desde la carpeta raíz, ejecuta:

```bash
cd auth-service
mvn clean package -DskipTests
cd ..

cd estudiantes-service
mvn clean package -DskipTests
cd ..

cd cursos-service
mvn clean package -DskipTests
cd ..

cd matriculas-service
mvn clean package -DskipTests
cd ..
```

> Este paso es importante incluso si luego usarás Docker, porque los `Dockerfile` copian el `.jar` desde la carpeta `target/`.

### Paso 3. Levantar los servicios en este orden

#### 3.1 auth-service

```bash
cd auth-service
mvn spring-boot:run
```

#### 3.2 estudiantes-service

En otra terminal:

```bash
cd estudiantes-service
mvn spring-boot:run
```

#### 3.3 cursos-service

En otra terminal:

```bash
cd cursos-service
mvn spring-boot:run
```

#### 3.4 matriculas-service

En otra terminal:

```bash
cd matriculas-service
mvn spring-boot:run
```

## 6. URLs de Swagger

Una vez levantados los servicios, puedes abrir Swagger en:

- Auth: `http://localhost:8081/swagger-ui.html`
- Estudiantes: `http://localhost:8082/swagger-ui.html`
- Cursos: `http://localhost:8083/swagger-ui.html`
- Matrículas: `http://localhost:8084/swagger-ui.html`

## 7. Consola H2

Si necesitas revisar la base de datos en memoria de cada servicio:

- Auth: `http://localhost:8081/h2-console`
- Estudiantes: `http://localhost:8082/h2-console`
- Cursos: `http://localhost:8083/h2-console`
- Matrículas: `http://localhost:8084/h2-console`

Parámetros generales de conexión:

- **Driver:** `org.h2.Driver`
- **User Name:** `sa`
- **Password:** vacío

Cada servicio tiene su propia URL JDBC interna definida en su `application.yaml`.

## 8. Usuarios de prueba

El `auth-service` carga automáticamente estos usuarios:

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | ADMIN |
| docente | docente123 | DOCENTE |
| estudiante | estudiante123 | ESTUDIANTE |

## 9. Datos semilla

### estudiantes-service

Se registra automáticamente un estudiante de ejemplo:

- Nombre: Ana Martínez
- Email: `ana@correo.edu`
- Edad: 19

### cursos-service

Se registra automáticamente un curso de ejemplo:

- Código: `IS3-001`
- Nombre: `Ingeniería de Software III`
- Créditos: 4

## 10. Flujo recomendado de prueba en Swagger

### Paso 1. Hacer login

Abre:

`http://localhost:8081/swagger-ui.html`

Usa el endpoint `POST /auth/login` con este JSON:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Copia el valor del token que devuelve la respuesta.

### Paso 2. Probar estudiantes-service

Abre:

`http://localhost:8082/swagger-ui.html`

Haz clic en **Authorize** y pega:

```text
Bearer TU_TOKEN_AQUI
```

Pruebas sugeridas:

- `GET /api/estudiantes`
- `POST /api/estudiantes`
- `PUT /api/estudiantes/{id}`
- `DELETE /api/estudiantes/{id}`

### Paso 3. Probar cursos-service

Abre:

`http://localhost:8083/swagger-ui.html`

Usa el mismo token y prueba:

- `GET /api/cursos`
- `POST /api/cursos`
- `PUT /api/cursos/{id}`
- `DELETE /api/cursos/{id}`

### Paso 4. Probar matriculas-service

Abre:

`http://localhost:8084/swagger-ui.html`

Usa el mismo token y prueba:

- `GET /api/matriculas`
- `POST /api/matriculas`

Ejemplo de registro de matrícula:

```json
{
  "estudianteId": 1,
  "cursoId": 1
}
```

## 11. Ejecución con Docker Compose

### Paso 1. Compilar primero los `.jar`

Debes ejecutar estos comandos antes de levantar Docker:

```bash
cd auth-service && mvn clean package -DskipTests && cd ..
cd estudiantes-service && mvn clean package -DskipTests && cd ..
cd cursos-service && mvn clean package -DskipTests && cd ..
cd matriculas-service && mvn clean package -DskipTests && cd ..
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

Deberías ver contenedores con nombres parecidos a:

- `auth-service`
- `estudiantes-service`
- `cursos-service`
- `matriculas-service`

### Paso 4. Probar Swagger

Las URLs siguen siendo las mismas:

- `http://localhost:8081/swagger-ui.html`
- `http://localhost:8082/swagger-ui.html`
- `http://localhost:8083/swagger-ui.html`
- `http://localhost:8084/swagger-ui.html`

## 12. Variables de entorno usadas por Docker

Docker Compose inyecta estas variables:

### estudiantes-service
- `AUTH_SERVICE_URL=http://auth-service:8081`

### cursos-service
- `AUTH_SERVICE_URL=http://auth-service:8081`

### matriculas-service
- `AUTH_SERVICE_URL=http://auth-service:8081`
- `ESTUDIANTES_SERVICE_URL=http://estudiantes-service:8082`
- `CURSOS_SERVICE_URL=http://cursos-service:8083`

## 13. Respuestas de seguridad esperadas

Si intentas acceder sin token o con token inválido, los servicios protegidos deben responder con una estructura uniforme similar a esta:

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

Si el token es válido pero el rol no tiene permisos suficientes, la respuesta esperada es `403`.

## 14. Problemas comunes

### Error: no encuentra el `.jar` al construir Docker

Causa: no compilaste el servicio antes.

Solución:

```bash
mvn clean package -DskipTests
```

Hazlo en cada módulo.

### Error: `Connection refused` entre servicios

Causa: `auth-service`, `estudiantes-service` o `cursos-service` todavía no está arriba.

Solución:

- verifica que todos estén corriendo;
- espera unos segundos y vuelve a probar;
- revisa la terminal de cada servicio.

### Error 401 en servicios protegidos

Causa posible:

- no enviaste el encabezado `Authorization`;
- el token expiró;
- el token no tiene formato `Bearer ...`.

### Error 403

Causa posible:

- el usuario sí está autenticado, pero no tiene el rol suficiente para ese endpoint.

## 15. Secuencia mínima de validación

Para demostrar que el proyecto corre correctamente, valida al menos esto:

1. Login exitoso con `admin`.
2. Login fallido con contraseña incorrecta.
3. Acceso rechazado sin token a estudiantes, cursos o matrículas.
4. Acceso exitoso con token válido.
5. Creación de estudiante con rol `ADMIN`.
6. Creación de curso con rol `ADMIN`.
7. Registro de matrícula válido.
8. Intento de matrícula con estudiante inexistente.
9. Intento de matrícula duplicada.
10. Intento de acceso con rol insuficiente.

## 16. Comandos útiles

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
docker compose up --build
```

---

Si quieres complementar este README, la carpeta `instrucciones/` incluye las páginas HTML del proyecto, la guía general, la guía de seguridad y la explicación por cada módulo.
