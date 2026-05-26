package com.innovatech.asignaciones.dto;

import lombok.Data;

@Data
public class IntegranteDTO {
    private Long idIntegrante;
    private Long idUser; // Lo único que nos importa rescatar del equipo
}
