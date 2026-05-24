package innovasync.ms_tareas.controller;

import innovasync.ms_tareas.dto.TareaDTO;
import innovasync.ms_tareas.dto.TareaDetalleDTO;
import innovasync.ms_tareas.dto.TareaResponseDTO;
import innovasync.ms_tareas.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
@Tag(name = "Tareas", description = "API para la gestión de tareas del sistema")
public class TareaController {

    private final TareaService tareaService;

    @Operation(summary = "Obtener todas las tareas", description = "Retorna una lista completa de todas las tareas registradas con sus estados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas recuperada exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<TareaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(tareaService.obtenerTodas());
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<TareaDetalleDTO> obtenerDetallePorId(
            @Parameter(description = "ID de la tarea a buscar", example = "1") 
            @PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerDetalle(id));
    }

    @Operation(summary = "Obtener una tarea por ID", description = "Busca y retorna los detalles de una tarea específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la tarea a buscar", example = "1") 
            @PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    @Operation(summary = "Obtener tareas por ID de proyecto", description = "Retorna una lista de tareas asociadas a un proyecto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tareas encontradas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<TareaResponseDTO>> obtenerPorProyectoId(
            @Parameter(description = "ID del proyecto", example = "10")
            @PathVariable Long proyectoId) {
        return ResponseEntity.ok(tareaService.obtenerPorProyectoId(proyectoId));
    }

    @Operation(summary = "Crear una nueva tarea", description = "Registra una nueva tarea en el sistema a partir de los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(tareaDTO));
    }

    @Operation(summary = "Actualizar una tarea", description = "Modifica los datos de una tarea existente según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizar(
            @Parameter(description = "ID de la tarea a actualizar", example = "1") 
            @PathVariable Long id, 
            @Valid @RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.ok(tareaService.actualizar(id, tareaDTO));
    }

    @Operation(summary = "Eliminar una tarea", description = "Elimina permanentemente una tarea del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada con éxito (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la tarea a eliminar", example = "1") 
            @PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}