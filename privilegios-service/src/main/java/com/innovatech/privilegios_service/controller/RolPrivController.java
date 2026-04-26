package com.innovatech.privilegios_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.service.RolPrivService;

@RestController
@RequestMapping("/api/roles")
public class RolPrivController {
    @Autowired
    private RolPrivService rolPrivService;

    @GetMapping("/{id}/privilegios")
    public ResponseEntity<List<PrivilegioDTO>> listarPrivilegiosPorRol(@PathVariable Long id) {
        try {
            List<PrivilegioDTO> privilegios = rolPrivService.listarPrivilegiosPorRol(id);
            return ResponseEntity.ok(privilegios);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @PostMapping("/{idRol}/privilegios/{idPriv}")
    public ResponseEntity<Void> asignarPrivilegio(@PathVariable Long idRol, @PathVariable Long idPriv) {
        try {
            rolPrivService.asignarPrivilegio(idRol, idPriv);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{idRol}/privilegios/{idPriv}")
    public ResponseEntity<Void> quitarPrivilegio(@PathVariable Long idRol, @PathVariable Long idPriv) {
        try {
            rolPrivService.quitarPrivilegio(idRol, idPriv);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
