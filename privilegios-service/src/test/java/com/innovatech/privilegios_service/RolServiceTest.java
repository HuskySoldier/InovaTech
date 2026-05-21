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

import com.innovatech.privilegios_service.dto.RolDTO;
import com.innovatech.privilegios_service.dto.RolDetalleDTO;
import com.innovatech.privilegios_service.model.Rol;
import com.innovatech.privilegios_service.repository.RolRepository;
import com.innovatech.privilegios_service.service.RolService;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Rol rolMock;

    @BeforeEach
    void setUp() {
        rolMock = new Rol();
        rolMock.setIdRol(1L);
        rolMock.setNombre("ADMIN");
        // Dejamos getRolPriv() como null intencionalmente para probar la validación segura en mapToDetalleDTO
    }

    @Test
    void testGetAllRoles() {
        when(rolRepository.findAll()).thenReturn(Arrays.asList(rolMock));

        List<RolDTO> result = rolService.getAllRoles();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).getNombre());
    }

    @Test
    void testGetRolByIdExito() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolMock));

        RolDTO result = rolService.getRolById(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
    }

    @Test
    void testGetRolByIdNoEncontrado() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> rolService.getRolById(99L));
        assertTrue(exception.getMessage().contains("Rol no encontrado con id: 99"));
    }

    @Test
    void testCrearRolExito() {
        RolDTO dto = new RolDTO();
        dto.setNombre("USER");

        when(rolRepository.existsByNombre("USER")).thenReturn(false);
        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> {
            Rol r = invocation.getArgument(0);
            r.setIdRol(2L);
            return r;
        });

        RolDTO result = rolService.crearRol(dto);

        assertNotNull(result);
        assertEquals(2L, result.getIdRol());
        assertEquals("USER", result.getNombre());
        verify(rolRepository).save(any(Rol.class));
    }

    @Test
    void testCrearRolDuplicadoLanzaExcepcion() {
        RolDTO dto = new RolDTO();
        dto.setNombre("ADMIN");

        when(rolRepository.existsByNombre("ADMIN")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> rolService.crearRol(dto));
        assertTrue(exception.getMessage().contains("Ya existe un rol con el nombre: ADMIN"));
        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void testObtenerDetalleSinPrivilegiosAsignados() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolMock));

        RolDetalleDTO result = rolService.obtenerDetalle(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
        assertTrue(result.getPrivilegios().isEmpty()); // Valida el operador ternario (rol.getRolPriv() == null)
    }

    @Test
    void testEliminarRolExito() {
        when(rolRepository.existsById(1L)).thenReturn(true);

        rolService.eliminarRol(1L);

        verify(rolRepository).deleteById(1L);
    }
}