package com.innovatech.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.usuarios.DTO.UsuarioAuthDTO;
import com.innovatech.usuarios.DTO.UsuarioRequestDTO;
import com.innovatech.usuarios.DTO.UsuarioResponseDTO;
import com.innovatech.usuarios.DTO.UsuarioSummaryDTO;
import com.innovatech.usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión y autenticación de usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios", description = "Retorna un resumen de todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario específico mediante su identificador numérico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return (usuario != null) ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener usuario por RUN", description = "Busca un usuario mediante su RUN (identificador único).")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @GetMapping("/run/{run}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorRun(
            @Parameter(description = "RUN del usuario", required = true) @PathVariable String run) {
        UsuarioResponseDTO usuario = usuarioService.buscarPorRun(run);
        return (usuario != null) ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener datos para autenticación", description = "Recupera los datos necesarios para validar la sesión de un usuario por su email.")
    @GetMapping("/auth/email/{email}")
    public ResponseEntity<UsuarioAuthDTO> obtenerParaAuth(
            @Parameter(description = "Correo electrónico del usuario", required = true) @PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @Operation(summary = "Listar usuarios por estado", description = "Filtra usuarios según su estado actual.")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuariosPorEstado(
            @Parameter(description = "ID del estado", required = true) @PathVariable Long estado) {
        return ResponseEntity.ok(usuarioService.buscarPorEstado(estado));
    }

    @Operation(summary = "Listar usuarios por rol", description = "Filtra usuarios que poseen un rol específico.")
    @GetMapping("/rol/{idrol}")
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuariosPorRol(
            @Parameter(description = "ID del rol", required = true) @PathVariable Long idrol) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(idrol));
    }

    @Operation(summary = "Crear nuevo usuario", description = "Registra un nuevo usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody UsuarioRequestDTO usuario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente.")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id, 
            @RequestBody UsuarioRequestDTO usuario) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cambiar estado de usuario", description = "Actualiza solo el estado de un usuario determinado.")
    @PatchMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstadoUsuario(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevo ID del estado", required = true) @PathVariable Long idEstado) {
        try {
            return ResponseEntity.ok(usuarioService.cambiarEstadoUsuario(id, idEstado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Inicio de sesión", description = "Valida las credenciales de un usuario y retorna los datos de sesión.")
    @PostMapping("/auth/login2")
    public ResponseEntity<UsuarioAuthDTO> iniciarSesion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciales de acceso") 
            @RequestBody UsuarioRequestDTO inicio){
        return ResponseEntity.ok(usuarioService.iniciarSesion(inicio.getCorreo(), inicio.getClave()));
    }
}