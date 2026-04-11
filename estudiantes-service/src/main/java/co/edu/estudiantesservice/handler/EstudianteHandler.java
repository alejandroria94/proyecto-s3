package co.edu.estudiantesservice.handler;

import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.dto.EstudianteDTO;
import co.edu.estudiantesservice.service.EstudianteService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstudianteHandler {
    private final EstudianteService service;
    public EstudianteHandler(EstudianteService service) { this.service = service; }
    public List<EstudianteDTO> listar() { return service.listar(); }
    public EstudianteDTO buscarPorId(Long id) { return service.buscarPorId(id); }
    public EstudianteDTO crear(EstudianteCreateDTO dto) { return service.crear(dto); }
    public EstudianteDTO actualizar(Long id, EstudianteCreateDTO dto) { return service.actualizar(id, dto); }
    public void eliminar(Long id) { service.eliminar(id); }
}
