package com.innovatech.estado_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.estado_service.dto.TipoDTO;
import com.innovatech.estado_service.service.TipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tipos")
@RequiredArgsConstructor
@Tag(name = "Tipos de Estado", description = "API para la gestión de las categorías o tipos a los que pertenecen los estados")
public class TipoController {

    private final TipoService tipoService;

    @Operation(summary = "Obtener todos los tipos", description = "Retorna una lista con todas las categorías de estados disponibles.")
    @GetMapping
    public ResponseEntity<List<TipoDTO>> getAllTipos() {
        List<TipoDTO> tipos = tipoService.getAllTipos();
        return ResponseEntity.ok(tipos);
    }

    @Operation(summary = "Obtener un tipo por ID", description = "Busca y retorna los detalles de un tipo específico mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoDTO> getTipoById(
            @Parameter(description = "ID del tipo a consultar", example = "1") @PathVariable Long id) {
        TipoDTO tipo = tipoService.getTipoById(id);
        return ResponseEntity.ok(tipo);
    }

    @Operation(summary = "Crear un nuevo tipo", description = "Registra una nueva categoría de estado en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la validación o guardado de los datos")
    })
    @PostMapping
    public ResponseEntity<TipoDTO> createTipo(@RequestBody TipoDTO tipoDTO) {
        try {
            TipoDTO saved = tipoService.createTipo(tipoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
            TipoDTO updated = tipoService.updateTipo(id, tipoDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @Operation(summary = "Eliminar un tipo", description = "Elimina permanentemente un tipo de estado del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo eliminado con éxito (Sin contenido)"),
        @ApiResponse(responseCode = "400", description = "Error al intentar eliminar el tipo (posiblemente esté siendo usado por algún estado)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipo(
            @Parameter(description = "ID del tipo a eliminar", example = "1") @PathVariable Long id) {
        try {
            tipoService.deleteTipo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}