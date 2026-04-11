package co.edu.cursosservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CursoCreateDTO {
    @NotBlank private String codigo;
    @NotBlank private String nombre;
    private String descripcion;
    @Min(1) private Integer creditos;
    @NotBlank private String docenteResponsable;

    public String getCodigo() { return codigo; } public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; } public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; } public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCreditos() { return creditos; } public void setCreditos(Integer creditos) { this.creditos = creditos; }
    public String getDocenteResponsable() { return docenteResponsable; } public void setDocenteResponsable(String docenteResponsable) { this.docenteResponsable = docenteResponsable; }
}
