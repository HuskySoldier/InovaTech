package innovasync.ms_tareas.dto;

import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

// DTO = Data Transfer Object
// Controla qué datos se reciben y envían al frontend
// Así no exponemos directamente el modelo de base de datos
@Data
public class TareaDTO {

    private String nombre;
    private String descripcion;
    private LocalDate fLimiteTerm;
    private BigDecimal presupuestoAsignado;
    private BigDecimal presupuestoFinal;
    private Long idEstado;
    private Long idPrioridad;
}