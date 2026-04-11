package co.edu.matriculasservice.service;

import co.edu.matriculasservice.dto.MatriculaCreateDTO;
import co.edu.matriculasservice.dto.MatriculaDTO;
import java.util.List;

public interface MatriculaService {
    List<MatriculaDTO> listar();
    MatriculaDTO buscarPorId(Long id);
    MatriculaDTO registrar(String authorization, MatriculaCreateDTO dto);
    MatriculaDTO anular(Long id);
}
