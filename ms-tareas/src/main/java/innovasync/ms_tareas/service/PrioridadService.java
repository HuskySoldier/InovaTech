package innovasync.ms_tareas.service;

import innovasync.ms_tareas.model.Prioridad;
import innovasync.ms_tareas.repository.PrioridadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;

    // Obtener todas las prioridades
    public List<Prioridad> obtenerTodas() {
        return prioridadRepository.findAll();
    }

    // Obtener prioridad por ID
    public Prioridad obtenerPorId(Long id) {
        return prioridadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada"));
    }

    // Crear nueva prioridad
    public Prioridad crear(Prioridad prioridad) {
        return prioridadRepository.save(prioridad);
    }

    // Eliminar prioridad
    public void eliminar(Long id) {
        prioridadRepository.deleteById(id);
    }
}