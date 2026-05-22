package innovasync.ms_tareas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora todo lo que no esté en esta clase
public class ProyectoDTO {
    private Long idProyecto;
    private String nombre;
    // Puedes agregar más campos si en el futuro necesitas mostrar información del proyecto
}