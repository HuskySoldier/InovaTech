package com.innovatech.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/auth")
    public ResponseEntity<String> authFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de autenticación no disponible");
    }

    @RequestMapping("/usuarios")
    public ResponseEntity<String> usuariosFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de usuarios no disponible");
    }

    @RequestMapping("/privilegios")
    public ResponseEntity<String> privilegiosFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de privilegios no disponible");
    }

    @RequestMapping("/estado")
    public ResponseEntity<String> estadoFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de estados no disponible");
    }

    @RequestMapping("/proyectos")
    public ResponseEntity<String> proyectosFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de proyectos no disponible");
    }

    @RequestMapping("/equipos")
    public ResponseEntity<String> equiposFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de equipos no disponible");
    }

    @RequestMapping("/tareas")
    public ResponseEntity<String> tareasFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de tareas no disponible");
    }

    @RequestMapping("/asignaciones")
    public ResponseEntity<String> asignacionesFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio de asignaciones no disponible");
    }
}
