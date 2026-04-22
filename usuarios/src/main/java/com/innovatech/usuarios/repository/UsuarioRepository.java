package com.innovatech.usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.usuarios.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByRun(String run);
    Usuario findByEmail(String email);
    List<Usuario> findByCargoIdCargo(Long idCargo);
    List<Usuario> findByIdRol(Long idRol);
    List<Usuario> findByIdEstado(Long idEstado);
    boolean existsByRun(String run);
    boolean existsByEmail(String email);
    
}
