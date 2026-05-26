package com.innovatech.asignaciones.config;

import com.innovatech.asignaciones.model.Asignacion;
import com.innovatech.asignaciones.repository.AsignacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAsignaciones(AsignacionRepository asignacionRepository) {
        return args -> {
            if (asignacionRepository.count() == 0) {
                System.out.println("Inicializando datos de prueba para Asignaciones...");

                LocalDateTime ahora = LocalDateTime.now();

                // =========================================================
                // PROYECTO 1 (ERP) - Asignado principalmente al Equipo 2 (Backend)
                // =========================================================
                // Tarea 1: Levantamiento -> Asignada al Gestor Backend (Integrante 5)
                Asignacion a1 = new Asignacion(null, ahora.minusDays(12), 1L, 5L);
                // Tarea 2: Arquitectura -> Asignada a Backend Senior (Integrante 6)
                Asignacion a2 = new Asignacion(null, ahora.minusDays(10), 2L, 6L);
                // Tarea 3: Módulo RRHH -> Asignada a Backend (Integrante 7)
                Asignacion a3 = new Asignacion(null, ahora.minusDays(2), 3L, 7L);
                // Tarea 4: QA ERP -> Asignada a Backend (Integrante 8)
                Asignacion a4 = new Asignacion(null, ahora.minusDays(1), 4L, 8L);

                // =========================================================
                // PROYECTO 2 (Cloud AWS) - Asignado al Equipo 3 (DevOps)
                // =========================================================
                // Tarea 5: Auditoría -> Gestor DevOps (Integrante 9)
                Asignacion a5 = new Asignacion(null, ahora.minusDays(15), 5L, 9L);
                // Tarea 6: VPC -> DevOps (Integrante 10)
                Asignacion a6 = new Asignacion(null, ahora.minusDays(8), 6L, 10L);
                // Tarea 7: RDS (Bloqueada) -> DevOps (Integrante 11)
                Asignacion a7 = new Asignacion(null, ahora.minusDays(5), 7L, 11L);
                // Tarea 8: Balanceador -> DevOps (Integrante 12)
                Asignacion a8 = new Asignacion(null, ahora.minusDays(1), 8L, 12L);

                // =========================================================
                // PROYECTO 3 (E-commerce) - Asignado al Equipo 1 (Front/UX)
                // =========================================================
                // Tarea 9: Wireframes -> UX Designer (Integrante 4)
                Asignacion a9 = new Asignacion(null, ahora.minusDays(3), 9L, 4L);
                // Tarea 10: Pasarela de Pago -> Gestor Front (Integrante 1) y Dev Front (Integrante 2)
                Asignacion a10 = new Asignacion(null, ahora, 10L, 1L); // Dos personas a una tarea compleja
                Asignacion a11 = new Asignacion(null, ahora, 10L, 2L);
                // Tarea 11: Maquetación Angular -> Dev Front (Integrante 3)
                Asignacion a12 = new Asignacion(null, ahora.plusDays(1), 11L, 3L);

                // =========================================================
                // PROYECTO 4 (Monitoreo) - Asignado al Equipo 4 (Célula Mixta)
                // =========================================================
                // Tarea 12: Métricas -> Gestor / PM (Integrante 13)
                Asignacion a13 = new Asignacion(null, ahora.minusDays(45), 12L, 13L);
                // Tarea 13: Gráficos Chart.js -> Dev Front (Integrante 15)
                Asignacion a14 = new Asignacion(null, ahora.minusDays(20), 13L, 15L);
                // Tarea 14: Consultas SQL (Bloqueada) -> Dev Backend (Integrante 14)
                Asignacion a15 = new Asignacion(null, ahora.minusDays(5), 14L, 14L);

                asignacionRepository.saveAll(Arrays.asList(
                    a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15
                ));
                System.out.println("¡15 Asignaciones creadas exitosamente para los integrantes de equipos!");
            }
        };
    }
}