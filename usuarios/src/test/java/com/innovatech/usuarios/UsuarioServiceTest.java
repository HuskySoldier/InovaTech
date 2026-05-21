package com.innovatech.usuarios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.innovatech.usuarios.DTO.EstadoDTO;
import com.innovatech.usuarios.DTO.RolDTO;
import com.innovatech.usuarios.DTO.UsuarioAuthDTO;
import com.innovatech.usuarios.DTO.UsuarioRequestDTO;
import com.innovatech.usuarios.DTO.UsuarioResponseDTO;
import com.innovatech.usuarios.client.EstadoClient;
import com.innovatech.usuarios.client.PrivilegiosClient;
import com.innovatech.usuarios.config.RabbitMQConfig;
import com.innovatech.usuarios.model.Cargo;
import com.innovatech.usuarios.model.Usuario;
import com.innovatech.usuarios.repository.CargoRepository;
import com.innovatech.usuarios.repository.UsuarioRepository;
import com.innovatech.usuarios.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CargoRepository cargoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EstadoClient estadoClient;
    @Mock
    private PrivilegiosClient privilegiosClient;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        Cargo cargo = new Cargo();
        cargo.setIdCargo(1L);
        cargo.setNombreCargo("Desarrollador");

        usuarioMock = new Usuario();
        usuarioMock.setIdUsuario(1L);
        usuarioMock.setRun("12345678-9");
        usuarioMock.setNombre("Juan");
        usuarioMock.setApellido("Perez");
        usuarioMock.setEmail("Juan.Perez@innovasync.cl");
        usuarioMock.setContrasena("hash_password");
        usuarioMock.setIdEstado(1L);
        usuarioMock.setIdRol(2L);
        usuarioMock.setCargo(cargo);
    }

    @Test
    void testObtenerUsuarioPorId() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        
        EstadoDTO estadoDTO = new EstadoDTO();
        estadoDTO.setNombre("Activo");
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        // Act
        UsuarioResponseDTO result = usuarioService.obtenerUsuarioPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("Desarrollador", result.getNombreCargo());
        assertEquals("Activo", result.getNombreEstado());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testIniciarSesionExito() {
        // Arrange
        when(usuarioRepository.findByEmail("Juan.Perez@innovasync.cl")).thenReturn(usuarioMock);
        when(passwordEncoder.matches("1234", "hash_password")).thenReturn(true);

        // Act
        UsuarioAuthDTO result = usuarioService.iniciarSesion("Juan.Perez@innovasync.cl", "1234");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertEquals("Juan.Perez@innovasync.cl", result.getEmail());
    }

    @Test
    void testIniciarSesionFalloContrasena() {
        // Arrange
        when(usuarioRepository.findByEmail("Juan.Perez@innovasync.cl")).thenReturn(usuarioMock);
        when(passwordEncoder.matches("12345", "hash_password")).thenReturn(false);

        // Act
        UsuarioAuthDTO result = usuarioService.iniciarSesion("Juan.Perez@innovasync.cl", "12345");

        // Assert
        assertNull(result);
    }

    @Test
    void testCrearUsuario() {
        // Arrange
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setRun("98765432-1");
        dto.setNombre("Ana");
        dto.setApellido("Gomez");
        dto.setClave("secreta");
        dto.setCorreo("Ana.Gomez@innovasync.cl");
        dto.setIdCargo(1L);
        dto.setIdRol(2L);

        when(usuarioRepository.existsByRun(dto.getRun())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoDTO());
        
        Cargo cargoMock = new Cargo();
        cargoMock.setIdCargo(1L);
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargoMock));
        
        RolDTO rolMock = new RolDTO();
        rolMock.setIdRol(2L);
        when(privilegiosClient.obtenerRolPorId(2L)).thenReturn(rolMock);
        
        when(passwordEncoder.encode(dto.getClave())).thenReturn("hashed_secreta");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        UsuarioResponseDTO result = usuarioService.crearUsuario(dto);

        // Assert
        assertNotNull(result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testDesactivarUsuario() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Act
        usuarioService.desactivarUsuario(1L);

        // Assert
        assertEquals(2L, usuarioMock.getIdEstado());
        verify(usuarioRepository).save(usuarioMock);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_USUARIOS),
                eq(RabbitMQConfig.ROUTING_KEY_USUARIO_DESACTIVADO),
                anyString()
        );
    }
}