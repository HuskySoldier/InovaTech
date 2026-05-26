package com.innovatech.asignaciones.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innovatech.asignaciones.dto.ProyectoDTO;

@FeignClient(name = "proyectos")
public interface ProyectoClient {
    @GetMapping("/api/proyectos/batch")
    List<ProyectoDTO> obtenerProyectosBatch(@RequestParam("ids") List<Long> ids);
}
