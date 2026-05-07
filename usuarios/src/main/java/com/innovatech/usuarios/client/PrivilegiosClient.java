package com.innovatech.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.innovatech.usuarios.DTO.RolDTO;

// "ms-privilegios" debe ser el spring.application.name del otro microservicio
@FeignClient(name = "privilegios-service", fallback = PrivilegiosClientFallback.class)
public interface PrivilegiosClient {

    @GetMapping("/api/roles/{id}")
    RolDTO obtenerRolPorId(@PathVariable Long id);
}
