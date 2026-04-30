package com.innovatech.Equipos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "proyectos")
public interface ProyectoClient {

    @GetMapping("/api/proyectos/{id}")
    Object obtenerProyectoPorId(@PathVariable Long id);
}