package co.edu.cursosservice.service;

import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.dto.CursoDTO;

import java.util.List;

public interface CursoService {
    List<CursoDTO> listar();
    CursoDTO buscarPorId(Long id);
    CursoDTO crear(CursoCreateDTO dto);
    CursoDTO actualizar(Long id, CursoCreateDTO dto);
    void eliminar(Long id);
}
