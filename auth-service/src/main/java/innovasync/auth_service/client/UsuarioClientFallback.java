package innovasync.auth_service.client;

import org.springframework.stereotype.Component;

import innovasync.auth_service.dto.LoginRequestDTO;

@Component
public class UsuarioClientFallback implements UsuarioClient {

    @Override
    public UsuarioClient.UsuarioResponseFeign obtenerPorEmail(LoginRequestDTO login) {
        // Si MS Usuarios no responde, lanzamos excepción descriptiva
        throw new RuntimeException("MS Usuarios no disponible, intente más tarde");
    }
}
