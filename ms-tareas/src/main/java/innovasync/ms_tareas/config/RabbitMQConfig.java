package innovasync.ms_tareas.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_TAREAS = "tareas.exchange";
    public static final String ROUTING_KEY_TAREA_CREADA = "tarea.creada.routing.key";
    public static final String QUEUE_TAREA_CREADA = "tarea.creada.queue";

    // Define la cola
    @Bean
    public Queue tareaCreadaQueue() {
        return new Queue(QUEUE_TAREA_CREADA, true); // true = durable
    }

    // Define el Exchange
    @Bean
    public TopicExchange tareasExchange() {
        return new TopicExchange(EXCHANGE_TAREAS);
    }

    // Vincula la cola con el Exchange mediante el Routing Key
    @Bean
    public Binding bindingTareaCreada(Queue tareaCreadaQueue, TopicExchange tareasExchange) {
        return BindingBuilder.bind(tareaCreadaQueue).to(tareasExchange).with(ROUTING_KEY_TAREA_CREADA);
    }
}