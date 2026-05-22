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

                // Tareas para el Proyecto 1 (Implementación de ERP)
                Tarea t1 = new Tarea();
                t1.setNombre("Levantamiento de requerimientos");
                t1.setDescripcion("Entrevistas con los stakeholders para definir módulos del ERP.");
                t1.setFLimiteTerm(LocalDate.now().plusDays(15));
                t1.setPresupuestoAsignado(new BigDecimal("500000"));
                t1.setIdEstado(2L); // Asumiendo que 2 es "Completado" o "En revisión"
                t1.setIdPrioridad(1L); // Asumiendo que 1 es "Alta"
                t1.setProyectoId(1L); // Asociado al Proyecto 1

                Tarea t2 = new Tarea();
                t2.setNombre("Configuración de base de datos");
                t2.setDescripcion("Instalación y configuración de PostgreSQL para el ERP.");
                t2.setFLimiteTerm(LocalDate.now().plusDays(30));
                t2.setPresupuestoAsignado(new BigDecimal("1200000"));
                t2.setIdEstado(1L); 
                t2.setIdPrioridad(2L); 
                t2.setProyectoId(1L); // Asociado al Proyecto 1

                // Tarea para el Proyecto 2 (Actualización Cloud)
                Tarea t3 = new Tarea();
                t3.setNombre("Creación de VPC y Subredes");
                t3.setDescripcion("Configuración de la red virtual en AWS.");
                t3.setFLimiteTerm(LocalDate.now().plusDays(10));
                t3.setPresupuestoAsignado(new BigDecimal("300000"));
                t3.setIdEstado(1L);
                t3.setIdPrioridad(1L);
                t3.setProyectoId(2L); // Asociado al Proyecto 2

                tareaRepository.saveAll(Arrays.asList(t1, t2, t3));
                System.out.println("Tareas creadas exitosamente.");
            }
        };
    }
}
