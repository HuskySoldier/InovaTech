package innovasync.ms_tareas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@Table(name = "historial_tarea")
@Schema(description = "Entidad que representa el registro histórico de eventos en una tarea")
public class HistorialTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro en el historial", example = "100")
    private Long id;

    // Referencia a la tarea
    @ManyToOne
    @JoinColumn(name = "id_tarea")
    @Schema(description = "Tarea a la que pertenece este registro histórico")
    private Tarea tarea;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha y hora exacta en la que se registró el evento", example = "2026-05-19T10:30:00")
    private LocalDateTime fecha;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descripción detallada del evento o modificación realizada", example = "Se modificó el presupuesto asignado")
    private String descripcion;
}