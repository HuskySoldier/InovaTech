package com.innovatech.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.usuarios.model.Cargo;
import com.innovatech.usuarios.service.CargoService;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {
    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<Cargo>> listarCargos() {
        return ResponseEntity.ok(cargoService.listarCargos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> obtenerCargoPorId(@PathVariable Long id) {
        Cargo cargo = cargoService.obtenerCargoPorId(id);
        if (cargo != null) {
            return ResponseEntity.ok(cargo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cargo> crearCargo(@RequestBody Cargo cargo) {
        try {
            Cargo nuevoCargo = cargoService.crearCargo(cargo);
            return ResponseEntity.created(null).body(nuevoCargo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> actualizarCargo(@PathVariable Long id, @RequestBody Cargo cargo) {
        try {
            Cargo cargoActualizado = cargoService.actualizarCargo(id, cargo);
            return ResponseEntity.ok(cargoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCargo(@PathVariable Long id) {
        try {
            cargoService.eliminarCargo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
