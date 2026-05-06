package innovasync.ms_tareas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoResponse {
    public Long id;
    public String nombre;
    public String descripcion;
}
