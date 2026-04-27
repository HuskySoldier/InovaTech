package com.innovatech.estado_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovatech.estado_service.model.Tipo;

public interface TipoRepository extends JpaRepository<Tipo, Long> {
    boolean existsByNombre(String nombre);
}
