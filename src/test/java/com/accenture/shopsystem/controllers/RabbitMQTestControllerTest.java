package com.accenture.shopsystem.controllers;

import com.accenture.shopsystem.controllers.RabbitMQTestController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitMQTestControllerTest {

    @Test
    void testRabbit() {
        RabbitTemplate rabbitTemplate = Mockito.mock(RabbitTemplate.class);

        RabbitMQTestController controller = new RabbitMQTestController(rabbitTemplate);

        String response = controller.testRabbit();

        assertEquals("Mensagem enviada para a fila 'teste' no RabbitMQ com sucesso.", response);

        verify(rabbitTemplate, times(1)).convertAndSend("", "teste", "Test Message");
    }

    @Test
    void testRabbit_WithException() {
        RabbitTemplate rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        doThrow(new RuntimeException("RabbitMQ connection failed")).when(rabbitTemplate).convertAndSend("", "teste", "Test Message");

        RabbitMQTestController controller = new RabbitMQTestController(rabbitTemplate);

        String response = controller.testRabbit();

        assertTrue(response.contains("Erro ao conectar ao RabbitMQ"));
        assertTrue(response.contains("RabbitMQ connection failed"));

        verify(rabbitTemplate, times(1)).convertAndSend("", "teste", "Test Message");
    }
}
