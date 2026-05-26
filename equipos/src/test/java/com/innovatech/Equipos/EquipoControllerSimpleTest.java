package com.innovatech.Equipos;

import com.innovatech.Equipos.controller.EquipoController;
import com.innovatech.Equipos.model.Equipo;
import com.innovatech.Equipos.service.EquipoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EquipoControllerSimpleTest {

    @Test
    void testListarEquipos_RetornaLista() {
        // 1. Crear el mock manualmente
        EquipoService service = Mockito.mock(EquipoService.class);
        
        // 2. Configurar comportamiento
        List<Equipo> lista = new ArrayList<>();
        lista.add(new Equipo()); // Simulamos un equipo
        when(service.listarTodos()).thenReturn(lista);

        // 3. Instanciar el controlador manualmente
        EquipoController controller = new EquipoController(service);

        // 4. Ejecutar el método
        ResponseEntity<List<Equipo>> response = controller.listarEquipos();

        // 5. Verificar
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}