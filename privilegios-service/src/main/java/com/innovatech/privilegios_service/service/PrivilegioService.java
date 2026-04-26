package com.innovatech.privilegios_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.privilegios_service.repository.PrivilegioRepository;
import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.model.Privilegio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PrivilegioService {

    @Autowired
    private PrivilegioRepository privRepo;


    public PrivilegioDTO obtenerPriv(Long id) {
        return privRepo.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Privilegio no encontrado con id: " + id));
    }

    public List<PrivilegioDTO> obtenerTodos() {
        return privRepo.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public PrivilegioDTO crearPriv(PrivilegioDTO dto) {
        if (privRepo.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe un privilegio con el nombre: " + dto.getNombre());
        }   
        Privilegio privilegio = new Privilegio();
        privilegio.setNombre(dto.getNombre());
        privilegio.setDescripcion(dto.getDescripcion());
        Privilegio saved = privRepo.save(privilegio);
        return mapToDTO(saved);
    }
    
    public PrivilegioDTO actualizarPriv(Long id, PrivilegioDTO dto) {
        Privilegio privilegio = privRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Privilegio no encontrado con id: " + id));
        privilegio.setNombre(dto.getNombre());
        privilegio.setDescripcion(dto.getDescripcion());
        Privilegio updated = privRepo.save(privilegio);
        return mapToDTO(updated);
    }

    public void eliminarPriv(Long id) {
        if (!privRepo.existsById(id)) {
            throw new RuntimeException("Privilegio no encontrado con id: " + id);
        }
        privRepo.deleteById(id);
    }


    //metodo para dto
    private PrivilegioDTO mapToDTO(Privilegio privilegio) {
        PrivilegioDTO dto = new PrivilegioDTO();
        dto.setIdPriv(privilegio.getIdPriv());
        dto.setNombre(privilegio.getNombre());
        dto.setDescripcion(privilegio.getDescripcion());
        return dto;
    }
}
