package co.edu.estudiantesservice.service;

import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.dto.EstudianteDTO;
import co.edu.estudiantesservice.exception.BusinessException;
import co.edu.estudiantesservice.exception.NotFoundException;
import co.edu.estudiantesservice.model.Estudiante;
import co.edu.estudiantesservice.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    private final EstudianteRepository repository;

    public EstudianteServiceImpl(EstudianteRepository repository) {
        this.repository = repository;
    }

    public List<EstudianteDTO> listar() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public EstudianteDTO buscarPorId(Long id) {
        return toDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado")));
    }

    public EstudianteDTO crear(EstudianteCreateDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un estudiante con ese correo");
        }
        Estudiante e = new Estudiante();
        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setEmail(dto.getEmail());
        e.setEdad(dto.getEdad());
        return toDto(repository.save(e));
    }

    public EstudianteDTO actualizar(Long id, EstudianteCreateDTO dto) {
        Estudiante e = repository.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        if (repository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Ya existe otro estudiante con ese correo");
        }
        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setEmail(dto.getEmail());
        e.setEdad(dto.getEdad());
        return toDto(repository.save(e));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Estudiante no encontrado");
        repository.deleteById(id);
    }

    private EstudianteDTO toDto(Estudiante e) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setEmail(e.getEmail());
        dto.setEdad(e.getEdad());
        return dto;
    }
}
