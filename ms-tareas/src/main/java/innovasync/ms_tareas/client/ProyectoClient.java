package innovasync.ms_tareas.client;

import innovasync.ms_tareas.dto.ProyectoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "proyectos", fallback = ProyectoClientFallback.class)
public interface ProyectoClient {

    // Asegúrate de que esta sea la ruta exacta que usas en el controlador de 'proyectos'
    @GetMapping("/api/proyectos/{id}") 
    ResponseEntity<ProyectoDTO> getProyectoById(@PathVariable("id") Long id);
}
