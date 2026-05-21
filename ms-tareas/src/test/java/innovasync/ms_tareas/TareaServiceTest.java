package innovasync.ms_tareas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import innovasync.ms_tareas.client.EstadoClient;
import innovasync.ms_tareas.config.RabbitMQConfig;
import innovasync.ms_tareas.dto.EstadoResponse;
import innovasync.ms_tareas.dto.TareaDTO;
import innovasync.ms_tareas.dto.TareaResponseDTO;
import innovasync.ms_tareas.model.Tarea;
import innovasync.ms_tareas.repository.TareaRepository;
import innovasync.ms_tareas.service.TareaService;

@ExtendWith(MockitoExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private EstadoClient estadoClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TareaService tareaService;

    private Tarea tareaMock;

    @BeforeEach
    void setUp() {
        tareaMock = new Tarea();
        tareaMock.setId(1L);
        tareaMock.setNombre("Implementar Login");
        tareaMock.setDescripcion("Desarrollar el backend para inicio de sesión");
        tareaMock.setIdEstado(1L);
        tareaMock.setIdPrioridad(2L);
    }

    @Test
    void testObtenerPorIdExito() {
        // Arrange
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaMock));
        
        EstadoResponse estadoResponse = new EstadoResponse();
        estadoResponse.setNombre("Pendiente");
        when(estadoClient.obtenerEstado(1L)).thenReturn(estadoResponse);

        // Act
        TareaResponseDTO result = tareaService.obtenerPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Implementar Login", result.getNombre());
        assertEquals("Pendiente", result.getNombreEstado());
        verify(tareaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorIdEstadoCaido() {
        // Arrange
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaMock));
        
        // Simulamos que el microservicio de estados falla
        when(estadoClient.obtenerEstado(1L)).thenThrow(new RuntimeException("MS Caído"));

        // Act
        TareaResponseDTO result = tareaService.obtenerPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Sin estado", result.getNombreEstado()); // Según la lógica del catch en tu servicio
    }

    @Test
    void testCrearTareaYEnviarEventoRabbitMQ() {
        // Arrange
        TareaDTO dto = new TareaDTO();
        dto.setNombre("Nueva Tarea");
        dto.setDescripcion("Descripción nueva");
        dto.setIdEstado(1L);
        dto.setIdPrioridad(3L);

        Tarea tareaGuardadaMock = new Tarea();
        tareaGuardadaMock.setId(10L); // Simulamos que la DB le asignó el ID 10
        tareaGuardadaMock.setNombre("Nueva Tarea");
        tareaGuardadaMock.setIdEstado(1L);

        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaGuardadaMock);
        
        EstadoResponse estadoResponse = new EstadoResponse();
        estadoResponse.setNombre("En Progreso");
        when(estadoClient.obtenerEstado(1L)).thenReturn(estadoResponse);

        // Act
        TareaResponseDTO result = tareaService.crear(dto);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Nueva Tarea", result.getNombre());
        
        verify(tareaRepository).save(any(Tarea.class));
        
        // Verificar que el evento de RabbitMQ se envió correctamente
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_TAREAS), 
                eq(RabbitMQConfig.ROUTING_KEY_TAREA_CREADA), 
                contains("Nueva tarea creada con ID: 10")
        );
    }

    @Test
    void testEliminarTarea() {
        // Act
        tareaService.eliminar(1L);

        // Assert
        verify(tareaRepository, times(1)).deleteById(1L);
    }
}