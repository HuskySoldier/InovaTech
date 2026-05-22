package innovasync.ms_tareas.config;

import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.repository.TareaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initTareas(TareaRepository tareaRepository) {
        return args -> {
            // Verificamos si la tabla está vacía antes de insertar
            if (tareaRepository.count() == 0) {
                System.out.println("Inicializando datos de prueba para Tareas...");

                // ==========================================
                // PROYECTO 1: Implementación de ERP (ID: 1L)
                // ==========================================
                
                // Tarea 1.1 - COMPLETADA (ID 10)
                Tarea t1 = new Tarea();
                t1.setNombre("Levantamiento de requerimientos");
                t1.setDescripcion("Entrevistas con los stakeholders para definir módulos del ERP.");
                t1.setFLimiteTerm(LocalDate.now().minusDays(10)); 
                t1.setPresupuestoAsignado(new BigDecimal("500000"));
                t1.setPresupuestoFinal(new BigDecimal("480000")); 
                t1.setIdEstado(10L); 
                t1.setIdPrioridad(1L); // Alta
                t1.setProyectoId(1L); 

                // Tarea 1.2 - COMPLETADA (ID 10)
                Tarea t2 = new Tarea();
                t2.setNombre("Diseño de Arquitectura de Software");
                t2.setDescripcion("Definición de microservicios y diagramas de base de datos.");
                t2.setFLimiteTerm(LocalDate.now().minusDays(2));
                t2.setPresupuestoAsignado(new BigDecimal("800000"));
                t2.setPresupuestoFinal(new BigDecimal("850000")); // Gastó un poco más
                t2.setIdEstado(10L); 
                t2.setIdPrioridad(1L); 
                t2.setProyectoId(1L); 

                // Tarea 1.3 - EN PROGRESO (ID 9)
                Tarea t3 = new Tarea();
                t3.setNombre("Desarrollo módulo de Recursos Humanos");
                t3.setDescripcion("Creación de CRUD para empleados y gestión de nóminas.");
                t3.setFLimiteTerm(LocalDate.now().plusDays(15));
                t3.setPresupuestoAsignado(new BigDecimal("2500000"));
                t3.setIdEstado(9L); 
                t3.setIdPrioridad(2L); // Media
                t3.setProyectoId(1L); 

                // Tarea 1.4 - PENDIENTE (ID 8)
                Tarea t4 = new Tarea();
                t4.setNombre("Pruebas de Integración (QA)");
                t4.setDescripcion("Ejecución de pruebas end-to-end del sistema ERP completo.");
                t4.setFLimiteTerm(LocalDate.now().plusDays(40));
                t4.setPresupuestoAsignado(new BigDecimal("1200000"));
                t4.setIdEstado(8L); 
                t4.setIdPrioridad(1L); 
                t4.setProyectoId(1L); 


                // ==========================================
                // PROYECTO 2: Actualización Cloud (ID: 2L)
                // ==========================================

                // Tarea 2.1 - COMPLETADA (ID 10)
                Tarea t5 = new Tarea();
                t5.setNombre("Auditoría de infraestructura On-Premise");
                t5.setDescripcion("Mapeo de todos los servidores físicos actuales y sus dependencias.");
                t5.setFLimiteTerm(LocalDate.now().minusDays(5));
                t5.setPresupuestoAsignado(new BigDecimal("400000"));
                t5.setPresupuestoFinal(new BigDecimal("400000"));
                t5.setIdEstado(10L); 
                t5.setIdPrioridad(2L);
                t5.setProyectoId(2L); 

                // Tarea 2.2 - EN PROGRESO (ID 9)
                Tarea t6 = new Tarea();
                t6.setNombre("Creación de VPC y Subredes en AWS");
                t6.setDescripcion("Configuración de la red virtual, tablas de enrutamiento y gateways.");
                t6.setFLimiteTerm(LocalDate.now().plusDays(10));
                t6.setPresupuestoAsignado(new BigDecimal("600000"));
                t6.setIdEstado(9L); 
                t6.setIdPrioridad(1L);
                t6.setProyectoId(2L); 

                // Tarea 2.3 - BLOQUEADA (ID 11)
                Tarea t7 = new Tarea();
                t7.setNombre("Migración de Base de Datos a RDS");
                t7.setDescripcion("Volcado de datos. BLOQUEADA: Esperando que el cliente libere credenciales del firewall antiguo.");
                t7.setFLimiteTerm(LocalDate.now().plusDays(12));
                t7.setPresupuestoAsignado(new BigDecimal("1500000"));
                t7.setIdEstado(11L); 
                t7.setIdPrioridad(1L);
                t7.setProyectoId(2L); 

                // Tarea 2.4 - PENDIENTE (ID 8)
                Tarea t8 = new Tarea();
                t8.setNombre("Configuración de Balanceador de Carga");
                t8.setDescripcion("Implementación de Application Load Balancer y Auto Scaling Groups.");
                t8.setFLimiteTerm(LocalDate.now().plusDays(25));
                t8.setPresupuestoAsignado(new BigDecimal("900000"));
                t8.setIdEstado(8L); 
                t8.setIdPrioridad(2L);
                t8.setProyectoId(2L); 

                // Guardamos todas las tareas de una sola vez
                tareaRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8));
                System.out.println("¡8 Tareas creadas y asignadas a proyectos exitosamente!");
            }
        };
    }
}