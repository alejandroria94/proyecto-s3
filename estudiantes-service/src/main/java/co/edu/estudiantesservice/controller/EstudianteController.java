package co.edu.estudiantesservice.controller;

import co.edu.estudiantesservice.api.ApiResponse;
import co.edu.estudiantesservice.api.ResponseBuilder;
import co.edu.estudiantesservice.dto.EstudianteCreateDTO;
import co.edu.estudiantesservice.dto.EstudianteDTO;
import co.edu.estudiantesservice.handler.EstudianteHandler;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    private final EstudianteHandler handler;
    public EstudianteController(EstudianteHandler handler) { this.handler = handler; }

    @GetMapping
    public ApiResponse<List<EstudianteDTO>> listar() { return ResponseBuilder.success("Consulta exitosa", handler.listar()); }

    @GetMapping("/{id}")
    public ApiResponse<EstudianteDTO> buscarPorId(@PathVariable Long id) { return ResponseBuilder.success("Consulta exitosa", handler.buscarPorId(id)); }

    @PostMapping
    public ApiResponse<EstudianteDTO> crear(@Valid @RequestBody EstudianteCreateDTO dto) { return ResponseBuilder.success("Estudiante creado", handler.crear(dto)); }

    @PutMapping("/{id}")
    public ApiResponse<EstudianteDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstudianteCreateDTO dto) { return ResponseBuilder.success("Estudiante actualizado", handler.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> eliminar(@PathVariable Long id) { handler.eliminar(id); return ResponseBuilder.success("Estudiante eliminado", null); }
}
