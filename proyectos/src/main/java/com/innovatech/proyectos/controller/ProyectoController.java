package com.innovatech.proyectos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.proyectos.dto.ProyectoDetalleDTO;
import com.innovatech.proyectos.model.HistorialProyecto;
import com.innovatech.proyectos.model.Proyecto;
import com.innovatech.proyectos.service.ProyectoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/proyectos")
@Tag(name = "Proyectos", description = "API para la gestión del ciclo de vida de proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Operation(summary = "Listar todos los proyectos", description = "Retorna una lista con todos los proyectos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de proyectos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarProyectos());
    }

    @Operation(summary = "Obtener detalle de proyecto por ID", description = "Busca el detalle de un proyecto específico mediante su identificador numérico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle del proyecto encontrado"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/detalle/{id}")
    public ResponseEntity<ProyectoDetalleDTO> obtenerDetallePorId(
            @Parameter(description = "ID del proyecto a buscar", example = "1") 
            @PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerDetallePorId(id));
    }

    @Operation(summary = "Obtener proyecto por ID", description = "Busca un proyecto específico mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto encontrado"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerProyecto(
            @Parameter(description = "ID del proyecto a consultar", required = true) @PathVariable Long id) {
        Proyecto proyecto = proyectoService.obtenerPorId(id);
        return proyecto != null ? ResponseEntity.ok(proyecto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear nuevo proyecto", description = "Registra un nuevo proyecto en el sistema.")
    @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente")
    @PostMapping
    public ResponseEntity<Proyecto> crearProyecto(@RequestBody Proyecto proyecto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.crearProyecto(proyecto));
    }

    @Operation(summary = "Actualizar proyecto", description = "Modifica los datos de un proyecto existente mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizarProyecto(
            @Parameter(description = "ID del proyecto a actualizar", required = true) @PathVariable Long id, 
            @RequestBody Proyecto proyecto) {
        try {
            return ResponseEntity.ok(proyectoService.actualizarProyecto(id, proyecto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar proyecto", description = "Elimina un proyecto del sistema de forma permanente.")
    @ApiResponse(responseCode = "204", description = "Proyecto eliminado exitosamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(
            @Parameter(description = "ID del proyecto a eliminar", required = true) @PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ver historial del proyecto", description = "Obtiene el historial de cambios o eventos de un proyecto específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialProyecto>> verHistorial(
            @Parameter(description = "ID del proyecto para ver historial", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.verHistorial(id));
    }
}