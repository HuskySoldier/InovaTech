package innovasync.ms_tareas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaDetalleDTO {
    private Long idTarea;
    private String nombre;
    private String descripcion;
    private LocalDate fLimiteTerm;
    private BigDecimal presupuestoAsignado;
    private String proyectoNombre;
}
