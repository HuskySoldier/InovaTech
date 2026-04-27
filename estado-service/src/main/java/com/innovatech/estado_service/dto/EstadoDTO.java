package com.innovatech.estado_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoDTO {
    private Long idEstado;
    private String nombre;
    private String descripcion;
    private Long idTipo;
    private String nombreTipo;
}
