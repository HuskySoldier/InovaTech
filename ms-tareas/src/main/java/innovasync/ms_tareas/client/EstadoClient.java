package innovasync.ms_tareas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-estado", url = "http://localhost:8084")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")
    EstadoResponse obtenerEstado(@PathVariable Long id);

    class EstadoResponse {
        public Long id;
        public String nombre;
        public String descripcion;
    }
}