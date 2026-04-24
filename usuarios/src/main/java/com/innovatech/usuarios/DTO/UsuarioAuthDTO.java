package com.innovatech.usuarios.DTO;

import lombok.Data;

@Data
public class UsuarioAuthDTO {
    private Long idUser;
    private String email;
    private String contrasena; // ← sí se incluye aquí
    private Long idRol;
    private Long idEstado;
}
