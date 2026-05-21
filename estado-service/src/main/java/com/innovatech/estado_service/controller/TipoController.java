package com.innovatech.estado_service.controller;

import com.innovatech.estado_service.dto.TipoDTO;
import com.innovatech.estado_service.service.TipoService;
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
@RequestMapping("/api/tipos")
@RequiredArgsConstructor
@Tag(name = "Tipos de Estado", description = "API para la gestión de las categorías o tipos a los que pertenecen los estados")
public class TipoController {

    private final TipoService tipoService;

    @Operation(summary = "Obtener todos los tipos", description = "Retorna una lista con todas las categorías de estados disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos recuperada exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<TipoDTO>> getAllTipos() {
        return ResponseEntity.ok(tipoService.getAllTipos());
    }

    @Operation(summary = "Obtener un tipo por ID", description = "Busca y retorna los detalles de un tipo específico mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoDTO> getTipoById(
            @Parameter(description = "ID del tipo a consultar", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(tipoService.getTipoById(id));
    }

    @Operation(summary = "Crear un nuevo tipo", description = "Registra una nueva categoría de estado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la validación o guardado de los datos")
    })
    @PostMapping
    public ResponseEntity<TipoDTO> createTipo(@RequestBody TipoDTO tipoDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tipoService.createTipo(tipoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un tipo", description = "Modifica los datos de un tipo de estado existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al actualizar (datos inválidos o tipo no encontrado)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoDTO> updateTipo(
            @Parameter(description = "ID del tipo a actualizar", example = "1") @PathVariable Long id,
            @RequestBody TipoDTO tipoDTO) {
        try {
            return ResponseEntity.ok(tipoService.updateTipo(id, tipoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar un tipo", description = "Elimina permanentemente un tipo de estado del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo eliminado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error al intentar eliminar el tipo")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipo(
            @Parameter(description = "ID del tipo a eliminar", example = "1") @PathVariable Long id) {
        try {
            tipoService.deleteTipo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}