package co.edu.authservice.config;

import co.edu.authservice.model.Usuario;
import co.edu.authservice.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedUsers(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(build("admin", "admin123", "ADMIN", encoder));
                repository.save(build("docente", "docente123", "DOCENTE", encoder));
                repository.save(build("estudiante", "estudiante123", "ESTUDIANTE", encoder));
            }
        };
    }

    private Usuario build(String username, String rawPassword, String role, PasswordEncoder encoder) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(role);
        return u;
    }
}
