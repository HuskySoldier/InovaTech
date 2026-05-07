package com.innovatech.asignaciones.client;


import org.springframework.stereotype.Component;

@Component
public class EquipoClientFallback implements EquipoClient {

    @Override
    public Object obtenerIntegrantePorId(Long idIntegrante) {
        throw new RuntimeException("MS Equipos no disponible, intente más tarde");
    }
}
