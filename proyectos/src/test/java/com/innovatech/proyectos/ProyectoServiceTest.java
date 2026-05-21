package com.innovatech.proyectos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.innovatech.proyectos.client.EstadoClient;
import com.innovatech.proyectos.config.RabbitMQConfig;
import com.innovatech.proyectos.dto.EstadoDTO;
import com.innovatech.proyectos.model.HistorialProyecto;
import com.innovatech.proyectos.model.Proyecto;
import com.innovatech.proyectos.repository.HistorialProyectoRepository;
import com.innovatech.proyectos.repository.ProyectoRepository;
import com.innovatech.proyectos.service.ProyectoService;

@ExtendWith(MockitoExtension.class)
class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private HistorialProyectoRepository historialRepository;

    @Mock
    private EstadoClient estadoClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto proyectoMock;

    @BeforeEach
    void setUp() {
        proyectoMock = new Proyecto();
        proyectoMock.setIdProyecto(1L);
        proyectoMock.setNombre("Migración Cloud");
        proyectoMock.setDescripcion("Migrar infraestructura a AWS");
        proyectoMock.setFechaInicio(LocalDate.now());
        proyectoMock.setIdEstado(1L); // 1 = Activo o Creado
    }

    @Test
    void testObtenerPorId() {
        // Arrange
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyectoMock));

        // Act
        Proyecto resultado = proyectoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Migración Cloud", resultado.getNombre());
        verify(proyectoRepository, times(1)).findById(1L);
    }

    @Test
    void testCrearProyectoExito() {
        // Arrange
        EstadoDTO estadoMock = new EstadoDTO(); // Simulamos la respuesta del MS Estado
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoMock);
        
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyectoMock);

        // Act
        Proyecto resultado = proyectoService.crearProyecto(proyectoMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdProyecto());
        
        // Verificamos que se consultó al MS Estado
        verify(estadoClient, times(1)).obtenerEstadoPorId(1L);
        
        // Verificamos que se guardó el proyecto y su historial
        verify(proyectoRepository, times(1)).save(proyectoMock);
        verify(historialRepository, times(1)).save(any(HistorialProyecto.class));
        
        // Verificamos el envío del mensaje a RabbitMQ
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_PROYECTOS),
                eq(RabbitMQConfig.ROUTING_KEY_PROYECTO_CREADO),
                contains("Nuevo proyecto creado con ID: 1")
        );
    }

    @Test
    void testCrearProyectoEstadoInvalidoLanzaExcepcion() {
        // Arrange
        when(estadoClient.obtenerEstadoPorId(99L)).thenThrow(new RuntimeException("No existe"));
        proyectoMock.setIdEstado(99L);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            proyectoService.crearProyecto(proyectoMock);
        });
        
        assertTrue(exception.getMessage().contains("Estado no encontrado con id: 99"));
        // Verificamos que NO se guarda nada si falla la validación
        verify(proyectoRepository, never()).save(any(Proyecto.class));
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }

    @Test
    void testActualizarProyectoConCambioDeEstado() {
        // Arrange
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyectoMock));
        
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setNombre("Migración Cloud V2");
        proyectoActualizado.setIdEstado(2L); // Cambio de estado a 2
        
        when(estadoClient.obtenerEstadoPorId(2L)).thenReturn(new EstadoDTO());
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyectoMock);

        // Act
        Proyecto resultado = proyectoService.actualizarProyecto(1L, proyectoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Migración Cloud V2", proyectoMock.getNombre()); // Se actualiza la referencia existente
        assertEquals(2L, proyectoMock.getIdEstado());
        
        // Verificamos que se validó el nuevo estado
        verify(estadoClient, times(1)).obtenerEstadoPorId(2L);
        
        // Verificamos que se guardó el historial indicando el cambio de estado
        verify(historialRepository, times(1)).save(argThat(historial -> 
            historial.getDescripcion().contains("Cambio de estado")
        ));
    }

    @Test
    void testEliminarProyecto() {
        // Act
        proyectoService.eliminarProyecto(1L);

        // Assert
        verify(proyectoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testVerHistorial() {
        // Arrange
        HistorialProyecto h1 = new HistorialProyecto();
        h1.setDescripcion("Creación");
        HistorialProyecto h2 = new HistorialProyecto();
        h2.setDescripcion("Actualización");
        
        when(historialRepository.findByProyectoIdProyecto(1L)).thenReturn(Arrays.asList(h1, h2));

        // Act
        List<HistorialProyecto> historial = proyectoService.verHistorial(1L);

        // Assert
        assertEquals(2, historial.size());
        verify(historialRepository, times(1)).findByProyectoIdProyecto(1L);
    }
}