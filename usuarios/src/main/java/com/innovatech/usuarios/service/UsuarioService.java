package com.innovatech.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.innovatech.usuarios.DTO.UsuarioRequestDTO;
import com.innovatech.usuarios.DTO.UsuarioResponseDTO;
import com.innovatech.usuarios.DTO.UsuarioSummaryDTO;
import com.innovatech.usuarios.model.Cargo;
import com.innovatech.usuarios.model.Usuario;
import com.innovatech.usuarios.repository.CargoRepository;
import com.innovatech.usuarios.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CargoRepository cargoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    public List<UsuarioSummaryDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toResponseDTO)
                .orElse(null);
    }

    public UsuarioResponseDTO buscarPorRun(String run) {
        Usuario usuario = usuarioRepository.findByRun(run);
        return usuario != null ? toResponseDTO(usuario) : null;
    }

    public List<UsuarioSummaryDTO> buscarPorEstado(Long idEstado) {
        return usuarioRepository.findByIdEstado(idEstado).stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioSummaryDTO> buscarPorRol(Long idRol) {
        return usuarioRepository.findByIdRol(idRol).stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null ? toResponseDTO(usuario) : null;
    }

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        // Validar que el RUN no esté duplicado
        if (usuarioRepository.existsByRun(dto.getRun())) {
            throw new RuntimeException("Ya existe un usuario con el RUN: " + dto.getRun());
        }

        // Validar que el correo no esté duplicado
        if (usuarioRepository.existsByEmail(dto.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + dto.getCorreo());
        }

        // Buscar el cargo
        Cargo cargo = cargoRepository.findById(dto.getIdCargo())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado con id: " + dto.getIdCargo()));

        // Construir la entidad
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setRun(dto.getRun());
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setApellido(dto.getApellido());
        nuevoUsuario.setEmail(dto.getNombre() + "." + dto.getApellido() + "@innovasync.cl");
        nuevoUsuario.setFechaNacimiento(dto.getFechaNacimiento());
        nuevoUsuario.setContrasena(passwordEncoder.encode(dto.getClave())); // Hash bcrypt
        nuevoUsuario.setFotoPerfil(dto.getImgPerfil());
        nuevoUsuario.setCargo(cargo);
        nuevoUsuario.setIdRol(dto.getIdRol());
        nuevoUsuario.setIdEstado(1L); // Estado activo por defecto

        return toResponseDTO(usuarioRepository.save(nuevoUsuario));
    }


    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        if (usuarioExistente != null) {
            // Validar que el RUN no esté duplicado (si se está actualizando el RUN)
            if (!usuarioExistente.getRun().equals(dto.getRun()) && usuarioRepository.existsByRun(dto.getRun())) {
                throw new RuntimeException("Ya existe un usuario con el RUN: " + dto.getRun());
            }

            // Validar que el correo no esté duplicado (si se está actualizando el correo)
            if (!usuarioExistente.getEmail().equals(dto.getCorreo()) && usuarioRepository.existsByEmail(dto.getCorreo())) {
                throw new RuntimeException("Ya existe un usuario con el correo: " + dto.getCorreo());
            }

            Cargo cargo = cargoRepository.findById(dto.getIdCargo())
                    .orElseThrow(() -> new RuntimeException("Cargo no encontrado con id: " + dto.getIdCargo()));

            usuarioExistente.setRun(dto.getRun());
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setApellido(dto.getApellido());
            usuarioExistente.setEmail(dto.getCorreo());
            usuarioExistente.setFechaNacimiento(dto.getFechaNacimiento());
            if (dto.getClave() != null && !dto.getClave().isEmpty()) {
                usuarioExistente.setContrasena(passwordEncoder.encode(dto.getClave())); // Hash bcrypt
            }
            usuarioExistente.setFotoPerfil(dto.getImgPerfil());
            usuarioExistente.setCargo(cargo);
            usuarioExistente.setIdRol(dto.getIdRol());
            // No se actualiza el estado aquí, se asume que es una operación separada

            return toResponseDTO(usuarioRepository.save(usuarioExistente));
        }
        return null;
    }

   
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }


    public UsuarioResponseDTO cambiarEstadoUsuario(Long id, Long nuevoEstado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioExistente.setIdEstado(nuevoEstado);
        return toResponseDTO(usuarioRepository.save(usuarioExistente));
    }

    
    //Metodos para convertir Usuario a las diferentes DTOs
    private UsuarioSummaryDTO toSummaryDTO(Usuario usuario) {
    UsuarioSummaryDTO dto = new UsuarioSummaryDTO();
        dto.setIdUser(usuario.getIdUsuario());
        dto.setNombreCompleto(usuario.getNombre() + " " + usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setNombreCargo(usuario.getCargo() != null 
                ? usuario.getCargo().getNombreCargo() 
                : "Sin cargo");
        return dto;
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUser(usuario.getIdUsuario());
        dto.setRun(usuario.getRun());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNombreCompleto(usuario.getNombre() + " " + usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setImgPerfil(usuario.getFotoPerfil());
        dto.setNombreCargo(usuario.getCargo() != null 
                ? usuario.getCargo().getNombreCargo() 
                : "Sin cargo");
        dto.setIdRol(usuario.getIdRol());
        dto.setIdEstado(usuario.getIdEstado());
        return dto;
    }   

    
}
