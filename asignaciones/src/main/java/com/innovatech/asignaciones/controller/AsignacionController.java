package com.innovatech.asignaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.asignaciones.model.Asignacion;
import com.innovatech.asignaciones.service.AsignacionService;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<List<Asignacion>> listar() {
        return ResponseEntity.ok(asignacionService.listarTodas());
    }

    @PostMapping("/tarea/{idTarea}/integrante/{idIntegrante}")
    public ResponseEntity<?> asignarTarea(@PathVariable Long idTarea, @PathVariable Long idIntegrante) {
        try {
            Asignacion asignacion = asignacionService.crearAsignacion(idTarea, idIntegrante);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}