package co.edu.estudiantesservice.service;

import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.dto.EstudianteDTO;

import java.util.List;

public interface EstudianteService {
    List<EstudianteDTO> listar();
    EstudianteDTO buscarPorId(Long id);
    EstudianteDTO crear(EstudianteCreateDTO dto);
    EstudianteDTO actualizar(Long id, EstudianteCreateDTO dto);
    void eliminar(Long id);
}
