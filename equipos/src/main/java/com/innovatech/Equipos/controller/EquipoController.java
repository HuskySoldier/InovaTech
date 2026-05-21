package com.innovatech.Equipos.controller;

import com.innovatech.Equipos.model.Equipo;
import com.innovatech.Equipos.repository.IntegranteRepository;
import com.innovatech.Equipos.service.EquipoService;
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
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
@Tag(name = "Equipos", description = "API para la gestión de equipos de trabajo y sus integrantes")
public class EquipoController {

    private final EquipoService equipoService;
    private final IntegranteRepository integranteRepository;

    @Operation(summary = "Listar todos los equipos", description = "Retorna una lista completa de todos los equipos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos recuperados exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Equipo>> listarEquipos() {
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @Operation(summary = "Obtener un equipo por ID", description = "Busca y retorna los detalles de un equipo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Equipo> obtenerEquipo(
            @Parameter(description = "ID del equipo a consultar", example = "1") @PathVariable Long id) {
        Equipo equipo = equipoService.obtenerPorId(id);
        return equipo != null ? ResponseEntity.ok(equipo) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar equipos por proyecto", description = "Retorna todos los equipos asociados a un ID de proyecto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipos recuperada exitosamente")
    })
    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<List<Equipo>> listarPorProyecto(
            @Parameter(description = "ID del proyecto", example = "1") @PathVariable Long idProyecto) {
        return ResponseEntity.ok(equipoService.listarPorProyecto(idProyecto));
    }

    @Operation(summary = "Crear un nuevo equipo", description = "Registra un nuevo equipo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Equipo> crearEquipo(@RequestBody Equipo equipo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.crearEquipo(equipo));
    }

    @Operation(summary = "Agregar integrante a un equipo", description = "Asocia un usuario existente a un equipo específico mediante sus IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Integrante agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo agregar el integrante (error de validación)")
    })
    @PostMapping("/{idEquipo}/integrantes/{idUser}")
    public ResponseEntity<Equipo> agregarIntegrante(
            @Parameter(description = "ID del equipo", example = "1") @PathVariable Long idEquipo,
            @Parameter(description = "ID del usuario a agregar", example = "10") @PathVariable Long idUser) {
        try {
            return ResponseEntity.ok(equipoService.agregarIntegrante(idEquipo, idUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar un equipo", description = "Elimina permanentemente un equipo del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipo(
            @Parameter(description = "ID del equipo a eliminar", example = "1") @PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener integrante", description = "Endpoint de utilidad para que otros microservicios validen la existencia de un integrante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Integrante encontrado"),
            @ApiResponse(responseCode = "404", description = "Integrante no encontrado")
    })
    @GetMapping("/integrante/{idIntegrante}")
    public ResponseEntity<?> obtenerIntegrante(
            @Parameter(description = "ID del integrante a consultar", example = "5") @PathVariable Long idIntegrante) {
        return integranteRepository.findById(idIntegrante)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}