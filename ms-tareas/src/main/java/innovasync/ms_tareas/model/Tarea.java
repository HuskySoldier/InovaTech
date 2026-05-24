package innovasync.ms_tareas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank valida que el nombre no sea vacío ni null
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // @NotNull valida que la fecha no sea null
    @NotNull(message = "La fecha límite es obligatoria")
    private LocalDate fLimiteTerm;

    private BigDecimal presupuestoAsignado;  //bigDecimal porque es exacto sin decimales
    private BigDecimal presupuestoFinal;
    private Long idEstado;
    // AGREGAMOS: La relación con el objeto Prioridad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prioridad", nullable = false)
    private Prioridad prioridad;


    @NotNull(message = "El ID del proyecto es obligatorio")
    @Column(name = "proyecto_id", nullable = false)
    private Long proyectoId;
}