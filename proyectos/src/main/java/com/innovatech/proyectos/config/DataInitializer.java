package com.innovatech.proyectos.config;

import com.innovatech.proyectos.model.Proyecto;
import com.innovatech.proyectos.repository.ProyectoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initProyectos(ProyectoRepository proyectoRepository) {
        return args -> {
            // Verificamos si la tabla está vacía antes de insertar
            if (proyectoRepository.count() == 0) {
                System.out.println("Inicializando datos de prueba para Proyectos...");

                Proyecto p1 = new Proyecto();
                p1.setNombre("Implementación de ERP");
                p1.setDescripcion("Despliegue e integración del nuevo sistema ERP para Grupo Cordillera.");
                p1.setFechaInicio(LocalDate.now().minusDays(10));
                p1.setFechaTerminoEsti(LocalDate.now().plusMonths(3));
                p1.setPresuEstimado(new BigDecimal("15000000"));
                p1.setIdEstado(1L); // Asumiendo que 1 es "Activo" o "En progreso"

                Proyecto p2 = new Proyecto();
                p2.setNombre("Actualización de Infraestructura Cloud");
                p2.setDescripcion("Migración de servidores on-premise a AWS.");
                p2.setFechaInicio(LocalDate.now().minusDays(5));
                p2.setFechaTerminoEsti(LocalDate.now().plusMonths(1));
                p2.setPresuEstimado(new BigDecimal("8500000"));
                p2.setIdEstado(1L);

                proyectoRepository.saveAll(Arrays.asList(p1, p2));
                System.out.println("Proyectos creados exitosamente.");
            }
        };
    }
}
