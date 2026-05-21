package com.innovatech.privilegios_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.privilegios_service.dto.RolDTO;
import com.innovatech.privilegios_service.service.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "API para la gestión de roles del sistema")
public class RolController {

    @Autowired
    private RolService rolService;

    @Operation(summary = "Listar todos los roles", description = "Retorna una lista completa de todos los roles configurados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        List<RolDTO> roles = rolService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Obtener rol por ID", description = "Busca un rol específico mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> getRolById(
            @Parameter(description = "ID del rol a consultar", required = true) @PathVariable Long id) {
        RolDTO rol = rolService.getRolById(id);
        return (rol != null) ? ResponseEntity.ok(rol) : ResponseEntity.notFound().build();
    }
    
    @Operation(summary = "Obtener detalle de rol", description = "Retorna el detalle completo de un rol, incluyendo sus privilegios asociados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle del rol obtenido"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> obtenerDetalle(
            @Parameter(description = "ID del rol para ver detalles", required = true) @PathVariable Long id) {
        try {
            var detalle = rolService.obtenerDetalle(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo rol", description = "Registra un nuevo rol en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos")
    })
    @PostMapping
    public ResponseEntity<RolDTO> crearRol(@RequestBody RolDTO rolDTO) {
        try {
            RolDTO nuevoRol = rolService.crearRol(rolDTO);
            return ResponseEntity.status(201).body(nuevoRol);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar rol", description = "Modifica un rol existente mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> actualizarRol(
            @Parameter(description = "ID del rol a actualizar", required = true) @PathVariable Long id, 
            @RequestBody RolDTO rolDTO) {
        try {
            RolDTO rolActualizado = rolService.actualizarRol(id, rolDTO);
            return ResponseEntity.ok(rolActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar rol", description = "Elimina un rol del sistema permanentemente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(
            @Parameter(description = "ID del rol a eliminar", required = true) @PathVariable Long id) {
        try {
            rolService.eliminarRol(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}