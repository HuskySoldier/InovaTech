package com.innovatech.Equipos.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.innovatech.Equipos.dto.UsuarioDTO;

@Component
public class UsuarioClientFallback implements UsuarioClient{
    @Override
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        return null; 
    }

    @Override
    public List<UsuarioDTO> obtenerUsuariosBatch(List<Long> ids) {
        return List.of();
    }

}
