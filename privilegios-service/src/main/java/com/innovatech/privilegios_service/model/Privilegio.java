package com.innovatech.privilegios_service.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "privilegios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Privilegio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPriv;

    @Column(nullable = false)
    private String nombre;
    
    @Column
    private String descripcion;

    @OneToMany(mappedBy = "privilegio")
    private List<RolPriv> rolPriv;
}
