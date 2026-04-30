package com.innovatech.proyectos.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.innovatech.proyectos.dto.EstadoDTO;

@FeignClient(name = "estado-service")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")
    EstadoDTO obtenerEstadoPorId(@PathVariable Long id);
}