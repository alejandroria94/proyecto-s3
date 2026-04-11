package co.edu.matriculasservice.client;

import co.edu.matriculasservice.dto.CursoDTO;
import co.edu.matriculasservice.dto.RemoteApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cursosClient", url = "${cursos-service.url}")
public interface CursosClient {
    @GetMapping("/api/cursos/{id}")
    RemoteApiResponse<CursoDTO> buscarPorId(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id);
}
