package innovasync.auth_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import innovasync.auth_service.client.UsuarioClient;
import innovasync.auth_service.client.UsuarioClient.UsuarioResponseFeign;
import innovasync.auth_service.config.JwtService;
import innovasync.auth_service.dto.LoginRequestDTO;
import innovasync.auth_service.dto.LoginResponseDTO;
import innovasync.auth_service.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AuthService authService;

    private BCryptPasswordEncoder encoder;
    private LoginRequestDTO requestMock;
    private UsuarioResponseFeign usuarioMock;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        
        requestMock = new LoginRequestDTO();
        requestMock.setCorreo("correo@innovasync.cl");
        requestMock.setClave("secreta123");

        usuarioMock = new UsuarioResponseFeign();
        usuarioMock.setIdUser(1L);
        usuarioMock.setEmail("correo@innovasync.cl");
        usuarioMock.setIdRol(2L);
        // Hasheamos la contraseña real para que el BCrypt de tu AuthService haga match
        usuarioMock.setContrasena(encoder.encode("secreta123"));
    }

    @Test
    void testLoginExito() {
        // Arrange
        when(usuarioClient.obtenerPorEmail(any(LoginRequestDTO.class))).thenReturn(usuarioMock);
        when(jwtService.generarToken("correo@innovasync.cl", "2")).thenReturn("token.jwt.falso");

        // Act
        LoginResponseDTO response = authService.login(requestMock);

        // Assert
        assertNotNull(response);
        assertEquals("token.jwt.falso", response.getToken());
        assertEquals("correo@innovasync.cl", response.getCorreo());
        assertEquals("2", response.getRol());
        
        verify(usuarioClient, times(1)).obtenerPorEmail(requestMock);
        verify(jwtService, times(1)).generarToken("correo@innovasync.cl", "2");
    }

    @Test
    void testLoginUsuarioNoEncontrado() {
        // Arrange
        when(usuarioClient.obtenerPorEmail(any(LoginRequestDTO.class))).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(requestMock);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(jwtService, never()).generarToken(anyString(), anyString());
    }

    @Test
    void testLoginContrasenaIncorrecta() {
        // Arrange
        requestMock.setClave("claveEquivocada"); // Cambiamos la clave por una errónea
        when(usuarioClient.obtenerPorEmail(any(LoginRequestDTO.class))).thenReturn(usuarioMock);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(requestMock);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(jwtService, never()).generarToken(anyString(), anyString());
    }
}