package com.innovatech.usuarios.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Long idUser;
    private String run;
    private String nombre;
    private String apellido;
    private String nombreCompleto; // Campo calculado
    private Date fechaNacimiento;
    private String email;
    private String imgPerfil;
    private String nombreCargo; // Ya no enviamos el objeto Cargo, solo su nombre
    private Long idRol;
    private Long idEstado;
}