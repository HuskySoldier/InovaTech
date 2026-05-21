package com.innovatech.asignaciones;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.innovatech.asignaciones.client.EquipoClient;
import com.innovatech.asignaciones.client.TareaClient;
import com.innovatech.asignaciones.model.Asignacion;
import com.innovatech.asignaciones.repository.AsignacionRepository;
import com.innovatech.asignaciones.service.AsignacionService;


@ExtendWith(MockitoExtension.class)
class AsignacionServiceTest {

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private EquipoClient equipoClient;

    @Mock
    private TareaClient tareaClient;

    @InjectMocks
    private AsignacionService asignacionService;

    private Asignacion asignacionMock;

    @BeforeEach
    void setUp() {
        asignacionMock = new Asignacion();
        asignacionMock.setId_asig(1L);
        asignacionMock.setIdIntegrante(10L);
        asignacionMock.setIdTarea(20L);
    }


    @Test
    void testCrearAsignacionEquipoNoExiste() {
        // Arrange: Simulamos que el cliente falla o devuelve null
        when(equipoClient.obtenerIntegrantePorId(99L)).thenThrow(new RuntimeException("Integrante no encontrado"));

        Asignacion nuevaAsignacion = new Asignacion();
        nuevaAsignacion.setIdIntegrante(99L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            asignacionService.crearAsignacion(null, null);
        });

        verify(asignacionRepository, never()).save(any(Asignacion.class));
    }

    @Test
    void testObtenerTodas() {
        // Arrange
        when(asignacionRepository.findAll()).thenReturn(Arrays.asList(asignacionMock));

        // Act
        List<Asignacion> result = asignacionService.listarTodas();

        // Assert
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getIdIntegrante());
    }
}