package innovasync.ms_tareas.service;

import innovasync.ms_tareas.model.HistorialTarea;
import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.repository.HistorialTareaRepository;
import innovasync.ms_tareas.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialTareaService {

    private final HistorialTareaRepository historialTareaRepository;
    private final TareaRepository tareaRepository;

    public List<HistorialTarea> obtenerPorTarea(Long tareaId) {
        return historialTareaRepository.findByTareaId(tareaId);
    }

    public HistorialTarea registrar(Long tareaId, String descripcion) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        HistorialTarea historial = new HistorialTarea();
        historial.setTarea(tarea);
        historial.setFecha(LocalDateTime.now());
        historial.setDescripcion(descripcion);
        return historialTareaRepository.save(historial);
    }
}