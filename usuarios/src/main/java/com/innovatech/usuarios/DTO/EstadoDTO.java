package com.innovatech.usuarios.DTO;

import lombok.Data;

@Data
public class EstadoDTO {
    private Long idEstado;
    private String nombre;
    private String descripcion;
    private Long idTipo;
    private String nombreTipo;
}