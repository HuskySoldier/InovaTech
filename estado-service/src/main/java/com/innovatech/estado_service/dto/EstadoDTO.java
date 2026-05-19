package com.innovatech.estado_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta que representa un Estado y la información de su Tipo asociado")
public class EstadoDTO {
    
    @Schema(description = "ID único del estado", example = "1")
    private Long idEstado;
    
    @Schema(description = "Nombre del estado", example = "EN PROGRESO")
    private String nombre;
    
    @Schema(description = "Descripción detallada del uso de este estado", example = "La entidad se encuentra en ejecución o desarrollo activo")
    private String descripcion;
    
    @Schema(description = "ID del tipo al que pertenece este estado", example = "2")
    private Long idTipo;
    
    @Schema(description = "Nombre del tipo de estado", example = "ESTADO_TAREA")
    private String nombreTipo;
}