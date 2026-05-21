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

import com.innovatech.estado_service.dto.TipoDTO;
import com.innovatech.estado_service.model.Tipo;
import com.innovatech.estado_service.repository.TipoRepository;
import com.innovatech.estado_service.service.TipoService;

@ExtendWith(MockitoExtension.class)
class TipoServiceTest {

    @Mock
    private TipoRepository tipoRepository;

    @InjectMocks
    private TipoService tipoService;

    private Tipo tipoMock;

    @BeforeEach
    void setUp() {
        tipoMock = new Tipo();
        tipoMock.setIdTipo(1L);
        tipoMock.setNombre("USUARIO");
    }

    @Test
    void testObtenerTodosLosTipos() {
        when(tipoRepository.findAll()).thenReturn(Arrays.asList(tipoMock));

        List<TipoDTO> result = tipoService.getAllTipos(); 

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("USUARIO", result.get(0).getNombre());
    }

    @Test
    void testObtenerTipoPorId() {
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoMock));

        TipoDTO result = tipoService.getTipoById(1L); 

        assertNotNull(result);
        assertEquals("USUARIO", result.getNombre());
    }
}