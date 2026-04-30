package com.innovatech.proyectos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.proyectos.client.EstadoClient;
import com.innovatech.proyectos.model.HistorialProyecto;
import com.innovatech.proyectos.model.Proyecto;
import com.innovatech.proyectos.repository.HistorialProyectoRepository;
import com.innovatech.proyectos.repository.ProyectoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private HistorialProyectoRepository historialRepository;

    @Autowired
    private EstadoClient estadoClient;

    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
    }

    public Proyecto obtenerPorId(Long id) {
        return proyectoRepository.findById(id).orElse(null);
    }

    public Proyecto crearProyecto(Proyecto proyecto) {

        // Validar que el estado existe en MS Estado
        try {
            estadoClient.obtenerEstadoPorId(proyecto.getIdEstado());
        } catch (Exception e) {
            throw new RuntimeException("Estado no encontrado con id: " + proyecto.getIdEstado());
        }

        Proyecto nuevoProyecto = proyectoRepository.save(proyecto);
        registrarHistorial(nuevoProyecto, "Creación inicial del proyecto.");
        return nuevoProyecto;
    }

    public Proyecto actualizarProyecto(Long id, Proyecto proyectoActualizado) {
        Proyecto existente = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        // Actualizar datos
        existente.setNombre(proyectoActualizado.getNombre());
        existente.setDescripcion(proyectoActualizado.getDescripcion());
        existente.setFechaInicio(proyectoActualizado.getFechaInicio());
        existente.setFechaTerminoEsti(proyectoActualizado.getFechaTerminoEsti());
        existente.setFechaTerminoReal(proyectoActualizado.getFechaTerminoReal());
        existente.setPresuEstimado(proyectoActualizado.getPresuEstimado());
        existente.setPresupReal(proyectoActualizado.getPresupReal());
        
        if (!existente.getIdEstado().equals(proyectoActualizado.getIdEstado())) {
        // Validar que el nuevo estado existe
            try {
                estadoClient.obtenerEstadoPorId(proyectoActualizado.getIdEstado());
            } catch (Exception e) {
                throw new RuntimeException("Estado no encontrado con id: " + proyectoActualizado.getIdEstado());
            }
                existente.setIdEstado(proyectoActualizado.getIdEstado());
                registrarHistorial(existente, "Cambio de estado del proyecto al ID: " + proyectoActualizado.getIdEstado());
        } else {
                registrarHistorial(existente, "Actualización de información del proyecto.");
        }

        return proyectoRepository.save(existente);
    }

    public void eliminarProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }

    public List<HistorialProyecto> verHistorial(Long idProyecto) {
        return historialRepository.findByProyectoIdProyecto(idProyecto);
    }

    // Método privado para automatizar el guardado en el historial
    private void registrarHistorial(Proyecto proyecto, String descripcion) {
        HistorialProyecto historial = new HistorialProyecto();
        historial.setProyecto(proyecto);
        historial.setFecha(LocalDateTime.now());
        historial.setDescripcion(descripcion);
        historialRepository.save(historial);
    }
}