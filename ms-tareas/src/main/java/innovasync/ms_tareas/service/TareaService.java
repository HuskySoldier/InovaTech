package innovasync.ms_tareas.service;

import innovasync.ms_tareas.client.EstadoClient;
import innovasync.ms_tareas.client.EstadoClient.EstadoResponse;
import innovasync.ms_tareas.dto.TareaDTO;
import innovasync.ms_tareas.dto.TareaResponseDTO;
import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final EstadoClient estadoClient;

    public List<TareaResponseDTO> obtenerTodas() {
        return tareaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TareaResponseDTO obtenerPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return toResponseDTO(tarea);
    }

    public Tarea crear(TareaDTO dto) {
        Tarea tarea = new Tarea();
        tarea.setNombre(dto.getNombre());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setFLimiteTerm(dto.getFLimiteTerm());
        tarea.setPresupuestoAsignado(dto.getPresupuestoAsignado());
        tarea.setPresupuestoFinal(dto.getPresupuestoFinal());
        tarea.setIdEstado(dto.getIdEstado());
        tarea.setIdPrioridad(dto.getIdPrioridad());
        return tareaRepository.save(tarea);
    }

    public Tarea actualizar(Long id, TareaDTO dto) {
        Tarea existente = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setFLimiteTerm(dto.getFLimiteTerm());
        existente.setPresupuestoAsignado(dto.getPresupuestoAsignado());
        existente.setPresupuestoFinal(dto.getPresupuestoFinal());
        existente.setIdEstado(dto.getIdEstado());
        existente.setIdPrioridad(dto.getIdPrioridad());
        return tareaRepository.save(existente);
    }

    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    // Convierte Tarea a TareaResponseDTO con nombre del estado
    private TareaResponseDTO toResponseDTO(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        dto.setId(tarea.getId());
        dto.setNombre(tarea.getNombre());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setFLimiteTerm(tarea.getFLimiteTerm());
        dto.setPresupuestoAsignado(tarea.getPresupuestoAsignado());
        dto.setPresupuestoFinal(tarea.getPresupuestoFinal());
        dto.setIdEstado(tarea.getIdEstado());
        dto.setIdPrioridad(tarea.getIdPrioridad());

        // Llama al MS Estado via Feign para obtener el nombre
        try {
            EstadoResponse estado = estadoClient.obtenerEstado(tarea.getIdEstado());
            dto.setNombreEstado(estado != null ? estado.nombre : "Sin estado");
        } catch (Exception e) {
            // Si el MS Estado no está disponible, no falla
            dto.setNombreEstado("Sin estado");
        }

        return dto;
    }
}