package com.innovatech.estado_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovatech.estado_service.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long>{
    List<Estado> findByTipo_IdTipo(Long tipoId);
    boolean existsByNombre(String nombre);
}
