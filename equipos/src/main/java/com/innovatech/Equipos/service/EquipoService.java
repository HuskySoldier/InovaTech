package com.innovatech.Equipos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return equipoRepository.save(equipo);
    }

    public Equipo agregarIntegrante(Long idEquipo, Long idUser) {
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        
        Integrante nuevoIntegrante = new Integrante();
        nuevoIntegrante.setEquipo(equipo);
        nuevoIntegrante.setIdUser(idUser);
        
        // 1. Guarda el integrante en la base de datos
        integranteRepository.save(nuevoIntegrante);

        // 2. Lo agrega a la lista en memoria para que el JSON lo muestre al instante
        equipo.getIntegrantes().add(nuevoIntegrante);
        
        return equipo;
    }

    public void eliminarEquipo(Long id) {
        equipoRepository.deleteById(id);
    }
}