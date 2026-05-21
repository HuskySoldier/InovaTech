package com.innovatech.privilegios_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.service.RolPrivService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles y Privilegios", description = "API para gestionar la asociación entre roles y privilegios")
public class RolPrivController {

    @Autowired
    private RolPrivService rolPrivService;

    @Operation(summary = "Listar privilegios por rol", description = "Obtiene la lista de todos los privilegios asociados a un rol específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Privilegios encontrados exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{id}/privilegios")
    public ResponseEntity<List<PrivilegioDTO>> listarPrivilegiosPorRol(
            @Parameter(description = "ID del rol a consultar", required = true) @PathVariable Long id) {
        try {
            List<PrivilegioDTO> privilegios = rolPrivService.listarPrivilegiosPorRol(id);
            return ResponseEntity.ok(privilegios);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Asignar privilegio a rol", description = "Asocia un privilegio específico a un rol determinado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Privilegio asignado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en la asignación")
    })
    @PostMapping("/{idRol}/privilegios/{idPriv}")
    public ResponseEntity<Void> asignarPrivilegio(
            @Parameter(description = "ID del rol", required = true) @PathVariable Long idRol, 
            @Parameter(description = "ID del privilegio a asignar", required = true) @PathVariable Long idPriv) {
        try {
            rolPrivService.asignarPrivilegio(idRol, idPriv);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Quitar privilegio a rol", description = "Elimina la asociación de un privilegio de un rol determinado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Privilegio desvinculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol o Privilegio no encontrado")
    })
    @DeleteMapping("/{idRol}/privilegios/{idPriv}")
    public ResponseEntity<Void> quitarPrivilegio(
            @Parameter(description = "ID del rol", required = true) @PathVariable Long idRol, 
            @Parameter(description = "ID del privilegio a quitar", required = true) @PathVariable Long idPriv) {
        try {
            rolPrivService.quitarPrivilegio(idRol, idPriv);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}