package com.innovatech.usuarios.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_USUARIOS = "usuarios.exchange";
    public static final String ROUTING_KEY_USUARIO_DESACTIVADO = "usuario.desactivado.routing.key";

    @Bean
    public TopicExchange usuariosExchange() {
        return new TopicExchange(EXCHANGE_USUARIOS);
    }
}