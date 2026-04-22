package com.innovatech.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.usuarios.model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long>{
    boolean existsByNombreCargo(String nombreCargo);
}
