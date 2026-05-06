package innovasync.ms_tareas.client;


import org.springframework.stereotype.Component;

import innovasync.ms_tareas.dto.EstadoResponse;

@Component
public class EstadoClientFallback implements EstadoClient {

    @Override
    public EstadoResponse obtenerEstado(Long id) {
        // Devuelve un estado por defecto cuando MS Estado no responde
        EstadoResponse fallback = new EstadoResponse();
        fallback.setId(id);
        fallback.setNombre("Desconocido");
        fallback.setDescripcion("MS Estado no disponible");
        return fallback;
    }
}