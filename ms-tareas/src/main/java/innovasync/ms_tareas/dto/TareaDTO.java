package innovasync.ms_tareas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Schema(description = "Objeto de transferencia de datos para crear o actualizar una Tarea")
public class TareaDTO {

    @Schema(description = "Nombre descriptivo de la tarea", example = "Desarrollar API de Inventario")
    private String nombre;
    
    @Schema(description = "Detalle completo de las actividades a realizar", example = "Crear CRUD completo para los productos...")
    private String descripcion;
    
    @Schema(description = "Fecha límite para terminar la tarea", example = "2026-06-15")
    private LocalDate fLimiteTerm;
    
    @Schema(description = "Presupuesto inicial asignado (sin decimales)", example = "500000")
    private BigDecimal presupuestoAsignado;
    
    @Schema(description = "Presupuesto final gastado (sin decimales)", example = "480000")
    private BigDecimal presupuestoFinal;
    
    @Schema(description = "Identificador del estado actual de la tarea", example = "2")
    private Long idEstado;
    
    @Schema(description = "Identificador de la prioridad de la tarea", example = "1")
    private Long idPrioridad;

    @Schema(description = "Identificador del proyecto al que pertenece la tarea", example = "10")
    private Long proyectoId;
}