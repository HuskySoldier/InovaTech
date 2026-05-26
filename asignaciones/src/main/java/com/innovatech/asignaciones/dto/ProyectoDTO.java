package com.innovatech.asignaciones.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyectoDTO {
    private Long idProyecto;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaTermEst;
    private BigDecimal presupuestoEst;
}