package com.innovatech.asignaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemanaDetalleDTO {
    private int semana;
    private int horas;
    private String proyecto;
}