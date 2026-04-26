package com.innovatech.privilegios_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDetalleDTO {
    private Long idRol;
    private String nombre;
    private List<PrivilegioDTO> privilegios;
}
