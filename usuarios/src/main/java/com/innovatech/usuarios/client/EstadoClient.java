package com.innovatech.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.innovatech.usuarios.DTO.EstadoDTO;

@FeignClient(name = "estado-service")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")
    EstadoDTO obtenerEstadoPorId(@PathVariable Long id);
}
