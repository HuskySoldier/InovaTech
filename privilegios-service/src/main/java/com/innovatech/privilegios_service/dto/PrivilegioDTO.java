package com.innovatech.privilegios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegioDTO {
    private Long idPriv;
    private String nombre;
    private String descripcion;
}
