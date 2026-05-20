package innovasync.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import innovasync.auth_service.dto.LoginRequestDTO;
import lombok.Data;

@FeignClient(name = "usuarios", fallback = UsuarioClientFallback.class)
public interface UsuarioClient {

    @GetMapping("/api/usuarios/auth/login2")
    UsuarioResponseFeign obtenerPorEmail(@RequestBody LoginRequestDTO login);

    @Data
    class UsuarioResponseFeign {
        private Long idUser;
        private String email;
        private String contrasena;
        private Long idRol;
        private Long idEstado;
    }
}