package com.innovatech.Equipos;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.innovatech.Equipos.model.Equipo;
import com.innovatech.Equipos.repository.EquipoRepository;
import com.innovatech.Equipos.repository.IntegranteRepository;
import com.innovatech.Equipos.service.EquipoService;
// Importa tus clients
import com.innovatech.Equipos.client.ProyectoClient;
import com.innovatech.Equipos.client.UsuarioClient;

@ExtendWith(MockitoExtension.class)
public class EquipoServiceTest {

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private IntegranteRepository integranteRepository;

    // Estos son los que faltaban
    @Mock
    private ProyectoClient proyectoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private EquipoService equipoService;

    @Test
    void testObtenerPorId_Encontrado() {
        Equipo equipo = new Equipo();
        equipo.setIdEquipo(1L);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        Equipo resultado = equipoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdEquipo());
        verify(equipoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.empty());

        Equipo resultado = equipoService.obtenerPorId(1L);

        assertNull(resultado);
        verify(equipoRepository, times(1)).findById(1L);
    }
}