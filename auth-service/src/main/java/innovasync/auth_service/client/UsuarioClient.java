package innovasync.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.Data;

@FeignClient(name = "usuarios", url = "http://localhost:8081")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/email/{email}")
    UsuarioResponseFeign obtenerPorEmail(@PathVariable String email);

    @Data
    class UsuarioResponseFeign {
        private Long idUser;
        private String email;
        private String contrasena;
        private Long idRol;
    }
}