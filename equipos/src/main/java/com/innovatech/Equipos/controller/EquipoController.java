package com.innovatech.Equipos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.innovatech.Equipos.model.Equipo;
import com.innovatech.Equipos.repository.IntegranteRepository;
import com.innovatech.Equipos.service.EquipoService;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private IntegranteRepository integranteRepository;

    @GetMapping
    public ResponseEntity<List<Equipo>> listarEquipos() {
        return ResponseEntity.ok(equipoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> obtenerEquipo(@PathVariable Long id) {
        Equipo equipo = equipoService.obtenerPorId(id);
        return equipo != null ? ResponseEntity.ok(equipo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<List<Equipo>> listarPorProyecto(@PathVariable Long idProyecto) {
        return ResponseEntity.ok(equipoService.listarPorProyecto(idProyecto));
    }

    @PostMapping
    public ResponseEntity<Equipo> crearEquipo(@RequestBody Equipo equipo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.crearEquipo(equipo));
    }

    // Endpoint especial para agregar un integrante a un equipo
    @PostMapping("/{idEquipo}/integrantes/{idUser}")
    public ResponseEntity<Equipo> agregarIntegrante(@PathVariable Long idEquipo, @PathVariable Long idUser) {
        try {
            return ResponseEntity.ok(equipoService.agregarIntegrante(idEquipo, idUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return ResponseEntity.noContent().build();
    }

    // Agrega este endpoint para que otros microservicios puedan validar integrantes
    @GetMapping("/integrante/{idIntegrante}")
    public ResponseEntity<?> obtenerIntegrante(@PathVariable Long idIntegrante) {
        return integranteRepository.findById(idIntegrante)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}