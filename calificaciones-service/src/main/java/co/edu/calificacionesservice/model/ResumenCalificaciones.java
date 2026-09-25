package co.edu.calificacionesservice.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Notas de una matrícula con su promedio ponderado. No es una entidad: se calcula en cada consulta.
 */
public record ResumenCalificaciones(Long matriculaId, List<Calificacion> calificaciones, BigDecimal promedioPonderado) {

    public boolean completa() {
        return calificaciones.size() == 3;
    }
}
