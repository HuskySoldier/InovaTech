package com.innovatech.proyectos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "f_inicio")
    private LocalDate fechaInicio;

    @Column(name = "f_termino_esti")
    private LocalDate fechaTerminoEsti;

    @Column(name = "f_termino_real")
    private LocalDate fechaTerminoReal;

    @Column(name = "presu_estimado")
    private BigDecimal presuEstimado;

    @Column(name = "presup_real")
    private BigDecimal presupReal;

    @Column(name = "id_estado")
    private Long idEstado; // Referencia al MS Estado

    // Relación bidireccional opcional para traer el historial
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistorialProyecto> historiales;
}