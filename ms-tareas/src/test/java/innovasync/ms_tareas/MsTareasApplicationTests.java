package innovasync.ms_tareas;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import innovasync.ms_tareas.client.EstadoClient;

// Sobrescribimos las propiedades de la BD solo para este test
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MsTareasApplicationTests {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EstadoClient estadoClient;

    @Test
    void contextLoads() {
        // La prueba pasará exitosamente al conectar con H2 en memoria
    }
}