package com.innovatech.estado_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.estado_service.dto.EstadoDTO;

import com.innovatech.estado_service.service.EstadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {
    private final EstadoService estadoService;

    @GetMapping
    public ResponseEntity<List<EstadoDTO>> getAllEstados() {
        return ResponseEntity.ok(estadoService.getAllEstados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getEstadoById(@PathVariable Long id) {
        return ResponseEntity.ok(estadoService.getEstadoById(id));
    }

    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<EstadoDTO>> getEstadosByTipoId(@PathVariable Long tipoId) {
        return ResponseEntity.ok(estadoService.getEstadosByTipoId(tipoId));
    }

    @PostMapping
    public ResponseEntity<EstadoDTO> createEstado(@RequestBody EstadoDTO estadoDTO) {
        try {
            return ResponseEntity.status(201).body(estadoService.createEstado(estadoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> updateEstado(@PathVariable Long id, @RequestBody EstadoDTO estadoDTO) {
        try {
            return ResponseEntity.ok(estadoService.updateEstado(id, estadoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        try {
            estadoService.deleteEstado(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
