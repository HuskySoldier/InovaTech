package com.innovatech.Equipos.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.Equipos.client.ProyectoClient;
import com.innovatech.Equipos.client.UsuarioClient;
import com.innovatech.Equipos.model.Equipo;
import com.innovatech.Equipos.model.Integrante;
import com.innovatech.Equipos.repository.EquipoRepository;
import com.innovatech.Equipos.repository.IntegranteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private IntegranteRepository integranteRepository;

    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    public Equipo obtenerPorId(Long id) {
        return equipoRepository.findById(id).orElse(null);
    }

    public List<Equipo> listarPorProyecto(Long idProyecto) {
        return equipoRepository.findByIdProyecto(idProyecto);
    }

    public Equipo crearEquipo(Equipo equipo) {
        try {
            proyectoClient.obtenerProyectoPorId(equipo.getIdProyecto());
        } catch (Exception e) {
            throw new RuntimeException("Proyecto no encontrado con id: " + equipo.getIdProyecto());
        }
        return equipoRepository.save(equipo);
    }

    @Autowired
    private ProyectoClient proyectoClient;
    @Autowired
    private UsuarioClient usuarioClient;

    public Equipo agregarIntegrante(Long idEquipo, Long idUser) {
    // 1. Llamada interna a MS Usuarios
    try {
        usuarioClient.obtenerUsuarioPorId(idUser);
    } catch (Exception e) {
        throw new RuntimeException("Error: El usuario con ID " + idUser + " no existe.");
    }

    // 2. Lógica de guardado habitual
    Equipo equipo = equipoRepository.findById(idEquipo)
            .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
    
    Integrante nuevoIntegrante = new Integrante();
    nuevoIntegrante.setEquipo(equipo);
    nuevoIntegrante.setIdUser(idUser);
    
    integranteRepository.save(nuevoIntegrante);
    equipo.getIntegrantes().add(nuevoIntegrante);
    return equipo;
    }

    public void eliminarEquipo(Long id) {
        equipoRepository.deleteById(id);
    }

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue("proyecto.creado.queue"))
    public void procesarProyectoCreado(String mensaje) {
        System.out.println("\n---------------------------------------------------------");
        System.out.println(" EQUIPOS: Evento recibido -> " + mensaje);
        System.out.println("Iniciando lógica de asignación o revisión de equipos...");
        System.out.println("---------------------------------------------------------\n");
        
        // AQUÍ PUEDES PONER TU LÓGICA:
        // 1. Extraer el ID del proyecto del mensaje
        // 2. Verificar disponibilidad de los equipos
        // 3. Vincular equipos al proyecto mediante un repositorio
    }

    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue("usuario.desactivado.queue.equipos"))
    public void removerIntegranteDeEquipo(String mensaje) {
    // 1. Extraer ID
    // 2. integranteRepository.deleteByIdUsuario(idUsuario);
    System.out.println("👥 EQUIPOS: Removiendo usuario desactivado de los equipos.");
    }
}