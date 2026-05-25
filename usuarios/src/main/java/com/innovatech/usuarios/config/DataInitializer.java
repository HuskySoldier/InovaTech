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
                        new Cargo(null, "Project Manager", null)));
            }

            Cargo cargo1 = cargoRepository.findById(1L).get(); // Desarrollador Backend
            Cargo cargo2 = cargoRepository.findById(2L).get(); // Desarrollador Frontend
            Cargo cargo3 = cargoRepository.findById(3L).get(); // UX Designer
            Cargo cargo4 = cargoRepository.findById(4L).get(); // DevOps
            Cargo cargo5 = cargoRepository.findById(5L).get(); // Project Manager

            usuarioRepository.saveAll(List.of(
                    // ==========================================
                    // ADMINISTRADOR DEL SISTEMA (Rol 1L)
                    // ==========================================
                    new Usuario(null, "11111111-1", "Admin", "Admin", "admin@innovasync.cl", null,
                            passwordEncoder.encode("admin123"), null, cargo5, 1L, 1L),

                    // ==========================================
                    // EQUIPO 1 (Desarrollo Frontend y UX)
                    // ==========================================
                    // 1 Gestor (Rol 2L) -> Project Manager
                    new Usuario(null, "12345678-9", "Ana", "Silva", "ana.silva@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo5, 2L, 1L),
                    // 3 Colaboradores (Rol 3L) -> 2 Frontends y 1 UX
                    new Usuario(null, "13451345-2", "Pedro", "Morales", "pedro.morales@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo2, 3L, 1L),
                    new Usuario(null, "14561456-4", "Lucía", "Rojas", "lucia.rojas@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo2, 3L, 1L),
                    new Usuario(null, "15671567-6", "Diego", "Castro", "diego.castro@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo3, 3L, 1L),

                    // ==========================================
                    // EQUIPO 2 (Desarrollo Backend puro)
                    // ==========================================
                    // 1 Gestor (Rol 2L) -> Project Manager
                    new Usuario(null, "16781678-8", "Camila", "Herrera", "camila.herrera@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo5, 2L, 1L),
                    // 3 Colaboradores (Rol 3L) -> 3 Backends
                    new Usuario(null, "17891789-K", "Jorge", "Soto", "jorge.soto@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo1, 3L, 1L),
                    new Usuario(null, "18901890-1", "Valentina", "Molina", "valentina.molina@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo1, 3L, 1L),
                    new Usuario(null, "19011901-3", "Felipe", "Vargas", "felipe.vargas@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo1, 3L, 1L),

                    // ==========================================
                    // EQUIPO 3 (Infraestructura y DevOps)
                    // ==========================================
                    // 1 Gestor (Rol 2L) -> Project Manager
                    new Usuario(null, "20122012-5", "Matías", "Díaz", "matias.diaz@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo5, 2L, 1L),
                    // 3 Colaboradores (Rol 3L) -> 3 DevOps
                    new Usuario(null, "21232123-7", "Javiera", "Flores", "javiera.flores@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo4, 3L, 1L),
                    new Usuario(null, "22342234-9", "Sebastián", "Poblete", "sebastian.poblete@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo4, 3L, 1L),
                    new Usuario(null, "23452345-0", "Catalina", "Navarro", "catalina.navarro@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo4, 3L, 1L),

                    // ==========================================
                    // EQUIPO 4 (Célula Ágil Mixta)
                    // ==========================================
                    // 1 Gestor (Rol 2L) -> Project Manager
                    new Usuario(null, "24562456-2", "Ignacio", "Salazar", "ignacio.salazar@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo5, 2L, 1L),
                    // 3 Colaboradores (Rol 3L) -> 1 Backend, 1 Frontend, 1 DevOps
                    new Usuario(null, "25672567-4", "Fernanda", "Guzmán", "fernanda.guzman@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo1, 3L, 1L),
                    new Usuario(null, "26782678-6", "Rodrigo", "Ríos", "rodrigo.rios@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo2, 3L, 1L),
                    new Usuario(null, "27892789-8", "Daniela", "Fuentes", "daniela.fuentes@innovasync.cl", null,
                            passwordEncoder.encode("password123"), null, cargo4, 3L, 1L)));
        };
    }
}
