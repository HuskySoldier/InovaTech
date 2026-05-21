package com.innovatech.Equipos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Colas principales
    public static final String QUEUE_PROYECTO_CREADO = "proyecto.creado.queue";
    public static final String QUEUE_USUARIO_DESACTIVADO = "usuario.desactivado.queue.equipos";
    
    // DLQ
    public static final String DLX_EXCHANGE = "equipos.dlx";
    public static final String DLQ_PROYECTO = "proyecto.creado.dlq.equipos";
    public static final String DLQ_USUARIO = "usuario.desactivado.dlq.equipos";

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(DLX_EXCHANGE);
    }

    // --- COLA PROYECTO CREADO ---
    @Bean
    public Queue proyectoCreadoQueue() {
        return QueueBuilder.durable(QUEUE_PROYECTO_CREADO)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "proyecto.dead")
                .build();
    }

    @Bean
    public Queue dlqProyecto() {
        return QueueBuilder.durable(DLQ_PROYECTO).build();
    }

    @Bean
    public Binding bindingDlqProyecto() {
        return BindingBuilder.bind(dlqProyecto()).to(dlxExchange()).with("proyecto.dead");
    }

    // --- COLA USUARIO DESACTIVADO ---
    @Bean
    public Queue usuarioDesactivadoQueue() {
        return QueueBuilder.durable(QUEUE_USUARIO_DESACTIVADO)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "usuario.dead")
                .build();
    }

    @Bean
    public Queue dlqUsuario() {
        return QueueBuilder.durable(DLQ_USUARIO).build();
    }

    @Bean
    public Binding bindingDlqUsuario() {
        return BindingBuilder.bind(dlqUsuario()).to(dlxExchange()).with("usuario.dead");
    }
}