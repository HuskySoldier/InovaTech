package com.innovatech.Equipos.client;

import org.springframework.stereotype.Component;

@Component
public class UsuarioClientFallback implements UsuarioClient{
    @Override
    public Object obtenerUsuarioPorId(Long id) {
        return null; 
    }

}
