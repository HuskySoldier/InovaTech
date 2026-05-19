package innovasync.ms_tareas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prioridad")
@Schema(description = "Entidad que representa un nivel de prioridad en la base de datos")
public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la prioridad", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre de la prioridad", example = "ALTA")
    private String nombre;
}