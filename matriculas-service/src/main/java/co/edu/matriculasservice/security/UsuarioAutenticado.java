package co.edu.matriculasservice.security;

import java.security.Principal;

/**
 * Principal que el filtro guarda en el SecurityContext. Además del usuario y el rol,
 * conserva el estudiante asociado (E4) para autorizar por dueño del recurso.
 */
public record UsuarioAutenticado(String username, String role, Long estudianteId) implements Principal {

    @Override
    public String getName() {
        return username;
    }
}
