package innovasync.auth_service.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String correo;
    private String rol;

    public LoginResponseDTO(String token, String correo, String rol) {
        this.token = token;
        this.correo = correo;
        this.rol = rol;
    }
}