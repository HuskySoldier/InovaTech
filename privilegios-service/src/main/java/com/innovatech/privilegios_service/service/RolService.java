package com.innovatech.privilegios_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.dto.RolDTO;
import com.innovatech.privilegios_service.dto.RolDetalleDTO;
import com.innovatech.privilegios_service.repository.RolRepository;
import com.innovatech.privilegios_service.model.Rol;


import jakarta.transaction.Transactional;


@Service
@Transactional
public class RolService {
    @Autowired
    private RolRepository rolRepository;



    public List<RolDTO> getAllRoles() {
        List<RolDTO> rolesDTO = rolRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return rolesDTO;
    }

    public RolDTO getRolById(Long id) {
        return rolRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }


    public RolDetalleDTO obtenerDetalle(Long id){
        return rolRepository.findById(id)
                .map(this::mapToDetalleDTO)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    public RolDTO crearRol(RolDTO rolDTO) {
        if (rolRepository.existsByNombre(rolDTO.getNombre())) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + rolDTO.getNombre());
        }
        Rol rol = new Rol();
        rol.setNombre(rolDTO.getNombre());
        Rol nuevoRol = rolRepository.save(rol);
        return mapToDTO(nuevoRol);
    }

    public RolDTO actualizarRol(Long id, RolDTO rolDTO) {
        Rol rolExistente = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
        rolExistente.setNombre(rolDTO.getNombre());
        Rol rolActualizado = rolRepository.save(rolExistente);
        return mapToDTO(rolActualizado);
    }

    public void eliminarRol(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }



    //metodos para DTOs
    private RolDTO mapToDTO(Rol rol) {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setIdRol(rol.getIdRol());
        rolDTO.setNombre(rol.getNombre());
        return rolDTO;
    }

    private RolDetalleDTO mapToDetalleDTO(Rol rol) {
        RolDetalleDTO dto = new RolDetalleDTO();
        dto.setIdRol(rol.getIdRol());
        dto.setNombre(rol.getNombre());
        dto.setPrivilegios(rol.getRolPriv() == null ? List.of() :
        rol.getRolPriv().stream()
                .map(rolPriv -> {
                    PrivilegioDTO privDTO = new PrivilegioDTO();
                    privDTO.setIdPriv(rolPriv.getPrivilegio().getIdPriv());
                    privDTO.setNombre(rolPriv.getPrivilegio().getNombre());
                    privDTO.setDescripcion(rolPriv.getPrivilegio().getDescripcion());
                    return privDTO;
                })
                .toList());
        return dto;
    }
}
