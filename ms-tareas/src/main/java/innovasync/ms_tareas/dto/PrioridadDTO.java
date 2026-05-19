package innovasync.ms_tareas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Objeto de transferencia de datos para crear un nuevo nivel de prioridad")
public class PrioridadDTO {

    @Schema(description = "Nombre descriptivo de la prioridad", example = "ALTA")
    private String nombre;
}