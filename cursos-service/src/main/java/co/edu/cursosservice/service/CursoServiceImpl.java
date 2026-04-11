package co.edu.cursosservice.service;

import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.dto.CursoDTO;
import co.edu.cursosservice.exception.BusinessException;
import co.edu.cursosservice.exception.NotFoundException;
import co.edu.cursosservice.model.Curso;
import co.edu.cursosservice.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoServiceImpl implements CursoService {
    private final CursoRepository repository;
    public CursoServiceImpl(CursoRepository repository) { this.repository = repository; }

    public List<CursoDTO> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    public CursoDTO buscarPorId(Long id) { return toDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Curso no encontrado"))); }

    public CursoDTO crear(CursoCreateDTO dto) {
        if (repository.existsByCodigo(dto.getCodigo())) throw new BusinessException("Ya existe un curso con ese código");
        Curso c = new Curso();
        c.setCodigo(dto.getCodigo()); c.setNombre(dto.getNombre()); c.setDescripcion(dto.getDescripcion()); c.setCreditos(dto.getCreditos()); c.setDocenteResponsable(dto.getDocenteResponsable());
        return toDto(repository.save(c));
    }

    public CursoDTO actualizar(Long id, CursoCreateDTO dto) {
        Curso c = repository.findById(id).orElseThrow(() -> new NotFoundException("Curso no encontrado"));
        c.setCodigo(dto.getCodigo()); c.setNombre(dto.getNombre()); c.setDescripcion(dto.getDescripcion()); c.setCreditos(dto.getCreditos()); c.setDocenteResponsable(dto.getDocenteResponsable());
        return toDto(repository.save(c));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new NotFoundException("Curso no encontrado");
        repository.deleteById(id);
    }

    private CursoDTO toDto(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setId(c.getId()); dto.setCodigo(c.getCodigo()); dto.setNombre(c.getNombre()); dto.setDescripcion(c.getDescripcion()); dto.setCreditos(c.getCreditos()); dto.setDocenteResponsable(c.getDocenteResponsable());
        return dto;
    }
}
