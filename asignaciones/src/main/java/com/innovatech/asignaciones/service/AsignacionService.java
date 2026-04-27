package com.innovatech.asignaciones.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.asignaciones.client.EquipoClient;
import com.innovatech.asignaciones.client.TareaClient;
import com.innovatech.asignaciones.model.Asignacion;
import com.innovatech.asignaciones.repository.AsignacionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsignacionService {

    @Autowired
    private AsignacionRepository asignacionRepository;

    @Autowired
    private EquipoClient equipoClient;

    @Autowired
    private TareaClient tareaClient;

    public List<Asignacion> listarTodas() {
        return asignacionRepository.findAll();
    }

    public Asignacion crearAsignacion(Long idTarea, Long idIntegrante) {
        // 1. Validar existencia de la Tarea (Cuando MS Tareas esté listo)
        try {
            tareaClient.obtenerTareaPorId(idTarea);
        } catch (Exception e) {
            throw new RuntimeException("La tarea especificada no existe.");
        }

        // 2. Validar existencia del Integrante en el MS Equipos
        /* Nota: Para que esto funcione 100%, deberás agregar el endpoint 
           GET /api/equipos/integrante/{id} en tu MS Equipos posteriormente.
        try {
            equipoClient.obtenerIntegrantePorId(idIntegrante);
        } catch (Exception e) {
            throw new RuntimeException("El integrante especificado no existe.");
        }
        */

        Asignacion nuevaAsignacion = new Asignacion();
        nuevaAsignacion.setIdTarea(idTarea);
        nuevaAsignacion.setIdIntegrante(idIntegrante);
        nuevaAsignacion.setFecha_asignacion(LocalDateTime.now()); // Automático

        return asignacionRepository.save(nuevaAsignacion);
    }
}