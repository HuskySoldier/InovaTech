package com.innovatech.asignaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.asignaciones.model.Asignacion;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByIdIntegrante(Long idIntegrante);
}