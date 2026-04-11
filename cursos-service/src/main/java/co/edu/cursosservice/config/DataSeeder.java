package co.edu.cursosservice.config;

import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.service.CursoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(CursoService service) {
        return args -> {
            if (service.listar().isEmpty()) {
                CursoCreateDTO c = new CursoCreateDTO();
                c.setCodigo("IS3-001"); c.setNombre("Ingeniería de Software III"); c.setDescripcion("Curso base"); c.setCreditos(4); c.setDocenteResponsable("Docente principal");
                service.crear(c);
            }
        };
    }
}
