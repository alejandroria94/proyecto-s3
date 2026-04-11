package co.edu.matriculasservice.dto;

import jakarta.validation.constraints.NotNull;

public class MatriculaCreateDTO {
    @NotNull private Long estudianteId;
    @NotNull private Long cursoId;
    public Long getEstudianteId() { return estudianteId; } public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public Long getCursoId() { return cursoId; } public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
}
