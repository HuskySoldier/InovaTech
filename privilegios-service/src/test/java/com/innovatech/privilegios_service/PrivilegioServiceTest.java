package com.innovatech.privilegios_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

import com.innovatech.privilegios_service.dto.PrivilegioDTO;
import com.innovatech.privilegios_service.model.Privilegio;
import com.innovatech.privilegios_service.repository.PrivilegioRepository;
import com.innovatech.privilegios_service.service.PrivilegioService;

@ExtendWith(MockitoExtension.class)
class PrivilegioServiceTest {

    @Mock
    private PrivilegioRepository privRepo;

    @InjectMocks
    private PrivilegioService privilegioService;

    private Privilegio privilegioMock;

    @BeforeEach
    void setUp() {
        privilegioMock = new Privilegio();
        privilegioMock.setIdPriv(1L);
        privilegioMock.setNombre("LEER_PROYECTOS");
        privilegioMock.setDescripcion("Permite ver la lista de proyectos");
    }

    @Test
    void testObtenerTodos() {
        when(privRepo.findAll()).thenReturn(Arrays.asList(privilegioMock));

        List<PrivilegioDTO> result = privilegioService.obtenerTodos();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("LEER_PROYECTOS", result.get(0).getNombre());
    }

    @Test
    void testObtenerPrivExito() {
        when(privRepo.findById(1L)).thenReturn(Optional.of(privilegioMock));

        PrivilegioDTO result = privilegioService.obtenerPriv(1L);

        assertNotNull(result);
        assertEquals("Permite ver la lista de proyectos", result.getDescripcion());
    }

    @Test
    void testCrearPrivExito() {
        PrivilegioDTO dto = new PrivilegioDTO();
        dto.setNombre("EDITAR_TAREAS");
        dto.setDescripcion("Permite modificar tareas");

        when(privRepo.existsByNombre(dto.getNombre())).thenReturn(false);
        when(privRepo.save(any(Privilegio.class))).thenReturn(privilegioMock); // Reutilizamos el mock para simular guardado

        PrivilegioDTO result = privilegioService.crearPriv(dto);

        assertNotNull(result);
        verify(privRepo).save(any(Privilegio.class));
    }

    @Test
    void testCrearPrivDuplicadoLanzaExcepcion() {
        PrivilegioDTO dto = new PrivilegioDTO();
        dto.setNombre("LEER_PROYECTOS");

        when(privRepo.existsByNombre("LEER_PROYECTOS")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> privilegioService.crearPriv(dto));
        assertTrue(exception.getMessage().contains("Ya existe un privilegio con el nombre"));
        verify(privRepo, never()).save(any(Privilegio.class));
    }

    @Test
    void testActualizarPrivExito() {
        when(privRepo.findById(1L)).thenReturn(Optional.of(privilegioMock));
        
        PrivilegioDTO dtoActualizar = new PrivilegioDTO();
        dtoActualizar.setNombre("LEER_PROYECTOS_V2");
        dtoActualizar.setDescripcion("Nueva descripcion");
        
        when(privRepo.save(any(Privilegio.class))).thenReturn(privilegioMock);

        PrivilegioDTO result = privilegioService.actualizarPriv(1L, dtoActualizar);

        assertNotNull(result);
        assertEquals("LEER_PROYECTOS_V2", privilegioMock.getNombre()); // Se modificó la entidad mockeada
        verify(privRepo).save(privilegioMock);
    }
}