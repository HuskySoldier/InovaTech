package com.innovatech.asignaciones.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.innovatech.asignaciones.dto.TareaDTO;

@Component
public class TareaClientFallback implements TareaClient {

    @Override
    public TareaDTO obtenerTareaPorId(Long id) {
        throw new RuntimeException("MS Tareas no disponible, intente más tarde");
    }

    @Override
    public List<TareaDTO> obtenerTareasBatch(List<Long> ids) {
        throw new RuntimeException("MS Tareas no disponible, intente más tarde");
    }
}
