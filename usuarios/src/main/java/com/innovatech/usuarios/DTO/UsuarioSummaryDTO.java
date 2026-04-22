package com.innovatech.usuarios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta DTO se utiliza para enviar un resumen de la información del usuario, 
// por ejemplo, en una lista de usuarios o en un perfil público, 
// donde no es necesario enviar toda la información detallada del usuario.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSummaryDTO {
    private Long idUser;
    private String nombreCompleto;
    private String email;
    private String nombreCargo;
}
