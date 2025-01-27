package com.accenture.shopsystem.services.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.pedido}")
    private String pedidoQueue;

    private final PedidoRepository pedidoRepository;
    private final PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    public PedidoService(PedidoRepository pedidoRepository, PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoHistoricoStatusRepository = pedidoHistoricoStatusRepository;
    }
    @Transactional
    public void deletarPedido(String pedidoId, String vendedorId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

        if (!pedido.getVendedor().getId().equals(vendedorId)) {
            throw new RuntimeException("Você não tem permissão para deletar este pedido.");
        }

        pedidoHistoricoStatusRepository.deleteByPedido(pedido);
        pedidoRepository.delete(pedido);
    }

    public void enviarPedido(Pedido pedido) {
        // Envia o pedido para a fila do RabbitMQ
        rabbitTemplate.convertAndSend(pedidoQueue, pedido);
        System.out.println("Pedido enviado para a fila: " + pedidoQueue + " - Pedido ID: " + pedido.getId());
    }
}
