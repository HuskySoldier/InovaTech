package com.innovatech.asignaciones.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener; // Importación de RabbitMQ
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
        // 1. Validar Tarea
         try {
             tareaClient.obtenerTareaPorId(idTarea);
         } catch (Exception e) {
             throw new RuntimeException("La tarea especificada no existe.");
         }

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

    // ==========================================
    // CONSUMIDOR DE RABBITMQ
    // ==========================================
    
    @RabbitListener(bindings = @org.springframework.amqp.rabbit.annotation.QueueBinding(
        // 1. Crea una cola exclusiva para este microservicio
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "tarea.creada.queue.asignaciones", durable = "true"),
        // 2. La enlaza al Exchange donde ms-tareas publica
        exchange = @org.springframework.amqp.rabbit.annotation.Exchange(value = "tareas.exchange", type = org.springframework.amqp.core.ExchangeTypes.TOPIC),
        // 3. Escucha la llave de ruteo específica
        key = "tarea.creada.routing.key"
    ))
    public void recibirNotificacionTareaCreada(String mensaje) {
        System.out.println("\n=========================================================");
        System.out.println(" EVENTO ASÍNCRONO RECIBIDO EN ASIGNACIONES-SERVICE ");
        System.out.println("Mensaje: " + mensaje);
        System.out.println("=========================================================\n");
    }

    @RabbitListener(bindings = @org.springframework.amqp.rabbit.annotation.QueueBinding(
        value = @org.springframework.amqp.rabbit.annotation.Queue(value = "usuario.desactivado.queue.asignaciones", durable = "true"),
        // Enlaza la cola al exchange del microservicio usuarios
        exchange = @org.springframework.amqp.rabbit.annotation.Exchange(value = "usuarios.exchange", type = org.springframework.amqp.core.ExchangeTypes.TOPIC),
        key = "usuario.desactivado.routing.key"
    ))
    public void limpiarAsignacionesDeUsuario(String mensaje) {
        System.out.println(" ASIGNACIONES: Limpiando tareas del usuario desactivado. Mensaje: " + mensaje);
    }
}