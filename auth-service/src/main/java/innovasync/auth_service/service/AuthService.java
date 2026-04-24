package innovasync.auth_service.service;

import innovasync.auth_service.config.JwtService;
import innovasync.auth_service.dto.LoginRequestDTO;
import innovasync.auth_service.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RestTemplate restTemplate = new RestTemplate();

    // URL del MS Usuarios
    private final String USUARIOS_URL = "http://localhost:8081/api/usuarios/email/";

    public LoginResponseDTO login(LoginRequestDTO request) {

        // 1. Busca el usuario en el MS Usuarios por email
        UsuarioResponse usuario = restTemplate.getForObject(
            USUARIOS_URL + request.getCorreo(),
            UsuarioResponse.class
        );

        // 2. Si no existe lanza error
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 3. Verifica la contraseña con BCrypt
        if (!passwordEncoder.matches(request.getClave(), usuario.contrasena)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // 4. Genera el token JWT
        String token = jwtService.generarToken(
            usuario.email, 
            String.valueOf(usuario.idRol)
        );

        return new LoginResponseDTO(token, usuario.email, String.valueOf(usuario.idRol));
    }

    // Clase para mapear la respuesta del MS Usuarios
    static class UsuarioResponse {
        public Long idUser;
        public String email;
        public String contrasena;
        public Long idRol;
    }
}
