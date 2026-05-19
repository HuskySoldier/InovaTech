package innovasync.ms_tareas.controller;

import innovasync.ms_tareas.model.Prioridad;
import innovasync.ms_tareas.service.PrioridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import innovasync.ms_tareas.dto.PrioridadDTO;

@RestController
@RequestMapping("/api/prioridades")
@CrossOrigin(origins = "*")   // cambiar a la URL de tu frontend en producción
@RequiredArgsConstructor
@Tag(name = "Prioridades", description = "API para la gestión de los niveles de prioridad de las tareas")
public class PrioridadController {

    private final PrioridadService prioridadService;

    @Operation(summary = "Obtener todas las prioridades", description = "Retorna una lista completa de los niveles de prioridad configurados en el sistema.")
    @GetMapping
    public ResponseEntity<List<Prioridad>> obtenerTodas() {
        return ResponseEntity.ok(prioridadService.obtenerTodas());
    }

    @Operation(summary = "Obtener una prioridad por ID", description = "Busca y retorna los detalles de un nivel de prioridad específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Prioridad> obtenerPorId(
            @Parameter(description = "ID de la prioridad a buscar", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(prioridadService.obtenerPorId(id));
    }

    @Operation(summary = "Crear una nueva prioridad", description = "Registra un nuevo nivel de prioridad en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prioridad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Prioridad> crear(@RequestBody PrioridadDTO dto) {
        Prioridad prioridad = new Prioridad();
        prioridad.setNombre(dto.getNombre());
        // Cambiado a HttpStatus.CREATED (201) por buena práctica
        return ResponseEntity.status(HttpStatus.CREATED).body(prioridadService.crear(prioridad));
    }

    @Operation(summary = "Eliminar una prioridad", description = "Elimina permanentemente una prioridad del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Prioridad eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la prioridad a eliminar", example = "1") @PathVariable Long id) {
        prioridadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}