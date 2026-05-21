package com.innovatech.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.usuarios.model.Cargo;
import com.innovatech.usuarios.service.CargoService;

// Anotaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cargos")
@Tag(name = "Cargos", description = "API para la gestión de cargos de usuario")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @Operation(summary = "Listar todos los cargos", description = "Retorna una lista completa de todos los cargos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de cargos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Cargo>> listarCargos() {
        return ResponseEntity.ok(cargoService.listarCargos());
    }

    @Operation(summary = "Obtener cargo por ID", description = "Busca un cargo específico por su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cargo encontrado"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cargo> obtenerCargoPorId(
            @Parameter(description = "ID del cargo a consultar", required = true) @PathVariable Long id) {
        Cargo cargo = cargoService.obtenerCargoPorId(id);
        if (cargo != null) {
            return ResponseEntity.ok(cargo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nuevo cargo", description = "Registra un nuevo cargo en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cargo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud, datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Cargo> crearCargo(@RequestBody Cargo cargo) {
        try {
            Cargo nuevoCargo = cargoService.crearCargo(cargo);
            return ResponseEntity.created(null).body(nuevoCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar cargo", description = "Actualiza los datos de un cargo existente mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cargo actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cargo> actualizarCargo(
            @Parameter(description = "ID del cargo a actualizar", required = true) @PathVariable Long id, 
            @RequestBody Cargo cargo) {
        try {
            Cargo cargoActualizado = cargoService.actualizarCargo(id, cargo);
            return ResponseEntity.ok(cargoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar cargo", description = "Elimina un cargo del sistema de forma permanente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cargo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCargo(
            @Parameter(description = "ID del cargo a eliminar", required = true) @PathVariable Long id) {
        try {
            cargoService.eliminarCargo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}