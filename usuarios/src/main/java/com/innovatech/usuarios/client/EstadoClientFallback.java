package com.innovatech.usuarios.client;

import org.springframework.stereotype.Component;
import com.innovatech.usuarios.DTO.EstadoDTO;

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
