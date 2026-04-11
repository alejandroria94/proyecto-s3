package co.edu.matriculasservice.config;

import co.edu.matriculasservice.security.AuthValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthValidationFilter authValidationFilter;

    public SecurityConfig(AuthValidationFilter authValidationFilter) {
        this.authValidationFilter = authValidationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/h2-console/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/matriculas/**").hasAnyAuthority("ADMIN", "DOCENTE")
                .requestMatchers(HttpMethod.POST, "/api/matriculas/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/matriculas/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/matriculas/**").hasAuthority("ADMIN")

                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .addFilterBefore(authValidationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
