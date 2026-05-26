package com.innovatech.asignaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.innovatech.asignaciones.dto.IntegranteDTO;

@FeignClient(name = "equipos", fallback = EquipoClientFallback.class)
public interface EquipoClient {
    @GetMapping("/api/equipos/integrante/{idIntegrante}")
    IntegranteDTO obtenerIntegrantePorId(@PathVariable("idIntegrante") Long idIntegrante);
}