package co.edu.calificacionesservice.client;

import co.edu.calificacionesservice.dto.MatriculaRemotaDTO;
import co.edu.calificacionesservice.dto.RemoteApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "matriculasClient", url = "${matriculas-service.url}")
public interface MatriculasClient {

    @GetMapping("/api/matriculas/{id}")
    RemoteApiResponse<MatriculaRemotaDTO> buscarPorId(@PathVariable("id") Long id);

    // Reto E4 + E3: con el token del ESTUDIANTE solo se puede usar este endpoint de matrículas
    @GetMapping("/api/matriculas/mias")
    RemoteApiResponse<List<MatriculaRemotaDTO>> misMatriculas();
}
