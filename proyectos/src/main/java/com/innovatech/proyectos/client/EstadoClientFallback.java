package com.innovatech.proyectos.client;

import com.innovatech.proyectos.dto.EstadoDTO;
import org.springframework.stereotype.Component;

@Component
public class EstadoClientFallback implements EstadoClient {
    @Override
    public EstadoDTO obtenerEstadoPorId(Long id) {
        EstadoDTO fallback = new EstadoDTO();
        fallback.setIdEstado(id);
        fallback.setNombre("Desconocido");
        fallback.setDescripcion("MS Estado no disponible");
        return fallback;
    }
}