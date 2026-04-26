package com.innovatech.privilegios_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.model.Privilegio;
import com.innovatech.privilegios_service.model.Rol;
import com.innovatech.privilegios_service.model.RolPriv;
import com.innovatech.privilegios_service.repository.PrivilegioRepository;
import com.innovatech.privilegios_service.repository.RolPrivRepository;
import com.innovatech.privilegios_service.repository.RolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolPrivService {

    @Autowired
    private RolPrivRepository rolPrivRepo;
    @Autowired
    private RolRepository rolRepo;
    @Autowired
    private PrivilegioRepository privRepo;

    // Asignar un privilegio a un rol
    public void asignarPrivilegio(Long idRol, Long idPriv) {
        // Validar que no esté ya asignado
        if (rolPrivRepo.existsByRol_IdRolAndPrivilegio_IdPriv(idRol, idPriv)) {
            throw new RuntimeException("El privilegio ya está asignado a este rol");
        }

        Rol rol = rolRepo.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + idRol));

        Privilegio privilegio = privRepo.findById(idPriv)
                .orElseThrow(() -> new RuntimeException("Privilegio no encontrado con id: " + idPriv));

        RolPriv rolPriv = new RolPriv();
        rolPriv.setRol(rol);
        rolPriv.setPrivilegio(privilegio);
        rolPrivRepo.save(rolPriv);
    }

    // Quitar un privilegio de un rol
    public void quitarPrivilegio(Long idRol, Long idPriv) {
        List<RolPriv> lista = rolPrivRepo.findByRol_IdRol(idRol);
        RolPriv rolPriv = lista.stream()
                .filter(rp -> rp.getPrivilegio().getIdPriv().equals(idPriv))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El privilegio no está asignado a este rol"));
        rolPrivRepo.delete(rolPriv);
    }

    // Listar privilegios de un rol
    public List<PrivilegioDTO> listarPrivilegiosPorRol(Long idRol) {
        return rolPrivRepo.findByRol_IdRol(idRol).stream()
                .map(rp -> {
                    PrivilegioDTO dto = new PrivilegioDTO();
                    dto.setIdPriv(rp.getPrivilegio().getIdPriv());
                    dto.setNombre(rp.getPrivilegio().getNombre());
                    dto.setDescripcion(rp.getPrivilegio().getDescripcion());
                    return dto;
                })
                .toList();
    }
}
