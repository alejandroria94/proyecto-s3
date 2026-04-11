package co.edu.cursosservice.handler;

import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.dto.CursoDTO;
import co.edu.cursosservice.service.CursoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CursoHandler {
    private final CursoService service;
    public CursoHandler(CursoService service) { this.service = service; }
    public List<CursoDTO> listar() { return service.listar(); }
    public CursoDTO buscarPorId(Long id) { return service.buscarPorId(id); }
    public CursoDTO crear(CursoCreateDTO dto) { return service.crear(dto); }
    public CursoDTO actualizar(Long id, CursoCreateDTO dto) { return service.actualizar(id, dto); }
    public void eliminar(Long id) { service.eliminar(id); }
}
