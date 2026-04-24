package innovasync.auth_service.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String correo;
    private String clave;
}