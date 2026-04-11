package co.edu.matriculasservice.handler;

import co.edu.matriculasservice.dto.MatriculaCreateDTO;
import co.edu.matriculasservice.dto.MatriculaDTO;
import co.edu.matriculasservice.service.MatriculaService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatriculaHandler {
    private final MatriculaService service;

    public MatriculaHandler(MatriculaService service) {
        this.service = service;
    }

    public List<MatriculaDTO> listar() { return service.listar(); }
    public MatriculaDTO buscarPorId(Long id) { return service.buscarPorId(id); }
    public MatriculaDTO registrar(MatriculaCreateDTO dto) { return service.registrar(dto); }
    public MatriculaDTO anular(Long id) { return service.anular(id); }
}
