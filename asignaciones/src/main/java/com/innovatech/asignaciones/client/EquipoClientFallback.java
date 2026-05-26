package com.innovatech.asignaciones.client;


import org.springframework.stereotype.Component;

import com.innovatech.asignaciones.dto.IntegranteDTO;

@Component
public class EquipoClientFallback implements EquipoClient {

    @Override
    public IntegranteDTO obtenerIntegrantePorId(Long idIntegrante) {
        throw new RuntimeException("MS Equipos no disponible, intente más tarde");
    }
}
