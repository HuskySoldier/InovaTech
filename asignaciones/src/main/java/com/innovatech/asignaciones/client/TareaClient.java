package com.innovatech.asignaciones.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.innovatech.asignaciones.dto.TareaDTO;

@FeignClient(name = "ms-tareas", fallback = TareaClientFallback.class)
public interface TareaClient {
    @GetMapping("/api/tareas/{id}")
    TareaDTO obtenerTareaPorId(@PathVariable("id") Long id);

    @GetMapping("/api/tareas/batch")
    List<TareaDTO> obtenerTareasBatch(@RequestParam("ids") List<Long> ids);
}