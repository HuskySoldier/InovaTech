package com.innovatech.Equipos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // --- COLAS PRINCIPALES ---
    public static final String QUEUE_PROYECTO_CREADO = "proyecto.creado.queue";
    public static final String QUEUE_USUARIO_DESACTIVADO = "usuario.desactivado.queue.equipos";
    
    // --- CONSTANTES DLQ (Errores) ---
    // Usamos el DLX de Equipos para mantener los errores de este microservicio separados
    public static final String DLX_EXCHANGE_EQUIPOS = "equipos.dlx"; 
    public static final String DLQ_PROYECTO = "proyecto.creado.dlq.equipos";
    public static final String DLQ_USUARIO = "usuario.desactivado.dlq.equipos";

    // 1. Declarar el Exchange de errores local (Equipos)
    @Bean
    public TopicExchange dlxExchangeEquipos() {
        return new TopicExchange(DLX_EXCHANGE_EQUIPOS);
    }

    // ==========================================
    // 1. CONFIGURACIÓN: PROYECTO CREADO
    // ==========================================

    @Bean
    public Queue proyectoCreadoQueue() {
        // Esta configuración es exacta a la del MS Proyectos para evitar conflictos
        return QueueBuilder.durable(QUEUE_PROYECTO_CREADO)
                .withArgument("x-dead-letter-exchange", "proyectos.dlx") 
                .withArgument("x-dead-letter-routing-key", "proyecto.creado.dead") 
                .build();
    }

    @Bean
    public Queue dlqProyecto() {
        return QueueBuilder.durable(DLQ_PROYECTO).build();
    }

    @Bean
    public Binding bindingDlqProyecto() {
        // Enlaza la cola de errores local con el DLX de proyectos
        return BindingBuilder.bind(dlqProyecto()).to(new TopicExchange("proyectos.dlx")).with("proyecto.creado.dead");
    }

    // ==========================================
    // 2. CONFIGURACIÓN: USUARIO DESACTIVADO 
    // ==========================================

    @Bean
    public Queue usuarioDesactivadoQueue() {
        return QueueBuilder.durable(QUEUE_USUARIO_DESACTIVADO)
                // Usamos el exchange de errores de Equipos para los fallos locales
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE_EQUIPOS)
                .withArgument("x-dead-letter-routing-key", "usuario.desactivado.dead")
                .build();
    }

    @Bean
    public Queue dlqUsuario() {
        return QueueBuilder.durable(DLQ_USUARIO).build();
    }

    @Bean
    public Binding bindingDlqUsuario() {
        return BindingBuilder.bind(dlqUsuario()).to(dlxExchangeEquipos()).with("usuario.desactivado.dead");
    }

    // --- SOLUCIÓN AL CONFLICTO 3: ENLACE (BINDING) CON EL MS USUARIOS ---
    
    // Referencia al exchange principal del Microservicio Usuarios
    @Bean
    public TopicExchange usuariosExchange() {
        return new TopicExchange("usuarios.exchange");
    }

    // Conecta la cola de Equipos al Exchange de Usuarios para recibir los mensajes
    @Bean
    public Binding bindingUsuarioDesactivadoEquipos() {
        return BindingBuilder.bind(usuarioDesactivadoQueue())
                .to(usuariosExchange())
                .with("usuario.desactivado.routing.key");
    }
}