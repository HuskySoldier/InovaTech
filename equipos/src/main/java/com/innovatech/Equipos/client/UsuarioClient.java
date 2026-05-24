package com.innovatech.Equipos.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.innovatech.Equipos.dto.UsuarioDTO;

@FeignClient(name = "usuarios", fallback = UsuarioClientFallback.class) // Nombre exacto en Eureka
public interface UsuarioClient {
    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id);

    @GetMapping("/api/usuarios/lista")
    List<UsuarioDTO> obtenerUsuariosBatch(@RequestParam("ids") List<Long> ids);
}
