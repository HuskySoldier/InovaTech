package innovasync.ms_tareas.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prioridad")
public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
}