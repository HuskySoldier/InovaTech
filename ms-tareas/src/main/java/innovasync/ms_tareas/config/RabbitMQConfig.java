package innovasync.ms_tareas.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_TAREAS = "tareas.exchange";
    public static final String ROUTING_KEY_TAREA_CREADA = "tarea.creada.routing.key";
    public static final String QUEUE_TAREA_CREADA = "tarea.creada.queue";

    // --- Constantes para Dead Letter (Mensajes Fallidos) ---
    public static final String DLX_TAREAS = "tareas.dlx";
    public static final String DLQ_TAREA_CREADA = "tarea.creada.dlq";

    // 1. Define la cola principal (con argumentos para DLX)
    @Bean
    public Queue tareaCreadaQueue() {
        return QueueBuilder.durable(QUEUE_TAREA_CREADA)
                .withArgument("x-dead-letter-exchange", DLX_TAREAS) // Redirige a este exchange si falla
                .withArgument("x-dead-letter-routing-key", QUEUE_TAREA_CREADA + ".dead") // Con esta clave
                .build();
    }

    // 2. Define el Exchange principal
    @Bean
    public TopicExchange tareasExchange() {
        return new TopicExchange(EXCHANGE_TAREAS);
    }

    // 3. Vincula la cola principal con el Exchange principal
    @Bean
    public Binding bindingTareaCreada(Queue tareaCreadaQueue, TopicExchange tareasExchange) {
        return BindingBuilder.bind(tareaCreadaQueue).to(tareasExchange).with(ROUTING_KEY_TAREA_CREADA);
    }

    // --- Configuración de Dead Letter ---

    // 4. Cola de mensajes muertos
    @Bean
    public Queue tareaCreadaDlq() {
        return QueueBuilder.durable(DLQ_TAREA_CREADA).build();
    }

    // 5. Exchange de mensajes muertos
    @Bean
    public TopicExchange tareasDlx() {
        return new TopicExchange(DLX_TAREAS);
    }

    // 6. Vincula la DLQ con el DLX
    @Bean
    public Binding bindingTareaCreadaDlq(Queue tareaCreadaDlq, TopicExchange tareasDlx) {
        return BindingBuilder.bind(tareaCreadaDlq).to(tareasDlx).with(QUEUE_TAREA_CREADA + ".dead");
    }
}