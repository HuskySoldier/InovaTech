package com.innovatech.usuarios.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.innovatech.usuarios.model.Cargo;
import com.innovatech.usuarios.model.Usuario;
import com.innovatech.usuarios.repository.CargoRepository;
import com.innovatech.usuarios.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(CargoRepository cargoRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            if (cargoRepository.count() == 0) {
                cargoRepository.saveAll(List.of(
                    new Cargo(null, "Desarrollador Backend", null),
                    new Cargo(null, "Desarrollador Frontend", null),
                    new Cargo(null, "UX Designer", null),
                    new Cargo(null, "DevOps", null),
                    new Cargo(null, "Project Manager", null)
                ));
            }

            if (usuarioRepository.count() == 0) {
                Cargo cargo1 = cargoRepository.findById(1L).get();
                Cargo cargo2 = cargoRepository.findById(2L).get();
                Cargo cargo3 = cargoRepository.findById(3L).get();

                usuarioRepository.saveAll(List.of(
                    // (idUsuario, run, nombre, apellido, email, fechaNacimiento, contrasena, fotoPerfil, cargo, idRol, idEstado)
                    new Usuario(null, "12345678-9", "Juan", "Pérez", "juan.perez@innovasync.cl", null, passwordEncoder.encode("password123"), null, cargo1, 2L, 1L),
                    new Usuario(null, "98765432-1", "María", "González", "maria.gonzalez@innovasync.cl", null, passwordEncoder.encode("password123"), null, cargo2, 3L, 1L),
                    new Usuario(null, "22222222-1", "Carlos", "López", "carlos.lopez@innovasync.cl", null, passwordEncoder.encode("password123"), null, cargo3, 2L, 2L),
                    new Usuario(null, "11111111-1", "Admin", "Admin", "admin@innovasync.cl", null, passwordEncoder.encode("admin123"), null, cargo3, 1L, 1L)
                ));
            }
        };
    }
}
