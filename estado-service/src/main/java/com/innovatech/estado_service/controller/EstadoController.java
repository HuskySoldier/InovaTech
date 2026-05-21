package com.innovatech.estado_service.controller;

import com.innovatech.estado_service.dto.EstadoDTO;
import com.innovatech.estado_service.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
@Tag(name = "Estados", description = "API para la gestión y consulta de los estados del sistema")
public class EstadoController {

    private final EstadoService estadoService;

    @Operation(summary = "Obtener todos los estados", description = "Retorna una lista completa de todos los estados registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados recuperada exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<EstadoDTO>> getAllEstados() {
        return ResponseEntity.ok(estadoService.getAllEstados());
    }

    @Operation(summary = "Obtener un estado por ID", description = "Busca y retorna los detalles de un estado específico utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getEstadoById(
            @Parameter(description = "ID único del estado", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(estadoService.getEstadoById(id));
    }

    @Operation(summary = "Obtener estados por Tipo", description = "Retorna una lista de estados que pertenecen a un tipo de estado específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estados recuperados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de estado no encontrado")
    })
    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<EstadoDTO>> getEstadosByTipoId(
            @Parameter(description = "ID del tipo de estado para filtrar", example = "2") @PathVariable Long tipoId) {
        return ResponseEntity.ok(estadoService.getEstadosByTipoId(tipoId));
    }

    @Operation(summary = "Crear un nuevo estado", description = "Registra un nuevo estado en el sistema utilizando los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la validación de los datos enviados")
    })
    @PostMapping
    public ResponseEntity<EstadoDTO> createEstado(@RequestBody EstadoDTO estadoDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.createEstado(estadoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un estado", description = "Modifica la información de un estado existente basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la actualización (datos inválidos o estado no encontrado)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> updateEstado(
            @Parameter(description = "ID del estado a actualizar", example = "1") @PathVariable Long id,
            @RequestBody EstadoDTO estadoDTO) {
        try {
            return ResponseEntity.ok(estadoService.updateEstado(id, estadoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar un estado", description = "Elimina permanentemente un estado del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estado eliminado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error al intentar eliminar el estado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(
            @Parameter(description = "ID del estado a eliminar", example = "1") @PathVariable Long id) {
        try {
            estadoService.deleteEstado(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}