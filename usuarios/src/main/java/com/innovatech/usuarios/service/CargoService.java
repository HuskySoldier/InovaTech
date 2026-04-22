package com.innovatech.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovatech.usuarios.model.Cargo;
import com.innovatech.usuarios.repository.CargoRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CargoService {
    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> listarCargos() {
        return cargoRepository.findAll();
    }

    public Cargo obtenerCargoPorId(Long id) {
        return cargoRepository.findById(id).orElse(null);
    }

    public Cargo crearCargo(Cargo cargo) {
        if (cargoRepository.existsByNombreCargo(cargo.getNombreCargo())) {
            throw new RuntimeException("Ya existe un cargo con el nombre: " + cargo.getNombreCargo());
        }
        return cargoRepository.save(cargo);
    }

    public Cargo actualizarCargo(Long id, Cargo cargoActualizado) {
        Cargo cargoExistente = cargoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado con id: " + id));
        if (cargoExistente != null) {
            cargoExistente.setNombreCargo(cargoActualizado.getNombreCargo());
            return cargoRepository.save(cargoExistente);
        }
        return null;
    }

    public boolean eliminarCargo(Long id) {
        if (cargoRepository.existsById(id)) {
            cargoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
