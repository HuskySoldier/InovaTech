package com.innovatech.privilegios_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.service.PrivilegioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/privilegios")
@Tag(name = "Privilegios", description = "API para la gestión de privilegios del sistema")
public class PrivilegioController {

    @Autowired
    private PrivilegioService privilegioService;

    @Operation(summary = "Listar todos los privilegios", description = "Retorna una lista con todos los privilegios disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de privilegios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<PrivilegioDTO>> getAllPrivilegios() {
        List<PrivilegioDTO> privilegios = privilegioService.obtenerTodos();
        return ResponseEntity.ok(privilegios);
    }

    @Operation(summary = "Obtener privilegio por ID", description = "Busca un privilegio específico mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Privilegio encontrado"),
        @ApiResponse(responseCode = "404", description = "Privilegio no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrivilegioDTO> getPrivilegioById(
            @Parameter(description = "ID del privilegio", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(privilegioService.obtenerPriv(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo privilegio", description = "Registra un nuevo privilegio en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Privilegio creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear el privilegio")
    })
    @PostMapping
    public ResponseEntity<PrivilegioDTO> crearPrivilegio(@RequestBody PrivilegioDTO privilegioDTO) {
        try {
            PrivilegioDTO nuevoPrivilegio = privilegioService.crearPriv(privilegioDTO);
            return ResponseEntity.status(201).body(nuevoPrivilegio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar privilegio", description = "Modifica un privilegio existente mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Privilegio actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Privilegio no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PrivilegioDTO> actualizarPrivilegio(
            @Parameter(description = "ID del privilegio a actualizar", required = true) @PathVariable Long id, 
            @RequestBody PrivilegioDTO privilegioDTO) {
        try {
            PrivilegioDTO privilegioActualizado = privilegioService.actualizarPriv(id, privilegioDTO);
            return ResponseEntity.ok(privilegioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar privilegio", description = "Elimina un privilegio del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Privilegio eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Privilegio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrivilegio(
            @Parameter(description = "ID del privilegio a eliminar", required = true) @PathVariable Long id) {
        try {
            privilegioService.eliminarPriv(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}