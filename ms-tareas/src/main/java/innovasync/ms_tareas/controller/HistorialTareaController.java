package innovasync.ms_tareas.controller;

import innovasync.ms_tareas.model.HistorialTarea;
import innovasync.ms_tareas.service.HistorialTareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-tareas")
@RequiredArgsConstructor
@Tag(name = "Historial de Tareas", description = "API para registrar y consultar los eventos o cambios de estado de las tareas")
public class HistorialTareaController {

    private final HistorialTareaService historialTareaService;

    @Operation(summary = "Obtener el historial de una tarea", description = "Retorna una lista con todos los registros históricos asociados a un ID de tarea específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial recuperado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @GetMapping("/tarea/{tareaId}")
    public ResponseEntity<List<HistorialTarea>> obtenerPorTarea(
            @Parameter(description = "ID de la tarea para consultar su historial", example = "10") 
            @PathVariable Long tareaId) {
        return ResponseEntity.ok(historialTareaService.obtenerPorTarea(tareaId));
    }

    @Operation(summary = "Registrar un nuevo evento en el historial", description = "Crea un nuevo registro en el historial de la tarea especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro de historial creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @PostMapping("/tarea/{tareaId}")
    public ResponseEntity<HistorialTarea> registrar(
            @Parameter(description = "ID de la tarea a la que se le agregará el historial", example = "10") 
            @PathVariable Long tareaId,
            @RequestBody(description = "Descripción del evento o cambio realizado", 
                         content = @Content(mediaType = "text/plain", 
                         schema = @Schema(example = "Se actualizó la fecha límite de la tarea"))) 
            @org.springframework.web.bind.annotation.RequestBody String descripcion) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(historialTareaService.registrar(tareaId, descripcion));
    }
}