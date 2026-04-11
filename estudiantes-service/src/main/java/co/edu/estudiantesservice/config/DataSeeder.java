package co.edu.estudiantesservice.config;

import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.service.EstudianteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(EstudianteService service) {
        return args -> {
            if (service.listar().isEmpty()) {
                EstudianteCreateDTO a = new EstudianteCreateDTO();
                a.setNombre("Ana"); a.setApellido("Martínez"); a.setEmail("ana@correo.edu"); a.setEdad(19);
                service.crear(a);
            }
        };
    }
}
