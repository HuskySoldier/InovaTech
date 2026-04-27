package com.innovatech.asignaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "equipos")
public interface EquipoClient {
    // Apunta al MS Equipos para validar que el integrante (y no solo el equipo) exista
    @GetMapping("/api/equipos/integrante/{idIntegrante}")
    Object obtenerIntegrantePorId(@PathVariable("idIntegrante") Long idIntegrante);
}