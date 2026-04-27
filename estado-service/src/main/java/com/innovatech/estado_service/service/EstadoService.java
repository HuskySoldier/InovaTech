package com.innovatech.estado_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.innovatech.estado_service.dto.EstadoDTO;
import com.innovatech.estado_service.model.Estado;
import com.innovatech.estado_service.model.Tipo;
import com.innovatech.estado_service.repository.EstadoRepository;
import com.innovatech.estado_service.repository.TipoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final TipoRepository tipoRepository;

    //metodos GET
    public EstadoDTO getEstadoById(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        return mapToDTO(estado);
    }


    public List<EstadoDTO> getEstadosByTipoId(Long tipoId) {
        List<Estado> estados = estadoRepository.findByTipo_IdTipo(tipoId);
        return estados.stream().map(this::mapToDTO).toList();
    }

    public List<EstadoDTO> getAllEstados() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream().map(this::mapToDTO).toList();
    }

    //metodos POST, PUT, DELETE

    public EstadoDTO createEstado(EstadoDTO estadoDTO) {
        if (estadoRepository.existsByNombre(estadoDTO.getNombre())) {
            throw new RuntimeException("El nombre del estado ya existe");
        }
        Tipo tipo = tipoRepository.findById(estadoDTO.getIdTipo())
            .orElseThrow(() -> new RuntimeException("Tipo no encontrado con id: " + estadoDTO.getIdTipo()));
            
        Estado estado = new Estado();
        estado.setNombre(estadoDTO.getNombre());
        estado.setDescripcion(estadoDTO.getDescripcion());
        estado.setTipo(tipo);
        Estado savedEstado = estadoRepository.save(estado);
        return mapToDTO(savedEstado);
    }

    public EstadoDTO updateEstado(Long id, EstadoDTO estadoDTO) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        if (!estado.getNombre().equals(estadoDTO.getNombre()) && estadoRepository.existsByNombre(estadoDTO.getNombre())) {
            throw new RuntimeException("El nombre del estado ya existe");
        }

        Tipo tipo = tipoRepository.findById(estadoDTO.getIdTipo())
            .orElseThrow(() -> new RuntimeException("Tipo no encontrado con id: " + estadoDTO.getIdTipo()));
        estado.setNombre(estadoDTO.getNombre());
        estado.setDescripcion(estadoDTO.getDescripcion());
        estado.setTipo(tipo);
        Estado updatedEstado = estadoRepository.save(estado);
        return mapToDTO(updatedEstado);
    }

    public void deleteEstado(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        estadoRepository.delete(estado);
    }

    //metodo DTO
    private EstadoDTO mapToDTO(Estado estado) {
        return new EstadoDTO(
                estado.getIdEstado(),
                estado.getNombre(),
                estado.getDescripcion(),
                estado.getTipo().getIdTipo(),
                estado.getTipo().getNombre()
        );
    }
}
