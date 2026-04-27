package com.innovatech.estado_service.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.innovatech.estado_service.model.Estado;
import com.innovatech.estado_service.model.Tipo;
import com.innovatech.estado_service.repository.EstadoRepository;
import com.innovatech.estado_service.repository.TipoRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(TipoRepository tipoRepo,
                               EstadoRepository estadoRepo) {
        return args -> {

            if (tipoRepo.count() == 0) {
                tipoRepo.saveAll(List.of(
                    new Tipo(null, "Estado de Usuario", null),
                    new Tipo(null, "Estado de Proyecto", null),
                    new Tipo(null, "Estado de Tarea", null)
                ));
            }

            if (estadoRepo.count() == 0) {
                Tipo tipoUsuario = tipoRepo.findById(1L).get();
                Tipo tipoProyecto = tipoRepo.findById(2L).get();
                Tipo tipoTarea = tipoRepo.findById(3L).get();

                estadoRepo.saveAll(List.of(
                    // Estados de Usuario
                    new Estado(null, "Activo", "Usuario activo en el sistema", tipoUsuario),
                    new Estado(null, "Inactivo", "Usuario inactivo en el sistema", tipoUsuario),
                    new Estado(null, "Suspendido", "Usuario suspendido temporalmente", tipoUsuario),

                    // Estados de Proyecto
                    new Estado(null, "En Planificación", "Proyecto en fase de planificación", tipoProyecto),
                    new Estado(null, "En Progreso", "Proyecto actualmente en desarrollo", tipoProyecto),
                    new Estado(null, "Completado", "Proyecto finalizado exitosamente", tipoProyecto),
                    new Estado(null, "Cancelado", "Proyecto cancelado", tipoProyecto),

                    // Estados de Tarea
                    new Estado(null, "Pendiente", "Tarea pendiente de inicio", tipoTarea),
                    new Estado(null, "En Progreso", "Tarea en desarrollo", tipoTarea),
                    new Estado(null, "Completada", "Tarea finalizada", tipoTarea),
                    new Estado(null, "Bloqueada", "Tarea bloqueada por dependencias", tipoTarea)
                ));
            }
        };
    }
}
