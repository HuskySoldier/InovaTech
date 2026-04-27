package innovasync.ms_tareas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historial_tarea")
public class HistorialTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia a la tarea
    @ManyToOne
    @JoinColumn(name = "id_tarea")
    private Tarea tarea;

    private LocalDateTime fecha;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}