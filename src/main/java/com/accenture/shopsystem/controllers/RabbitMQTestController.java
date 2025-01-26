package com.accenture.shopsystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Teste de envio de mensagem para o RabbitMQ")
public class RabbitMQTestController {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQTestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/test-rabbit")
    @Operation(summary = "Envio de mensagem", description = "Método para testar o envio de mensagem para o Rabbit")
    public String testRabbit() {
        try {
            // Envia diretamente para a fila "teste" utilizando o default exchange
            rabbitTemplate.convertAndSend("", "teste", "Test Message");
            return "Mensagem enviada para a fila 'teste' no RabbitMQ com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao conectar ao RabbitMQ: " + e.getMessage();
        }
    }
}

