package com.innovatech.usuarios.client;

import org.springframework.stereotype.Component;
import com.innovatech.usuarios.DTO.RolDTO;

@Component
public class PrivilegiosClientFallback implements PrivilegiosClient {

    @Override
    public RolDTO obtenerRolPorId(Long id) {
        RolDTO rol = new RolDTO();
        rol.setIdRol(id);
        rol.setNombre("Sin rol");
        return rol;
        

    }

}
