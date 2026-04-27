package com.innovatech.asignaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tareas")
public interface TareaClient {
    @GetMapping("/api/tareas/{id}")
    Object obtenerTareaPorId(@PathVariable("id") Long id);
}