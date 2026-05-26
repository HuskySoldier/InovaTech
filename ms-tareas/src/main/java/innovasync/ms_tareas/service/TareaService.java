package innovasync.ms_tareas.service;

import innovasync.ms_tareas.client.EstadoClient;
import innovasync.ms_tareas.client.ProyectoClient;
import innovasync.ms_tareas.config.RabbitMQConfig; // Agregado
import innovasync.ms_tareas.dto.EstadoResponse;
import innovasync.ms_tareas.dto.ProyectoDTO;
import innovasync.ms_tareas.dto.TareaDTO;
import innovasync.ms_tareas.dto.TareaDetalleDTO;
import innovasync.ms_tareas.dto.TareaResponseDTO;
import innovasync.ms_tareas.model.Prioridad;
import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.repository.PrioridadRepository;
import innovasync.ms_tareas.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate; // Agregado
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final EstadoClient estadoClient;
    private final RabbitTemplate rabbitTemplate; // Inyectamos RabbitTemplate para enviar mensajes
    private final ProyectoClient proyectoClient;
    private final PrioridadRepository prioridadRepository;

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

    public TareaResponseDTO crear(TareaDTO dto) {
        ResponseEntity<ProyectoDTO> responseProyecto = proyectoClient.getProyectoById(dto.getProyectoId());

        if (responseProyecto == null || !responseProyecto.getStatusCode().is2xxSuccessful()
                || responseProyecto.getBody() == null) {
            throw new IllegalArgumentException("Error: El proyecto con ID " + dto.getProyectoId()
                    + " no existe o el servicio de proyectos no está disponible.");
        }

        Prioridad prioridad = prioridadRepository.findById(dto.getIdPrioridad())
                .orElseThrow(() -> new RuntimeException("Error: La prioridad no existe."));

        Tarea tarea = new Tarea();
        tarea.setNombre(dto.getNombre());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setFLimiteTerm(dto.getFLimiteTerm());
        tarea.setPresupuestoAsignado(dto.getPresupuestoAsignado());
        tarea.setPresupuestoFinal(dto.getPresupuestoFinal());
        tarea.setIdEstado(dto.getIdEstado());
        tarea.setProyectoId(dto.getProyectoId());
        tarea.setPrioridad(prioridad);
        Tarea tareaGuardada = tareaRepository.save(tarea);
        TareaResponseDTO response = toResponseDTO(tareaGuardada);

        // ==========================================
        // EMISIÓN DE EVENTO A RABBITMQ
        // ==========================================
        // System.out.println("Enviando evento de nueva tarea a RabbitMQ...");
        // rabbitTemplate.convertAndSend(
        // RabbitMQConfig.EXCHANGE_TAREAS,
        // RabbitMQConfig.ROUTING_KEY_TAREA_CREADA,
        // "Nueva tarea creada con ID: " + tareaGuardada.getId()
        // );

        return response;
    }

    public TareaDetalleDTO obtenerDetalle(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return toDetalleDTO(tarea);
    }

    public TareaResponseDTO actualizar(Long id, TareaDTO dto) {
        Tarea existente = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        Prioridad nuevaPrioridad = prioridadRepository.findById(dto.getIdPrioridad())
                .orElseThrow(() -> new RuntimeException("Error: La prioridad no existe."));
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setFLimiteTerm(dto.getFLimiteTerm());
        existente.setPresupuestoAsignado(dto.getPresupuestoAsignado());
        existente.setPresupuestoFinal(dto.getPresupuestoFinal());
        existente.setIdEstado(dto.getIdEstado());
        existente.setPrioridad(nuevaPrioridad);
        existente.setProyectoId(dto.getProyectoId());
        return toResponseDTO(tareaRepository.save(existente));
    }


    public List<TareaResponseDTO> obtenerTareasBatch(List<Long> ids) {
        // Verifica que la lista no venga vacía o nula para evitar errores en base de datos
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>(); 
        }
        return tareaRepository.findAllById(ids).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    public List<TareaResponseDTO> obtenerPorProyectoId(Long proyectoId) {
        return tareaRepository.findByProyectoId(proyectoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
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
        dto.setIdPrioridad(tarea.getPrioridad().getId());
        dto.setProyectoId(tarea.getProyectoId());

        // Llama al MS Estado via Feign para obtener el nombre
        try {
            EstadoResponse estado = estadoClient.obtenerEstado(tarea.getIdEstado());
            dto.setNombreEstado(estado != null ? estado.getNombre() : "Sin estado");
        } catch (Exception e) {
            // Si el MS Estado no está disponible, no falla
            dto.setNombreEstado("Sin estado");
        }

        return dto;
    }

    private TareaDetalleDTO toDetalleDTO(Tarea tarea) {
        TareaDetalleDTO dto = new TareaDetalleDTO();
        dto.setIdTarea(tarea.getId());
        dto.setNombre(tarea.getNombre());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setFLimiteTerm(tarea.getFLimiteTerm());
        dto.setPresupuestoAsignado(tarea.getPresupuestoAsignado());
        try {
            ProyectoDTO proyecto = proyectoClient.getProyectoById(tarea.getProyectoId()).getBody();
            dto.setProyectoNombre(proyecto != null ? proyecto.getNombre() : "Nombre del Proyecto no disponible");
        } catch (Exception e) {
            dto.setProyectoNombre("Nombre del Proyecto no disponible");
            return dto;
        }

        return dto;
    }
}