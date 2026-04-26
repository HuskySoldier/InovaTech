package com.innovatech.privilegios_service.repository;

import com.innovatech.privilegios_service.model.Privilegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PrivilegioRepository extends JpaRepository<Privilegio, Long> {
    Optional<Privilegio> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
