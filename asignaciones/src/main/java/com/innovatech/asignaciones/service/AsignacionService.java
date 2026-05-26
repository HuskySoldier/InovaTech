package com.innovatech.asignaciones.service;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener; // Importación de RabbitMQ
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.asignaciones.client.EquipoClient;
import com.innovatech.asignaciones.client.ProyectoClient;
import com.innovatech.asignaciones.client.TareaClient;
import com.innovatech.asignaciones.dto.IntegranteDTO;
import com.innovatech.asignaciones.dto.ProyectoDTO;
import com.innovatech.asignaciones.dto.RespuestaSemanasDTO;
import com.innovatech.asignaciones.dto.SemanaDetalleDTO;
import com.innovatech.asignaciones.dto.TareaDTO;
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

    @Autowired
    private ProyectoClient proyectoClient;

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
            key = "tarea.creada.routing.key"))
    public void recibirNotificacionTareaCreada(String mensaje) {
        System.out.println("\n=========================================================");
        System.out.println(" EVENTO ASÍNCRONO RECIBIDO EN ASIGNACIONES-SERVICE ");
        System.out.println("Mensaje: " + mensaje);
        System.out.println("=========================================================\n");
    }

    @RabbitListener(bindings = @org.springframework.amqp.rabbit.annotation.QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "usuario.desactivado.queue.asignaciones", durable = "true"),
            // Enlaza la cola al exchange del microservicio usuarios
            exchange = @org.springframework.amqp.rabbit.annotation.Exchange(value = "usuarios.exchange", type = org.springframework.amqp.core.ExchangeTypes.TOPIC), key = "usuario.desactivado.routing.key"))
    public void limpiarAsignacionesDeUsuario(String mensaje) {
        System.out.println(" ASIGNACIONES: Limpiando tareas del usuario desactivado. Mensaje: " + mensaje);
    }

    public RespuestaSemanasDTO obtenerCargaSemanal2(Long idUser) {

        // 1. Obtener el idIntegrante asociado a este idUser
        IntegranteDTO integrante = equipoClient.obtenerIntegrantePorId(idUser);

        // 2. Buscar todas las asignaciones de este integrante
        List<Asignacion> asignaciones = asignacionRepository.findByIdIntegrante(integrante.getIdIntegrante());

        if (asignaciones.isEmpty()) {
            RespuestaSemanasDTO vacio = new RespuestaSemanasDTO();
            vacio.setIdUser(idUser);
            vacio.setSemanas(List.of());
            return vacio;
        }

        // 3. Extraer IDs y traer la información de Tareas y Proyectos
        List<Long> idsTareas = asignaciones.stream().map(Asignacion::getIdTarea).toList();
        System.out.println("1. IDs Tareas a buscar: " + idsTareas);
        List<TareaDTO> tareas = tareaClient.obtenerTareasBatch(idsTareas);
        System.out.println("2. Tareas recibidas: " + tareas);

        List<Long> idsProyectos = tareas.stream().map(TareaDTO::getProyectoId).distinct().toList();
        System.out.println("3. IDs Proyectos a buscar: " + idsProyectos);
        List<ProyectoDTO> proyectos = proyectoClient.obtenerProyectosBatch(idsProyectos);
        System.out.println("4. Proyectos recibidos: " + proyectos);

        // Crear diccionarios (Maps) para búsquedas ultrarrápidas en memoria
        Map<Long, Long> mapaTareaProyecto = tareas.stream()
                .collect(Collectors.toMap(TareaDTO::getId, TareaDTO::getProyectoId));

        Map<Long, String> mapaProyectoNombre = proyectos.stream()
                .collect(Collectors.toMap(ProyectoDTO::getIdProyecto, ProyectoDTO::getNombre));

        // 4. Agrupar las asignaciones por Semana y por Proyecto
        // Usamos un Map anidado: Map<Semana, Map<NombreProyecto, CantidadAsignaciones>>
        Map<Integer, Map<String, Integer>> agrupacionSemanas = new HashMap<>();

        for (Asignacion asig : asignaciones) {
            // Extraemos el número de la semana (Ej: Semana 15 del año)
            int numeroSemana = asig.getFecha_asignacion().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

            // Navegamos: Asignacion -> Tarea -> Proyecto -> Nombre
            Long idProyecto = mapaTareaProyecto.get(asig.getIdTarea());
            String nombreProyecto = mapaProyectoNombre.getOrDefault(idProyecto, "Proyecto Desconocido");

            // Registramos la asignación en nuestra estructura
            agrupacionSemanas.putIfAbsent(numeroSemana, new HashMap<>());
            Map<String, Integer> conteoPorProyecto = agrupacionSemanas.get(numeroSemana);
            conteoPorProyecto.put(nombreProyecto, conteoPorProyecto.getOrDefault(nombreProyecto, 0) + 1);
        }

        // 5. Convertir nuestra agrupación al JSON exacto que pide el Frontend
        List<SemanaDetalleDTO> listaSemanas = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, Integer>> entrySemana : agrupacionSemanas.entrySet()) {
            int semana = entrySemana.getKey();

            for (Map.Entry<String, Integer> entryProyecto : entrySemana.getValue().entrySet()) {
                SemanaDetalleDTO detalle = new SemanaDetalleDTO();
                detalle.setSemana(semana);
                detalle.setProyecto(entryProyecto.getKey());

                // MOCK DE HORAS: Como no tienes campo de horas, multiplicamos la cantidad
                // de asignaciones que tuvo en esa semana por 15 horas a modo de ejemplo.
                int cantidadAsignaciones = entryProyecto.getValue();
                detalle.setHoras(cantidadAsignaciones * 15);

                listaSemanas.add(detalle);
            }
        }

        // Ordenamos la lista para que la semana 1 salga antes que la 2
        listaSemanas.sort(Comparator.comparingInt(SemanaDetalleDTO::getSemana));

        RespuestaSemanasDTO respuestaFinal = new RespuestaSemanasDTO();
        respuestaFinal.setIdUser(idUser);
        respuestaFinal.setSemanas(listaSemanas);

        return respuestaFinal;
    }
}