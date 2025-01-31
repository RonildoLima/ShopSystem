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
    public static final String STATUS_QUEUE = "status-queue";
    public static final String EMAIL_QUEUE = "email-queue"; // Nova fila de e-mails
    public static final String EXCHANGE_NAME = "pedido-exchange";
    public static final String EXCHANGE_PEDIDOS = "pedidos";
    public static final String PEDIDO_ROUTING_KEY = "pedido.routingkey";
    public static final String ESTOQUE_ROUTING_KEY = "estoque.routingkey";
    public static final String STATUS_ROUTING_KEY = "status.routingkey";
    public static final String EMAIL_ROUTING_KEY = "email.routingkey"; // Nova routing key

    @Bean
    public Queue pedidoQueue() {
        return new Queue(PEDIDO_QUEUE, true);
    }

    @Bean
    public Queue estoqueQueue() {
        return new Queue(ESTOQUE_QUEUE, true);
    }

    @Bean
    public Queue statusQueue() {
        return new Queue(STATUS_QUEUE, true);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true); // Fila durável para envio de e-mails
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding pedidoBinding(Queue pedidoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(pedidoQueue).to(exchange).with(PEDIDO_ROUTING_KEY);
    }

    @Bean
    public Binding estoqueBinding(Queue estoqueQueue, TopicExchange exchange) {
        return BindingBuilder.bind(estoqueQueue).to(exchange).with(ESTOQUE_ROUTING_KEY);
    }

    @Bean
    public Binding statusBinding(Queue statusQueue, TopicExchange exchange) {
        return BindingBuilder.bind(statusQueue).to(exchange).with(STATUS_ROUTING_KEY);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
