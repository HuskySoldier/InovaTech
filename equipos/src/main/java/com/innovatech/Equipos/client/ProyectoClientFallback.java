package com.innovatech.Equipos.client;

import org.springframework.stereotype.Component;

@Component
public class ProyectoClientFallback implements ProyectoClient {
    @Override
    public Object obtenerProyectoPorId(Long id) {
        return null; 
    }

}
