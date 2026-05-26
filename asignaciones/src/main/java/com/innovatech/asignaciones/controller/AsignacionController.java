package com.innovatech.asignaciones.controller;

import com.innovatech.asignaciones.dto.RespuestaSemanasDTO;
import com.innovatech.asignaciones.model.Asignacion;
import com.innovatech.asignaciones.service.AsignacionService;
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
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
@Tag(name = "Asignaciones", description = "API para gestionar la asignación de tareas a los integrantes del equipo")
public class AsignacionController {

    private final AsignacionService asignacionService;

    @Operation(summary = "Listar todas las asignaciones", description = "Retorna una lista completa de todas las asignaciones de tareas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignaciones recuperadas exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Asignacion>> listar() {
        return ResponseEntity.ok(asignacionService.listarTodas());
    }

    @Operation(summary = "Crear una nueva asignación", description = "Registra una nueva relación entre una tarea y un integrante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asignación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al realizar la asignación (datos inválidos o conflicto)")
    })
    @PostMapping("/tarea/{idTarea}/integrante/{idIntegrante}")
    public ResponseEntity<?> asignarTarea(
            @Parameter(description = "ID de la tarea a asignar", example = "1") @PathVariable Long idTarea,
            @Parameter(description = "ID del integrante que recibe la tarea", example = "5") @PathVariable Long idIntegrante) {
        try {
            Asignacion asignacion = asignacionService.crearAsignacion(idTarea, idIntegrante);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/usuario/{idUser}/semanas")
    public ResponseEntity<RespuestaSemanasDTO> obtenerSemanas(@PathVariable Long idUser) {
        
        // Llamamos a tu método y devolvemos un status 200 OK
        RespuestaSemanasDTO respuesta = asignacionService.obtenerCargaSemanal2(idUser);
        
        return ResponseEntity.ok(respuesta);
    }
}