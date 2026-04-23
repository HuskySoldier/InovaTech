package com.innovatech.Equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.Equipos.model.Integrante;
import java.util.List;

@Repository
public interface IntegranteRepository extends JpaRepository<Integrante, Long> {
    List<Integrante> findByIdUser(Long idUser);
}