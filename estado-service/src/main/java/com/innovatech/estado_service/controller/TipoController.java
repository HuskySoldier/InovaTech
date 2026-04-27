package com.innovatech.estado_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.estado_service.dto.TipoDTO;
import com.innovatech.estado_service.service.TipoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tipos")
@RequiredArgsConstructor
public class TipoController {

    private final TipoService tipoService;

    @GetMapping
    public ResponseEntity<List<TipoDTO>> getAllTipos() {
        List<TipoDTO> tipos = tipoService.getAllTipos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDTO> getTipoById(@PathVariable Long id) {
        TipoDTO tipo = tipoService.getTipoById(id);
        return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<TipoDTO> createTipo(@RequestBody TipoDTO tipoDTO) {
        try {
            TipoDTO saved = tipoService.createTipo(tipoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDTO> updateTipo(@PathVariable Long id, @RequestBody TipoDTO tipoDTO) {
        try {
            TipoDTO updated = tipoService.updateTipo(id, tipoDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipo(@PathVariable Long id) {
        try {
            tipoService.deleteTipo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
