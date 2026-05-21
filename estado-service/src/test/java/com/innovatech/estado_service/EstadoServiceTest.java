package com.innovatech.estado_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.innovatech.estado_service.dto.EstadoDTO;
import com.innovatech.estado_service.model.Estado;
import com.innovatech.estado_service.model.Tipo;
import com.innovatech.estado_service.repository.EstadoRepository;
import com.innovatech.estado_service.service.EstadoService;

@ExtendWith(MockitoExtension.class)
class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    private Estado estadoMock;

    @BeforeEach
    void setUp() {
        Tipo tipoMock = new Tipo();
        tipoMock.setIdTipo(1L);
        tipoMock.setNombre("PROYECTO");

        estadoMock = new Estado();
        estadoMock.setIdEstado(1L);
        estadoMock.setNombre("ACTIVO");
        estadoMock.setTipo(tipoMock);
    }

    @Test
    void testObtenerTodos() {
        // Arrange
        when(estadoRepository.findAll()).thenReturn(Arrays.asList(estadoMock));

        // Act
        // Nota: Ajusta el nombre del método según cómo lo hayas nombrado en EstadoService (ej. getAllEstados)
        List<EstadoDTO> result = estadoService.getAllEstados(); 

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("ACTIVO", result.get(0).getNombre());
    }

    @Test
    void testObtenerEstadoPorIdExito() {
        // Arrange
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoMock));

        // Act
        EstadoDTO result = estadoService.getEstadoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("ACTIVO", result.getNombre());
    }

    @Test
    void testObtenerEstadoPorIdNoEncontrado() {
        // Arrange
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> estadoService.getEstadoById(99L));
        assertTrue(exception.getMessage().contains("no encontrado"));
    }
    
    // Si tu EstadoService tiene métodos para crear, actualizar o eliminar (aunque a veces los estados son estáticos), 
    // puedes agregar pruebas similares a las de RolServiceTest aquí usando verify(estadoRepository).save(...)
}