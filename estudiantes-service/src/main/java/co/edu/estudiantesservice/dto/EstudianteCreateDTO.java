package co.edu.estudiantesservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class EstudianteCreateDTO {
    @NotBlank private String nombre;
    @NotBlank private String apellido;
    @Email @NotBlank private String email;
    @Min(14) private Integer edad;

    public String getNombre() { return nombre; } public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; } public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public Integer getEdad() { return edad; } public void setEdad(Integer edad) { this.edad = edad; }
}
