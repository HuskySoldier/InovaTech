package com.innovatech.proyectos.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- sirve para evitar la serialización del proyecto en el historial
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "historial_proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_h_proyecto")
    private Long idHProyecto;

    @JsonIgnore // <-- AGREGA ESTA ANOTACIÓN AQUÍ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}