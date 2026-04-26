package com.innovatech.privilegios_service.repository;

import com.innovatech.privilegios_service.model.RolPriv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RolPrivRepository extends JpaRepository<RolPriv, Long> {
    List<RolPriv> findByRol_IdRol(Long idRol);
    List<RolPriv> findByPrivilegio_IdPriv(Long idPriv);
    boolean existsByRol_IdRolAndPrivilegio_IdPriv(Long idRol, Long idPriv);
}
