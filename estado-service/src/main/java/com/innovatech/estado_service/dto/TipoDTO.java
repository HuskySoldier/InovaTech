package com.innovatech.estado_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de transferencia que representa una categoría o tipo de estado")
public class TipoDTO {
    
    @Schema(description = "ID único del tipo de estado", example = "1")
    private Long idTipo;
    
    @Schema(description = "Nombre de la categoría o tipo", example = "ESTADO_TAREA")
    private String nombre;
}