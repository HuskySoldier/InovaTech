package innovasync.auth_service.client;

import org.springframework.stereotype.Component;

@Component
public class UsuarioClientFallback implements UsuarioClient {

    @Override
    public UsuarioClient.UsuarioResponseFeign obtenerPorEmail(String email) {
        // Si MS Usuarios no responde, lanzamos excepción descriptiva
        throw new RuntimeException("MS Usuarios no disponible, intente más tarde");
    }
}
