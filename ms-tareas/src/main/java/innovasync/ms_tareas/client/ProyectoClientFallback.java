package innovasync.ms_tareas.client;

import innovasync.ms_tareas.dto.ProyectoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProyectoClientFallback implements ProyectoClient {

    @Override
    public ResponseEntity<ProyectoDTO> getProyectoById(Long id) {
        // Como es un fallback, significa que la comunicación falló.
        // Retornamos un status 503 (Service Unavailable) y un body nulo.
        // Esto le dirá a nuestro TareaService que no se pudo validar el proyecto.
        return ResponseEntity.status(503).body(null);
    }
}
