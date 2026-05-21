package innovasync.auth_service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import innovasync.auth_service.config.JwtService;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // Instanciamos el servicio real, ya que no tiene dependencias inyectadas
        jwtService = new JwtService();
    }

    @Test
    void testGenerarYValidarToken() {
        // Act: Generamos un token real
        String token = jwtService.generarToken("admin@innovasync.cl", "1");

        // Assert: Comprobamos que no sea nulo y que sea válido según nuestra propia llave
        assertNotNull(token);
        assertTrue(jwtService.validarToken(token));
    }

    @Test
    void testObtenerCorreoDesdeToken() {
        // Arrange
        String correoOriginal = "test@innovasync.cl";
        String token = jwtService.generarToken(correoOriginal, "2");

        // Act
        String correoExtraido = jwtService.obtenerCorreo(token);

        // Assert
        assertEquals(correoOriginal, correoExtraido);
    }

    @Test
    void testValidarTokenInvalido() {
        // Act & Assert
        assertFalse(jwtService.validarToken("este.es.un.token.falso.y.alterado"));
    }
}