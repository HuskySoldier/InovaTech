package com.innovatech.usuarios.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
    private String run;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String correo;
    private String clave; // Se recibe en crudo y el MS la encripta
    private String imgPerfil;
    private Long idCargo; // Solo el ID para la relación ManyToOne local
    private Long idRol;   // ID de referencia al microservicio de Privilegios
    private Long idEstado; // ID de referencia al microservicio de Estados
}