package com.innovatech.privilegios_service.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.innovatech.privilegios_service.model.Privilegio;
import com.innovatech.privilegios_service.model.Rol;
import com.innovatech.privilegios_service.model.RolPriv;
import com.innovatech.privilegios_service.repository.PrivilegioRepository;
import com.innovatech.privilegios_service.repository.RolPrivRepository;
import com.innovatech.privilegios_service.repository.RolRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RolRepository rolRepo,
                               PrivilegioRepository privRepo,
                               RolPrivRepository rolPrivRepo) {
        return args -> {

            if (privRepo.count() == 0) {
                privRepo.saveAll(List.of(
                    new Privilegio(null, "CREAR_USUARIO", "Puede crear usuarios", null),
                    new Privilegio(null, "EDITAR_USUARIO", "Puede editar usuarios", null),
                    new Privilegio(null, "ELIMINAR_USUARIO", "Puede eliminar usuarios", null),
                    new Privilegio(null, "VER_PROYECTOS", "Puede ver proyectos", null),
                    new Privilegio(null, "CREAR_PROYECTO", "Puede crear proyectos", null),
                    new Privilegio(null, "EDITAR_PROYECTO", "Puede editar proyectos", null),
                    new Privilegio(null, "CREAR_TAREA", "Puede crear tareas", null),
                    new Privilegio(null, "EDITAR_TAREA", "Puede editar tareas", null),
                    new Privilegio(null, "ASIGNAR_TAREA", "Puede asignar tareas", null)
                ));
            }

            if (rolRepo.count() == 0) {
                rolRepo.saveAll(List.of(
                    new Rol(null, "Administrador", null),
                    new Rol(null, "Gestor", null),
                    new Rol(null, "Colaborador", null)
                ));
            }

            if (rolPrivRepo.count() == 0) {
                Rol admin = rolRepo.findById(1L).get();
                Rol gestor = rolRepo.findById(2L).get();
                Rol colaborador = rolRepo.findById(3L).get();

                Privilegio crearUser = privRepo.findById(1L).get();
                Privilegio editarUser = privRepo.findById(2L).get();
                Privilegio eliminarUser = privRepo.findById(3L).get();
                Privilegio verProy = privRepo.findById(4L).get();
                Privilegio crearProy = privRepo.findById(5L).get();
                Privilegio editarProy = privRepo.findById(6L).get();
                Privilegio crearTarea = privRepo.findById(7L).get();
                Privilegio editarTarea = privRepo.findById(8L).get();
                Privilegio asignarTarea = privRepo.findById(9L).get();

                rolPrivRepo.saveAll(List.of(
                    // Administrador tiene todos los privilegios
                    new RolPriv(null, admin, crearUser),
                    new RolPriv(null, admin, editarUser),
                    new RolPriv(null, admin, eliminarUser),
                    new RolPriv(null, admin, verProy),
                    new RolPriv(null, admin, crearProy),
                    new RolPriv(null, admin, editarProy),
                    new RolPriv(null, admin, crearTarea),
                    new RolPriv(null, admin, editarTarea),
                    new RolPriv(null, admin, asignarTarea),

                    // Gestor puede ver y gestionar proyectos y tareas
                    new RolPriv(null, gestor, verProy),
                    new RolPriv(null, gestor, crearProy),
                    new RolPriv(null, gestor, editarProy),
                    new RolPriv(null, gestor, crearTarea),
                    new RolPriv(null, gestor, editarTarea),
                    new RolPriv(null, gestor, asignarTarea),

                    // Colaborador solo puede ver proyectos y editar tareas
                    new RolPriv(null, colaborador, verProy),
                    new RolPriv(null, colaborador, editarTarea)
                ));
            }
        };
    }
}
