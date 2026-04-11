package co.edu.cursosservice.controller;

import co.edu.cursosservice.api.ApiResponse;
import co.edu.cursosservice.api.ResponseBuilder;
import co.edu.cursosservice.dto.CursoCreateDTO;
import co.edu.cursosservice.dto.CursoDTO;
import co.edu.cursosservice.handler.CursoHandler;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoHandler handler;

    public CursoController(CursoHandler handler) {
        this.handler = handler;
    }

    @GetMapping
    public ApiResponse<List<CursoDTO>> listar() {
        return ResponseBuilder.success("Consulta exitosa", handler.listar());
    }

    @GetMapping("/{id}")
    public ApiResponse<CursoDTO> buscarPorId(
            @Parameter(description = "Id del curso", required = true)
            @PathVariable("id") Long id) {
        return ResponseBuilder.success("Consulta exitosa", handler.buscarPorId(id));
    }

    @PostMapping
    public ApiResponse<CursoDTO> crear(@Valid @RequestBody CursoCreateDTO dto) {
        return ResponseBuilder.success("Curso creado", handler.crear(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<CursoDTO> actualizar(
            @Parameter(description = "Id del curso", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody CursoCreateDTO dto) {
        return ResponseBuilder.success("Curso actualizado", handler.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> eliminar(
            @Parameter(description = "Id del curso", required = true)
            @PathVariable("id") Long id) {
        handler.eliminar(id);
        return ResponseBuilder.success("Curso eliminado", null);
    }
}