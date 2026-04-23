package com.innovatech.proyectos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.proyectos.model.HistorialProyecto;
import java.util.List;

@Repository
public interface HistorialProyectoRepository extends JpaRepository<HistorialProyecto, Long> {
    List<HistorialProyecto> findByProyectoIdProyecto(Long idProyecto);
}