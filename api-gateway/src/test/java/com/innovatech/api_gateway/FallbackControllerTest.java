package com.innovatech.api_gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.innovatech.api_gateway.controller.FallbackController;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FallbackControllerTest {

    private FallbackController fallbackController;

    @BeforeEach
    void setUp() {
        // Simplemente creamos el objeto manualmente
        fallbackController = new FallbackController();
    }

    @ParameterizedTest
    @CsvSource({
        "/auth, Servicio de autenticación no disponible",
        "/usuarios, Servicio de usuarios no disponible",
        "/privilegios, Servicio de privilegios no disponible",
        "/estado, Servicio de estados no disponible",
        "/proyectos, Servicio de proyectos no disponible",
        "/equipos, Servicio de equipos no disponible",
        "/tareas, Servicio de tareas no disponible",
        "/asignaciones, Servicio de asignaciones no disponible"
    })
    void testTodosLosFallbacks(String endpoint, String mensajeEsperado) {
        
        ResponseEntity<String> response = null;

        // Llamamos al método correspondiente según el endpoint
        switch (endpoint) {
            case "/auth" -> response = fallbackController.authFallback();
            case "/usuarios" -> response = fallbackController.usuariosFallback();
            case "/privilegios" -> response = fallbackController.privilegiosFallback();
            case "/estado" -> response = fallbackController.estadoFallback();
            case "/proyectos" -> response = fallbackController.proyectosFallback();
            case "/equipos" -> response = fallbackController.equiposFallback();
            case "/tareas" -> response = fallbackController.tareasFallback();
            case "/asignaciones" -> response = fallbackController.asignacionesFallback();
        }

        // Verificamos el resultado
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(mensajeEsperado, response.getBody());
    }
}