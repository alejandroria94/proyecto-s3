package co.edu.matriculasservice.service;

import co.edu.matriculasservice.client.CursosClient;
import co.edu.matriculasservice.client.EstudiantesClient;
import co.edu.matriculasservice.dto.MatriculaCreateDTO;
import co.edu.matriculasservice.dto.MatriculaDTO;
import co.edu.matriculasservice.exception.BusinessException;
import co.edu.matriculasservice.exception.NotFoundException;
import co.edu.matriculasservice.model.Matricula;
import co.edu.matriculasservice.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaServiceImpl implements MatriculaService {
    private final MatriculaRepository repository;
    private final EstudiantesClient estudiantesClient;
    private final CursosClient cursosClient;

    public MatriculaServiceImpl(MatriculaRepository repository, EstudiantesClient estudiantesClient, CursosClient cursosClient) {
        this.repository = repository;
        this.estudiantesClient = estudiantesClient;
        this.cursosClient = cursosClient;
    }

    public List<MatriculaDTO> listar() { return repository.findAll().stream().map(this::toDto).toList(); }

    public MatriculaDTO buscarPorId(Long id) {
        return toDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Matrícula no encontrada")));
    }

    public MatriculaDTO registrar(String authorization, MatriculaCreateDTO dto) {
        try {
            var er = estudiantesClient.buscarPorId(authorization, dto.getEstudianteId());
            if (er == null || !er.isSuccess() || er.getData() == null) throw new BusinessException("Estudiante no existe");
            var cr = cursosClient.buscarPorId(authorization, dto.getCursoId());
            if (cr == null || !cr.isSuccess() || cr.getData() == null) throw new BusinessException("Curso no existe");
        } catch (Exception ex) {
            throw new BusinessException("No fue posible validar estudiante/curso: " + ex.getMessage());
        }

        repository.findByEstudianteIdAndCursoIdAndEstado(dto.getEstudianteId(), dto.getCursoId(), "ACTIVA")
                .ifPresent(m -> { throw new BusinessException("Ya existe una matrícula activa"); });

        Matricula m = new Matricula();
        m.setEstudianteId(dto.getEstudianteId());
        m.setCursoId(dto.getCursoId());
        m.setFechaMatricula(LocalDate.now());
        m.setEstado("ACTIVA");
        return toDto(repository.save(m));
    }

    public MatriculaDTO anular(Long id) {
        Matricula m = repository.findById(id).orElseThrow(() -> new NotFoundException("Matrícula no encontrada"));
        m.setEstado("ANULADA");
        return toDto(repository.save(m));
    }

    private MatriculaDTO toDto(Matricula m) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(m.getId()); dto.setEstudianteId(m.getEstudianteId()); dto.setCursoId(m.getCursoId()); dto.setFechaMatricula(m.getFechaMatricula()); dto.setEstado(m.getEstado());
        return dto;
    }
}
