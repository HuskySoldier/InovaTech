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

                // ==========================================
                // PROYECTO 1 (ID: 1L)
                // ==========================================
                Proyecto p1 = new Proyecto();
                p1.setNombre("Implementación de ERP");
                p1.setDescripcion("Despliegue e integración del nuevo sistema ERP para Grupo Cordillera.");
                p1.setFechaInicio(LocalDate.now().minusDays(30)); // Empezó hace un mes
                p1.setFechaTerminoEsti(LocalDate.now().plusMonths(3));
                p1.setPresuEstimado(new BigDecimal("15000000"));
                p1.setIdEstado(5L); // 5 = En Progreso

                // ==========================================
                // PROYECTO 2 (ID: 2L)
                // ==========================================
                Proyecto p2 = new Proyecto();
                p2.setNombre("Actualización de Infraestructura Cloud");
                p2.setDescripcion("Migración de servidores on-premise a AWS para optimizar costos operativos.");
                p2.setFechaInicio(LocalDate.now().minusDays(15)); // Empezó hace 15 días
                p2.setFechaTerminoEsti(LocalDate.now().plusMonths(1));
                p2.setPresuEstimado(new BigDecimal("8500000"));
                p2.setIdEstado(5L); // 5 = En Progreso

                // ==========================================
                // PROYECTO 3 (ID: 3L)
                // ==========================================
                Proyecto p3 = new Proyecto();
                p3.setNombre("Rediseño de Plataforma E-commerce");
                p3.setDescripcion("Modernización de la interfaz de usuario y pasarela de pagos B2C.");
                p3.setFechaInicio(LocalDate.now().plusDays(10)); // Empieza en el futuro
                p3.setFechaTerminoEsti(LocalDate.now().plusMonths(4));
                p3.setPresuEstimado(new BigDecimal("12000000"));
                p3.setIdEstado(4L); // 4 = Planificación

                // ==========================================
                // PROYECTO 4 (ID: 4L)
                // ==========================================
                Proyecto p4 = new Proyecto();
                p4.setNombre("Sistema de Monitoreo de Rendimiento");
                p4.setDescripcion("Desarrollo de dashboard interno para analizar métricas de ventas y KPIs en tiempo real.");
                p4.setFechaInicio(LocalDate.now().minusMonths(2)); // Empezó hace 2 meses
                p4.setFechaTerminoEsti(LocalDate.now().minusDays(5)); // Debió terminar hace 5 días
                p4.setPresuEstimado(new BigDecimal("6000000"));
                p4.setIdEstado(6L); // 6 = Completado (o puedes usar el ID de "Atrasado" si tienes uno)

                proyectoRepository.saveAll(Arrays.asList(p1, p2, p3, p4));
                System.out.println("¡4 Proyectos creados exitosamente!");
            }
        };
    }
}