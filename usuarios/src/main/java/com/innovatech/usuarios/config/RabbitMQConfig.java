package com.innovatech.usuarios.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_USUARIOS = "usuarios.exchange";
    public static final String ROUTING_KEY_USUARIO_DESACTIVADO = "usuario.desactivado.routing.key";
    public static final String QUEUE_USUARIO_DESACTIVADO = "usuario.desactivado.queue";

    // Constantes DLQ
    public static final String EXCHANGE_DLX = "usuarios.dlx";
    public static final String QUEUE_DLQ = "usuario.desactivado.dlq";
    public static final String ROUTING_KEY_DLQ = "usuario.desactivado.dead";

    // 1. Cola principal con DLQ configurada
    @Bean
    public Queue usuarioDesactivadoQueue() {
        return QueueBuilder.durable(QUEUE_USUARIO_DESACTIVADO)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_DLQ)
                .build();
    }

    @Bean
    public TopicExchange usuariosExchange() {
        return new TopicExchange(EXCHANGE_USUARIOS);
    }

    // 2. Binding principal
    @Bean
    public Binding bindingUsuarioDesactivado(Queue usuarioDesactivadoQueue, TopicExchange usuariosExchange) {
        return BindingBuilder.bind(usuarioDesactivadoQueue).to(usuariosExchange).with(ROUTING_KEY_USUARIO_DESACTIVADO);
    }

    // 3. Infraestructura DLQ
    @Bean
    public TopicExchange usuariosDlx() {
        return new TopicExchange(EXCHANGE_DLX);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(QUEUE_DLQ).build();
    }

    @Bean
    public Binding bindingDlq(Queue deadLetterQueue, TopicExchange usuariosDlx) {
        return BindingBuilder.bind(deadLetterQueue).to(usuariosDlx).with(ROUTING_KEY_DLQ);
    }
}