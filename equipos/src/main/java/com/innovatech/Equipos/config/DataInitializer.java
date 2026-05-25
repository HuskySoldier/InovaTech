package com.innovatech.Equipos.config;

import com.innovatech.Equipos.model.Equipo;
import com.innovatech.Equipos.model.Integrante;
import com.innovatech.Equipos.repository.EquipoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initEquipos(EquipoRepository equipoRepository) {
        return args -> {
            
            // Verificamos si ya existen datos para no duplicar en cada reinicio
            if (equipoRepository.count() == 0) {
                System.out.println("Inicializando datos de prueba para Equipos e Integrantes...");

                List<Equipo> equiposAInserts = new ArrayList<>();

                // =================================================================
                // EQUIPO 1: Desarrollo Frontend y UX (Asignado al Proyecto ID: 1L)
                // =================================================================
                Equipo eq1 = new Equipo();
                eq1.setNombre("Equipo de Desarrollo Frontend");
                eq1.setIdProyecto(1L);

                // IDs de usuarios en MS Usuarios: 2 (Gestor), 3, 4, 5 (Colaboradores)
                eq1.getIntegrantes().add(new Integrante(null, eq1, 2L));
                eq1.getIntegrantes().add(new Integrante(null, eq1, 3L));
                eq1.getIntegrantes().add(new Integrante(null, eq1, 4L));
                eq1.getIntegrantes().add(new Integrante(null, eq1, 5L));
                
                equiposAInserts.add(eq1);

                // =================================================================
                // EQUIPO 2: Desarrollo Backend (Asignado al Proyecto ID: 1L o 2L)
                // =================================================================
                Equipo eq2 = new Equipo();
                eq2.setNombre("Equipo de Desarrollo Backend");
                eq2.setIdProyecto(2L);

                // IDs de usuarios en MS Usuarios: 6 (Gestor), 7, 8, 9 (Colaboradores)
                eq2.getIntegrantes().add(new Integrante(null, eq2, 6L));
                eq2.getIntegrantes().add(new Integrante(null, eq2, 7L));
                eq2.getIntegrantes().add(new Integrante(null, eq2, 8L));
                eq2.getIntegrantes().add(new Integrante(null, eq2, 9L));
                
                equiposAInserts.add(eq2);

                // =================================================================
                // EQUIPO 3: Infraestructura y DevOps (Asignado al Proyecto ID: 2L)
                // =================================================================
                Equipo eq3 = new Equipo();
                eq3.setNombre("Célula Cloud & DevOps");
                eq3.setIdProyecto(3L);

                // IDs de usuarios en MS Usuarios: 10 (Gestor), 11, 12, 13 (Colaboradores)
                eq3.getIntegrantes().add(new Integrante(null, eq3, 10L));
                eq3.getIntegrantes().add(new Integrante(null, eq3, 11L));
                eq3.getIntegrantes().add(new Integrante(null, eq3, 12L));
                eq3.getIntegrantes().add(new Integrante(null, eq3, 13L));
                
                equiposAInserts.add(eq3);

                // =================================================================
                // EQUIPO 4: Célula Ágil Mixta (Asignado al Proyecto ID: 2L)
                // =================================================================
                Equipo eq4 = new Equipo();
                eq4.setNombre("Célula Ágil de Innovación");
                eq4.setIdProyecto(4L);

                // IDs de usuarios en MS Usuarios: 14 (Gestor), 15, 16, 17 (Colaboradores)
                eq4.getIntegrantes().add(new Integrante(null, eq4, 14L));
                eq4.getIntegrantes().add(new Integrante(null, eq4, 15L));
                eq4.getIntegrantes().add(new Integrante(null, eq4, 16L));
                eq4.getIntegrantes().add(new Integrante(null, eq4, 17L));
                
                equiposAInserts.add(eq4);

                // Gracias al CascadeType.ALL en el modelo Equipo, guardar los equipos
                // registrará automáticamente a todos sus integrantes en la tabla 'integrantes'
                equipoRepository.saveAll(equiposAInserts);
                System.out.println("¡4 Equipos y sus 16 integrantes guardados correctamente!");
            }
        };
    }
}