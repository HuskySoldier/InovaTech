package com.innovatech.usuarios.client;

import org.springframework.stereotype.Component;
import com.innovatech.usuarios.DTO.RolDTO;

@Component
public class PrivilegiosClientFallback implements PrivilegiosClient {

    @Override
    public RolDTO obtenerRolPorId(Long id) {
        throw new RuntimeException("MS Privilegios no disponible, intente más tarde");
    }
}
