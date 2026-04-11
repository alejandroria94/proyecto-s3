package co.edu.cursosservice.security;

import co.edu.cursosservice.client.AuthClient;
import co.edu.cursosservice.dto.TokenValidationResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthValidationFilter extends OncePerRequestFilter {

    private final AuthClient authClient;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public AuthValidationFilter(AuthClient authClient,
                                CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.authClient = authClient;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new CustomAuthenticationException(
                            "Debe enviar un token Bearer válido",
                            "AUTH_HEADER_MISSING"
                    )
            );
            return;
        }

        try {
            TokenValidationResponse validation = authClient.validate(authHeader);
            if (!validation.isValid() || validation.getRole() == null) {
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(
                        request,
                        response,
                        new CustomAuthenticationException(
                                "El token no es válido o no contiene rol",
                                "TOKEN_INVALID"
                        )
                );
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            validation.getUsername(),
                            null,
                            List.of(new SimpleGrantedAuthority(validation.getRole()))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new CustomAuthenticationException(
                            "No fue posible validar el token",
                            "TOKEN_VALIDATION_ERROR"
                    )
            );
        }
    }
}
