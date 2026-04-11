# Proyecto S3 refactorizado

Cambios aplicados en esta versión:

1. **auth-service**
   - Se creó `AuthService` y `AuthServiceImpl`.
   - `AuthController` quedó sin lógica de negocio de autenticación.

2. **estudiantes-service**
   - Se agregó validación de correo duplicado al actualizar (`existsByEmailAndIdNot`).
   - Se incorporó manejo uniforme de errores de seguridad con:
     - `ApiErrorResponse`
     - `CustomAuthenticationException`
     - `CustomAuthenticationEntryPoint`
     - `CustomAccessDeniedHandler`
   - `AuthValidationFilter` ya no responde solo con `setStatus(...)`, sino con JSON estructurado para 401.

3. **cursos-service**
   - Se agregó validación de código duplicado al actualizar (`existsByCodigoAndIdNot`).
   - Se incorporó manejo uniforme de errores de seguridad con:
     - `ApiErrorResponse`
     - `CustomAuthenticationException`
     - `CustomAuthenticationEntryPoint`
     - `CustomAccessDeniedHandler`
   - `AuthValidationFilter` ya no responde solo con `setStatus(...)`, sino con JSON estructurado para 401.

4. **matriculas-service**
   - `MatriculaService` ya no recibe el encabezado `Authorization`.
   - Se agregó propagación automática del token hacia OpenFeign mediante `FeignAuthPropagationConfig`.
   - Se mejoró el manejo de errores remotos con `RemoteServiceException`.
   - Se incorporó manejo uniforme de errores de seguridad con:
     - `ApiErrorResponse`
     - `CustomAuthenticationException`
     - `CustomAuthenticationEntryPoint`
     - `CustomAccessDeniedHandler`
   - `AuthValidationFilter` ya no responde solo con `setStatus(...)`, sino con JSON estructurado para 401.

5. **Documentación HTML**
   - Se actualizó la guía de seguridad.
   - Se agregaron notas sobre respuestas uniformes 401/403 en la guía general, módulos protegidos y página de pruebas.
   - Se mantuvo la navegación interna del mini sitio para GitHub Pages.

## Importante

Este paquete contiene **código fuente refactorizado** e **instrucciones HTML actualizadas**.
Antes de ejecutar con Docker Compose, compile cada microservicio:

```bash
mvn clean package -DskipTests
```

Luego ejecute:

```bash
docker compose up --build
```
