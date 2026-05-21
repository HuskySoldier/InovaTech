package com.innovatech.proyectos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_PROYECTOS = "proyectos.exchange";
    public static final String ROUTING_KEY_PROYECTO_CREADO = "proyecto.creado.routing.key";
    public static final String QUEUE_PROYECTO_CREADO = "proyecto.creado.queue";

    // Constantes para el manejo de Dead Letter
    public static final String EXCHANGE_DLX = "proyectos.dlx";
    public static final String QUEUE_DLQ = "proyecto.creado.dlq";
    public static final String ROUTING_KEY_DLQ = "proyecto.creado.dead";

    // 1. Define la cola principal con argumentos para Dead Letter
    @Bean
    public Queue proyectoCreadoQueue() {
        return QueueBuilder.durable(QUEUE_PROYECTO_CREADO)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_DLQ)
                .build();
    }

    // 2. Define el Exchange principal
    @Bean
    public TopicExchange proyectosExchange() {
        return new TopicExchange(EXCHANGE_PROYECTOS);
    }

    // 3. Vincula la cola con el Exchange principal
    @Bean
    public Binding bindingProyectoCreado(Queue proyectoCreadoQueue, TopicExchange proyectosExchange) {
        return BindingBuilder.bind(proyectoCreadoQueue).to(proyectosExchange).with(ROUTING_KEY_PROYECTO_CREADO);
    }

    // --- INFRAESTRUCTURA DE DEAD LETTER ---

    // 4. Define el Exchange de errores (Dead Letter Exchange)
    @Bean
    public TopicExchange proyectosDlx() {
        return new TopicExchange(EXCHANGE_DLX);
    }

    // 5. Define la cola de errores (Dead Letter Queue)
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(QUEUE_DLQ).build();
    }

    // 6. Vincula la DLQ con el DLX
    @Bean
    public Binding bindingDlq(Queue deadLetterQueue, TopicExchange proyectosDlx) {
        return BindingBuilder.bind(deadLetterQueue).to(proyectosDlx).with(ROUTING_KEY_DLQ);
    }
}