package com.innovatech.Equipos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "integrantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Integrante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_integrante")
    private Long idIntegrante;

    @JsonIgnore // Evita el bucle infinito en el JSON
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @Column(name = "id_user", nullable = false)
    private Long idUser; // FK externa hacia MS Usuario
}