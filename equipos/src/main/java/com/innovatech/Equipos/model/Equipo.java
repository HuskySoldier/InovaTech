package com.innovatech.Equipos.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "equipos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long idEquipo;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto; 

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Integrante> integrantes = new ArrayList<>();

    // --- MÉTODO BLINDADO ---
    // Fuerza a que la lista jamás sea null, devolviendo una lista vacía en su lugar
    public List<Integrante> getIntegrantes() {
        if (this.integrantes == null) {
            this.integrantes = new ArrayList<>();
        }
        return this.integrantes;
    }
}