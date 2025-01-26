package com.accenture.shopsystem.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PEDIDO_QUEUE = "pedido-queue";
    public static final String ESTOQUE_QUEUE = "estoque-queue";
    public static final String EXCHANGE_NAME = "pedido-exchange";
    public static final String PEDIDO_ROUTING_KEY = "pedido.routingkey";
    public static final String ESTOQUE_ROUTING_KEY = "estoque.routingkey";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    // Fila para pedidos
    @Bean
    public Queue pedidoQueue() {
        return new Queue(PEDIDO_QUEUE, true); // Fila durável
    }

    // Fila para estoque
    @Bean
    public Queue estoqueQueue() {
        return new Queue(ESTOQUE_QUEUE, true); // Fila durável
    }

    // Exchange do tipo Topic
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Binding para a fila de pedidos
    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(pedidoQueue).to(exchange).with(PEDIDO_ROUTING_KEY);
    }

    // Binding para a fila de estoque
    @Bean
    public Binding estoqueBinding(Queue estoqueQueue, TopicExchange exchange) {
        return BindingBuilder.bind(estoqueQueue).to(exchange).with(ESTOQUE_ROUTING_KEY);
    }

    // Conversor de mensagens para JSON
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Template para envio de mensagens
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
