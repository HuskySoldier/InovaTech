package com.innovatech.asignaciones.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "asignaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asig")
    private Long id_asig;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fecha_asignacion;

    @Column(name = "id_tarea", nullable = false)
    private long idTarea;

    @Column(name = "id_integrante", nullable = false)
    private long idIntegrante;

}
