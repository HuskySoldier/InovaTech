package com.innovatech.proyectos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_PROYECTOS = "proyectos.exchange";
    public static final String ROUTING_KEY_PROYECTO_CREADO = "proyecto.creado.routing.key";
    public static final String QUEUE_PROYECTO_CREADO = "proyecto.creado.queue";

    // Define la cola
    @Bean
    public Queue proyectoCreadoQueue() {
        return new Queue(QUEUE_PROYECTO_CREADO, true);
    }

    // Define el Exchange
    @Bean
    public TopicExchange proyectosExchange() {
        return new TopicExchange(EXCHANGE_PROYECTOS);
    }

    // Vincula la cola con el Exchange
    @Bean
    public Binding bindingProyectoCreado(Queue proyectoCreadoQueue, TopicExchange proyectosExchange) {
        return BindingBuilder.bind(proyectoCreadoQueue).to(proyectosExchange).with(ROUTING_KEY_PROYECTO_CREADO);
    }
}