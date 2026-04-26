package com.innovatech.privilegios_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.service.PrivilegioService;

@RestController
@RequestMapping("/api/privilegios")
public class PrivilegioController {
    @Autowired
    private PrivilegioService privilegioService;

    @GetMapping
    public ResponseEntity<List<PrivilegioDTO>> getAllPrivilegios() {
        List<PrivilegioDTO> privilegios = privilegioService.obtenerTodos();
        return ResponseEntity.ok(privilegios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivilegioDTO> getPrivilegioById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(privilegioService.obtenerPriv(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PrivilegioDTO> crearPrivilegio(@RequestBody PrivilegioDTO privilegioDTO) {
        try {
            PrivilegioDTO nuevoPrivilegio = privilegioService.crearPriv(privilegioDTO);
            return ResponseEntity.status(201).body(nuevoPrivilegio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrivilegioDTO> actualizarPrivilegio(@PathVariable Long id, @RequestBody PrivilegioDTO privilegioDTO) {
        try {
            PrivilegioDTO privilegioActualizado = privilegioService.actualizarPriv(id, privilegioDTO);
            return ResponseEntity.ok(privilegioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrivilegio(@PathVariable Long id) {
        try {
            privilegioService.eliminarPriv(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
