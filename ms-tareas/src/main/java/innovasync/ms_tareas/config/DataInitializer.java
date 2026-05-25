package innovasync.ms_tareas.config;

import innovasync.ms_tareas.model.Prioridad;
import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.repository.PrioridadRepository;
import innovasync.ms_tareas.repository.TareaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initTareas(TareaRepository tareaRepository, PrioridadRepository prioridadRepository) {
        return args -> {

            // 1. Inicializar prioridades en la base de datos si está vacía
            if (prioridadRepository.count() == 0) {
                Prioridad alta = new Prioridad();
                alta.setNombre("Alta");

                Prioridad media = new Prioridad();
                media.setNombre("Media");

                Prioridad baja = new Prioridad();
                baja.setNombre("Baja");

                prioridadRepository.saveAll(Arrays.asList(alta, media, baja));
                System.out.println("¡3 Prioridades creadas exitosamente!");
            }

            // 2. Recuperar las prioridades de la base de datos para ligarlas a las tareas
            List<Prioridad> todasLasPrioridades = prioridadRepository.findAll();

            Prioridad pAlta = todasLasPrioridades.stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Alta"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Prioridad 'Alta' no encontrada en la BD"));

            Prioridad pMedia = todasLasPrioridades.stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Media"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Prioridad 'Media' no encontrada en la BD"));

            Prioridad pBaja = todasLasPrioridades.stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase("Baja"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Prioridad 'Baja' no encontrada en la BD"));

            // 3. Verificamos si la tabla de tareas está vacía antes de insertar
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
                t1.setPrioridad(pAlta); // <--- LIGADO AL OBJETO
                t1.setProyectoId(1L);

                // Tarea 1.2 - COMPLETADA (ID 10)
                Tarea t2 = new Tarea();
                t2.setNombre("Diseño de Arquitectura de Software");
                t2.setDescripcion("Definición de microservicios y diagramas de base de datos.");
                t2.setFLimiteTerm(LocalDate.now().minusDays(2));
                t2.setPresupuestoAsignado(new BigDecimal("800000"));
                t2.setPresupuestoFinal(new BigDecimal("850000"));
                t2.setIdEstado(10L);
                t2.setPrioridad(pAlta); // <--- LIGADO AL OBJETO
                t2.setProyectoId(1L);

                // Tarea 1.3 - EN PROGRESO (ID 9)
                Tarea t3 = new Tarea();
                t3.setNombre("Desarrollo módulo de Recursos Humanos");
                t3.setDescripcion("Creación de CRUD para empleados y gestión de nóminas.");
                t3.setFLimiteTerm(LocalDate.now().plusDays(15));
                t3.setPresupuestoAsignado(new BigDecimal("2500000"));
                t3.setIdEstado(9L);
                t3.setPrioridad(pMedia); // <--- LIGADO AL OBJETO
                t3.setProyectoId(1L);

                // Tarea 1.4 - PENDIENTE (ID 8)
                Tarea t4 = new Tarea();
                t4.setNombre("Pruebas de Integración (QA)");
                t4.setDescripcion("Ejecución de pruebas end-to-end del sistema ERP completo.");
                t4.setFLimiteTerm(LocalDate.now().plusDays(40));
                t4.setPresupuestoAsignado(new BigDecimal("1200000"));
                t4.setIdEstado(8L);
                t4.setPrioridad(pAlta); // <--- LIGADO AL OBJETO
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
                t5.setPrioridad(pMedia); // <--- LIGADO AL OBJETO
                t5.setProyectoId(2L);

                // Tarea 2.2 - EN PROGRESO (ID 9)
                Tarea t6 = new Tarea();
                t6.setNombre("Creación de VPC y Subredes en AWS");
                t6.setDescripcion("Configuración de la red virtual, tablas de enrutamiento y gateways.");
                t6.setFLimiteTerm(LocalDate.now().plusDays(10));
                t6.setPresupuestoAsignado(new BigDecimal("600000"));
                t6.setIdEstado(9L);
                t6.setPrioridad(pAlta); // <--- LIGADO AL OBJETO
                t6.setProyectoId(2L);

                // Tarea 2.3 - BLOQUEADA (ID 11)
                Tarea t7 = new Tarea();
                t7.setNombre("Migración de Base de Datos a RDS");
                t7.setDescripcion(
                        "Volcado de datos. BLOQUEADA: Esperando que el cliente libere credenciales del firewall antiguo.");
                t7.setFLimiteTerm(LocalDate.now().plusDays(12));
                t7.setPresupuestoAsignado(new BigDecimal("1500000"));
                t7.setIdEstado(11L);
                t7.setPrioridad(pBaja); // <--- LIGADO AL OBJETO
                t7.setProyectoId(2L);

                // Tarea 2.4 - PENDIENTE (ID 8)
                Tarea t8 = new Tarea();
                t8.setNombre("Configuración de Balanceador de Carga");
                t8.setDescripcion("Implementación de Application Load Balancer y Auto Scaling Groups.");
                t8.setFLimiteTerm(LocalDate.now().plusDays(25));
                t8.setPresupuestoAsignado(new BigDecimal("900000"));
                t8.setIdEstado(8L);
                t8.setPrioridad(pMedia); // <--- LIGADO AL OBJETO
                t8.setProyectoId(2L);

                // ==========================================
                // PROYECTO 3: Rediseño de E-commerce (ID: 3L)
                // (Proyecto en etapa temprana / planificación)
                // ==========================================

                // Tarea 3.1 - PENDIENTE (ID 8)
                Tarea t9 = new Tarea();
                t9.setNombre("Diseño de Wireframes UX/UI");
                t9.setDescripcion("Creación de prototipos en Figma para la nueva tienda y flujo de compra.");
                t9.setFLimiteTerm(LocalDate.now().plusDays(20));
                t9.setPresupuestoAsignado(new BigDecimal("700000"));
                t9.setIdEstado(8L);
                t9.setPrioridad(pAlta);
                t9.setProyectoId(3L);

                // Tarea 3.2 - EN PROGRESO (ID 9)
                Tarea t10 = new Tarea();
                t10.setNombre("Configuración de pasarela de pago");
                t10.setDescripcion("Integración de la API de Webpay y pruebas en entorno sandbox.");
                t10.setFLimiteTerm(LocalDate.now().plusDays(35));
                t10.setPresupuestoAsignado(new BigDecimal("1200000"));
                t10.setIdEstado(9L);
                t10.setPrioridad(pAlta);
                t10.setProyectoId(3L);

                // Tarea 3.3 - PENDIENTE (ID 8)
                Tarea t11 = new Tarea();
                t11.setNombre("Maquetación del catálogo de productos");
                t11.setDescripcion("Desarrollo de componentes en Angular para mostrar los artículos.");
                t11.setFLimiteTerm(LocalDate.now().plusDays(50));
                t11.setPresupuestoAsignado(new BigDecimal("950000"));
                t11.setIdEstado(8L);
                t11.setPrioridad(pMedia);
                t11.setProyectoId(3L);

                // ==========================================
                // PROYECTO 4: Monitoreo de Rendimiento (ID: 4L)
                // (Proyecto en su etapa final / completado)
                // ==========================================

                // Tarea 4.1 - COMPLETADA (ID 10)
                Tarea t12 = new Tarea();
                t12.setNombre("Levantamiento de métricas clave");
                t12.setDescripcion("Definición de los KPIs de negocio a mostrar en el dashboard principal.");
                t12.setFLimiteTerm(LocalDate.now().minusDays(40));
                t12.setPresupuestoAsignado(new BigDecimal("400000"));
                t12.setPresupuestoFinal(new BigDecimal("400000"));
                t12.setIdEstado(10L);
                t12.setPrioridad(pAlta);
                t12.setProyectoId(4L);

                // Tarea 4.2 - COMPLETADA (ID 10)
                Tarea t13 = new Tarea();
                t13.setNombre("Desarrollo de gráficos dinámicos");
                t13.setDescripcion("Implementación de Chart.js para visualización de ventas en tiempo real.");
                t13.setFLimiteTerm(LocalDate.now().minusDays(15));
                t13.setPresupuestoAsignado(new BigDecimal("850000"));
                t13.setPresupuestoFinal(new BigDecimal("900000")); // Gastaron un poco más
                t13.setIdEstado(10L);
                t13.setPrioridad(pMedia);
                t13.setProyectoId(4L);

                // Tarea 4.3 - BLOQUEADA (ID 11)
                Tarea t14 = new Tarea();
                t14.setNombre("Optimización de consultas SQL masivas");
                t14.setDescripcion(
                        "Mejora en tiempos de respuesta. BLOQUEADA: Faltan permisos de DBA para crear índices.");
                t14.setFLimiteTerm(LocalDate.now().minusDays(2));
                t14.setPresupuestoAsignado(new BigDecimal("600000"));
                t14.setIdEstado(11L);
                t14.setPrioridad(pAlta);
                t14.setProyectoId(4L);

                tareaRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
                System.out.println(
                        "¡14 Tareas creadas y asignadas a proyectos exitosamente con sus prioridades relacionales!");
            }
        };
    }
}