package co.edu.matriculasservice.service;

import co.edu.matriculasservice.client.CursosClient;
import co.edu.matriculasservice.client.EstudiantesClient;
import co.edu.matriculasservice.dto.MatriculaCreateDTO;
import co.edu.matriculasservice.dto.MatriculaDTO;
import co.edu.matriculasservice.dto.RemoteApiResponse;
import co.edu.matriculasservice.exception.BusinessException;
import co.edu.matriculasservice.exception.NotFoundException;
import co.edu.matriculasservice.exception.RemoteServiceException;
import co.edu.matriculasservice.model.Matricula;
import co.edu.matriculasservice.repository.MatriculaRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaServiceImpl implements MatriculaService {
    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_ANULADA = "ANULADA";

    private final MatriculaRepository repository;
    private final EstudiantesClient estudiantesClient;
    private final CursosClient cursosClient;

    public MatriculaServiceImpl(MatriculaRepository repository, EstudiantesClient estudiantesClient, CursosClient cursosClient) {
        this.repository = repository;
        this.estudiantesClient = estudiantesClient;
        this.cursosClient = cursosClient;
    }

    public List<MatriculaDTO> listar() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public MatriculaDTO buscarPorId(Long id) {
        return toDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Matrícula no encontrada")));
    }

    public MatriculaDTO registrar(MatriculaCreateDTO dto) {
        validarEstudiante(dto.getEstudianteId());
        validarCurso(dto.getCursoId());

        repository.findByEstudianteIdAndCursoIdAndEstado(dto.getEstudianteId(), dto.getCursoId(), ESTADO_ACTIVA)
                .ifPresent(m -> {
                    throw new BusinessException("Ya existe una matrícula activa para el estudiante y curso indicados");
                });

        Matricula matricula = new Matricula();
        matricula.setEstudianteId(dto.getEstudianteId());
        matricula.setCursoId(dto.getCursoId());
        matricula.setFechaMatricula(LocalDate.now());
        matricula.setEstado(ESTADO_ACTIVA);
        return toDto(repository.save(matricula));
    }

    public MatriculaDTO anular(Long id) {
        Matricula matricula = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Matrícula no encontrada"));
        matricula.setEstado(ESTADO_ANULADA);
        return toDto(repository.save(matricula));
    }

    private void validarEstudiante(Long estudianteId) {
        try {
            RemoteApiResponse<?> response = estudiantesClient.buscarPorId(estudianteId);
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new BusinessException("El estudiante indicado no existe o no está disponible");
            }
        } catch (FeignException.NotFound ex) {
            throw new BusinessException("El estudiante indicado no existe");
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            throw new RemoteServiceException("No fue posible validar el estudiante por un problema de autorización con el servicio remoto");
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al consultar estudiantes-service. Código remoto: " + ex.status());
        }
    }

    private void validarCurso(Long cursoId) {
        try {
            RemoteApiResponse<?> response = cursosClient.buscarPorId(cursoId);
            if (response == null || !response.isSuccess() || response.getData() == null) {
                throw new BusinessException("El curso indicado no existe o no está disponible");
            }
        } catch (FeignException.NotFound ex) {
            throw new BusinessException("El curso indicado no existe");
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            throw new RemoteServiceException("No fue posible validar el curso por un problema de autorización con el servicio remoto");
        } catch (FeignException ex) {
            throw new RemoteServiceException("Error al consultar cursos-service. Código remoto: " + ex.status());
        }
    }

    private MatriculaDTO toDto(Matricula matricula) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(matricula.getId());
        dto.setEstudianteId(matricula.getEstudianteId());
        dto.setCursoId(matricula.getCursoId());
        dto.setFechaMatricula(matricula.getFechaMatricula());
        dto.setEstado(matricula.getEstado());
        return dto;
    }
}
