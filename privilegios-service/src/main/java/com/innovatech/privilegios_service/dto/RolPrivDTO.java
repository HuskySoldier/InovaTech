package com.innovatech.privilegios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolPrivDTO {
    private Long idRolPriv;
    private Long idRol;
    private Long idPriv;
}
