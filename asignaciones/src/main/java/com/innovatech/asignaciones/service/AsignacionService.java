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
        // // 1. Validar Tarea (COMENTADO HASTA CREAR MS TAREAS)
        // try {
        //     tareaClient.obtenerTareaPorId(idTarea);
        // } catch (Exception e) {
        //     throw new RuntimeException("La tarea especificada no existe.");
        // }

        // 2. Validar Integrante
        try {
            equipoClient.obtenerIntegrantePorId(idIntegrante);
        } catch (Exception e) {
            throw new RuntimeException("No se puede asignar: El integrante " + idIntegrante + " no existe.");
        }

        Asignacion nuevaAsignacion = new Asignacion();
        nuevaAsignacion.setIdTarea(idTarea);
        nuevaAsignacion.setIdIntegrante(idIntegrante);
        nuevaAsignacion.setFecha_asignacion(LocalDateTime.now());

        return asignacionRepository.save(nuevaAsignacion);
    }
}