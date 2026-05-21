package com.innovatech.proyectos;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import com.innovatech.proyectos.client.EstadoClient;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProyectosApplicationTests {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EstadoClient estadoClient;

    @Test
    void contextLoads() {
        // La prueba pasará exitosamente al cargar todos los beans sin conectarse a servicios reales
    }
}