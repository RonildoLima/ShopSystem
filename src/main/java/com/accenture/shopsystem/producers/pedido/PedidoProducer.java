package com.accenture.shopsystem.producers.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.pedido}")
    private String pedidoQueue;

    public void enviarPedido(Pedido pedido) {
        // Envia o pedido para a fila do RabbitMQ
        rabbitTemplate.convertAndSend(pedidoQueue, pedido);
        System.out.println("Pedido enviado para a fila: " + pedidoQueue + " - Pedido ID: " + pedido.getId());
    }
}


