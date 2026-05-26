package com.innovatech.Equipos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.Equipos.config.RabbitMQConfig;
import com.innovatech.Equipos.dto.IntegranteDetalleDTO;
import com.innovatech.Equipos.dto.UsuarioDTO;
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

    @Autowired
    private ProyectoClient proyectoClient;

    @Autowired
    private UsuarioClient usuarioClient;

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

    public Equipo eliminarIntegrante(Long idEquipo, Long idUser) {
        // 1. Buscamos el equipo
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        boolean removido = equipo.getIntegrantes().removeIf(i -> i.getIdUser().equals(idUser));

        if (!removido) {
            throw new RuntimeException("El usuario no es integrante de este equipo");
        }

        return equipoRepository.save(equipo);
    }

    public void eliminarEquipo(Long id) {
        equipoRepository.deleteById(id);
    }

    public List<IntegranteDetalleDTO> obtenerIntegranteDetalle(Long idEquipo) {
        List<Integrante> integrantes = integranteRepository.findByEquipo_IdEquipo(idEquipo);

        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        // 1. Salida rápida si el equipo no tiene integrantes
        if (integrantes.isEmpty()) {
            return List.of();
        }

        List<Long> idsUsuarios = integrantes.stream()
                .map(Integrante::getIdUser)
                .toList();

        // 2. Try-catch para protegerte si ms-usuarios se cae
        List<UsuarioDTO> usuariosTemp;
        try {
            usuariosTemp = usuarioClient.obtenerUsuariosBatch(idsUsuarios);
        } catch (Exception e) {
            System.err.println("Error al contactar ms-usuarios: " + e.getMessage());
            usuariosTemp = List.of(); // Asignamos lista vacía para que el código siga funcionando
        }
        
        final List<UsuarioDTO> usuarios = usuariosTemp;

        // 3. Mapeo seguro contra valores nulos
        List<IntegranteDetalleDTO> detalles = integrantes.stream().map(integrante -> {

            UsuarioDTO usuario = usuarios.stream()
                    .filter(u -> u.getIdUser().equals(integrante.getIdUser()))
                    .findFirst()
                    .orElse(null);

            // Validamos si el usuario existe antes de concatenar su nombre
            String nombreCompleto = (usuario != null)
                    ? usuario.getNombre() + " " + usuario.getApellido()
                    : "Usuario no encontrado o eliminado";

            return new IntegranteDetalleDTO(integrante.getIdIntegrante(), nombreCompleto, equipo.getNombre());

        }).toList();

        return detalles;
    }

    public Optional<Integrante> obtenerIntegrantePorId(Long idIntegrante) {
        return integranteRepository.findById(idIntegrante);
    }

    // Listener actualizado apuntando a la configuración centralizada
    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROYECTO_CREADO)
    public void procesarProyectoCreado(String mensaje) {
        System.out.println("\n---------------------------------------------------------");
        System.out.println(" EQUIPOS: Evento recibido -> " + mensaje);
        System.out.println("Iniciando lógica de asignación o revisión de equipos...");
        System.out.println("---------------------------------------------------------\n");
    }

    // Listener actualizado apuntando a la configuración centralizada
    @RabbitListener(queues = RabbitMQConfig.QUEUE_USUARIO_DESACTIVADO)
    public void removerIntegranteDeEquipo(String mensaje) {
        System.out.println(" EQUIPOS: Removiendo usuario desactivado de los equipos.");
    }
}