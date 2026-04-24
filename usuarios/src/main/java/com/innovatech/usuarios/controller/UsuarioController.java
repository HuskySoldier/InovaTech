package com.innovatech.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.usuarios.DTO.UsuarioAuthDTO;
import com.innovatech.usuarios.DTO.UsuarioRequestDTO;
import com.innovatech.usuarios.DTO.UsuarioResponseDTO;
import com.innovatech.usuarios.DTO.UsuarioSummaryDTO;
import com.innovatech.usuarios.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuarios() {
        List<UsuarioSummaryDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/run/{run}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorRun(@PathVariable String run) {
        UsuarioResponseDTO usuario = usuarioService.buscarPorRun(run);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/auth/email/{email}")
    public ResponseEntity<UsuarioAuthDTO> obtenerParaAuth(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuariosPorEstado(@PathVariable Long estado) {
        List<UsuarioSummaryDTO> usuarios = usuarioService.buscarPorEstado(estado);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/rol/{idrol}")
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuariosPorRol(@PathVariable Long idrol) {
        List<UsuarioSummaryDTO> usuarios = usuarioService.buscarPorRol(idrol);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody UsuarioRequestDTO usuario) {
        try {
            UsuarioResponseDTO nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuario) {
        try {
            UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstadoUsuario(@PathVariable Long id, @PathVariable Long idEstado) {
        try {
            UsuarioResponseDTO usuarioActualizado = usuarioService.cambiarEstadoUsuario(id, idEstado);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
