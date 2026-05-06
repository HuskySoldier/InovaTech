package innovasync.ms_tareas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import innovasync.ms_tareas.dto.EstadoResponse;

@FeignClient(name = "estado-service", url = "http://localhost:8084", fallback = EstadoClientFallback.class)
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")
    EstadoResponse obtenerEstado(@PathVariable Long id);

    
}