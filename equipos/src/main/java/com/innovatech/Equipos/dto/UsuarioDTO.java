package com.innovatech.Equipos.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
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
    private String nombreEstado;
}
