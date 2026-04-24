package innovasync.auth_service.service;

import innovasync.auth_service.client.UsuarioClient;
import innovasync.auth_service.client.UsuarioClient.UsuarioResponseFeign;
import innovasync.auth_service.config.JwtService;
import innovasync.auth_service.dto.LoginRequestDTO;
import innovasync.auth_service.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UsuarioClient usuarioClient;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponseDTO login(LoginRequestDTO request) {

        // 1. Busca usuario en MS Usuarios via Feign
        UsuarioResponseFeign usuario = usuarioClient.obtenerPorEmail(request.getCorreo());

        // 2. Si no existe lanza error
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 3. Verifica contraseña con BCrypt
        if (!passwordEncoder.matches(request.getClave(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // 4. Genera JWT
        String token = jwtService.generarToken(
            usuario.getEmail(),
            String.valueOf(usuario.getIdRol())
        );

        return new LoginResponseDTO(token, usuario.getEmail(), String.valueOf(usuario.getIdRol()));
    }
}