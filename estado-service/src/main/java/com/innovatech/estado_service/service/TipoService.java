package com.innovatech.estado_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.innovatech.estado_service.dto.TipoDTO;
import com.innovatech.estado_service.model.Tipo;
import com.innovatech.estado_service.repository.TipoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoService {

    private final TipoRepository tipoRepository;

    public List<TipoDTO> getAllTipos() {
        List<Tipo> tipos = tipoRepository.findAll();
        return tipos.stream().map(this::mapToDTO).toList();
    }

    public TipoDTO getTipoById(Long id) {
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        return mapToDTO(tipo);
    }

    public TipoDTO createTipo(TipoDTO tipoDTO) {
        if (tipoRepository.existsByNombre(tipoDTO.getNombre())) {
            throw new RuntimeException("El nombre del tipo ya existe");
        }
        Tipo tipo = new Tipo();
        tipo.setNombre(tipoDTO.getNombre());
        Tipo savedTipo = tipoRepository.save(tipo);
        return mapToDTO(savedTipo);
    }

    public TipoDTO updateTipo(Long id, TipoDTO tipoDTO) {
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        if (!tipo.getNombre().equals(tipoDTO.getNombre()) && tipoRepository.existsByNombre(tipoDTO.getNombre())) {
            throw new RuntimeException("El nombre del tipo ya existe");
        }
        tipo.setNombre(tipoDTO.getNombre());
        Tipo updatedTipo = tipoRepository.save(tipo);
        return mapToDTO(updatedTipo);
    }

    public void deleteTipo(Long id) {
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
        tipoRepository.delete(tipo);
    }

    private TipoDTO mapToDTO(Tipo tipo) {
        return new TipoDTO(tipo.getIdTipo(), tipo.getNombre());
    }
}
