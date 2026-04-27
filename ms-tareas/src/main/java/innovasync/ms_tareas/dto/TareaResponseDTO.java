package innovasync.ms_tareas.dto;

import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
public class TareaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fLimiteTerm;
    private BigDecimal presupuestoAsignado;
    private BigDecimal presupuestoFinal;
    private Long idEstado;
    private String nombreEstado;  // ← viene del MS Estado via Feign
    private Long idPrioridad;
}
