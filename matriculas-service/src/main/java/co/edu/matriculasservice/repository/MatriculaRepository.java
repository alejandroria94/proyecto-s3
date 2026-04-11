package co.edu.matriculasservice.repository;

import co.edu.matriculasservice.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    Optional<Matricula> findByEstudianteIdAndCursoIdAndEstado(Long estudianteId, Long cursoId, String estado);
}
