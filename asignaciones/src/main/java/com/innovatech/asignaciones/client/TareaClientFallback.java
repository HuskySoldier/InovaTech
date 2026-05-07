package com.innovatech.asignaciones.client;

import org.springframework.stereotype.Component;

@Component
public class TareaClientFallback implements TareaClient {

    @Override
    public Object obtenerTareaPorId(Long id) {
        throw new RuntimeException("MS Tareas no disponible, intente más tarde");
    }
}
