package com.innovatech.Equipos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegranteDetalleDTO {
    private Long idIntegrante;
    private String nombreUsuario; // Nombre del usuario obtenido desde MS Usuarios
    private String nombreEquipo;
}
