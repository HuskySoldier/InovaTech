package innovasync.ms_tareas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Schema(description = "Objeto de respuesta que incluye todos los detalles de la Tarea y datos de otros microservicios")
public class TareaResponseDTO {
    
    @Schema(description = "ID único de la tarea", example = "10")
    private Long id;
    
    @Schema(description = "Nombre descriptivo de la tarea", example = "Desarrollar API de Inventario")
    private String nombre;
    
    @Schema(description = "Detalle completo de las actividades a realizar", example = "Crear CRUD completo para los productos...")
    private String descripcion;
    
    @Schema(description = "Fecha límite para terminar la tarea", example = "2026-06-15")
    private LocalDate fLimiteTerm;
    
    @Schema(description = "Presupuesto inicial asignado", example = "500000")
    private BigDecimal presupuestoAsignado;
    
    @Schema(description = "Presupuesto final gastado", example = "480000")
    private BigDecimal presupuestoFinal;
    
    @Schema(description = "Identificador del estado", example = "2")
    private Long idEstado;
    
    @Schema(description = "Nombre del estado obtenido dinámicamente", example = "EN PROGRESO")
    private String nombreEstado; 
    
    @Schema(description = "Identificador de la prioridad", example = "1")
    private Long idPrioridad;
}