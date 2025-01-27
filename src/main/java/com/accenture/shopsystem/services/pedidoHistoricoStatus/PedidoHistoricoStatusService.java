package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PedidoHistoricoStatusService {

    private final PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;
    private final PedidoRepository pedidoRepository;
    private final RabbitTemplate rabbitTemplate;

    public PedidoHistoricoStatusService(PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository,
                                        PedidoRepository pedidoRepository,
                                        RabbitTemplate rabbitTemplate) {
        this.pedidoHistoricoStatusRepository = pedidoHistoricoStatusRepository;
        this.pedidoRepository = pedidoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarParaFila(String pedidoId, String vendedorId) {
        PedidoHistoricoStatus historico = pedidoHistoricoStatusRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Histórico de pedido não encontrado para o ID: " + pedidoId));

        if (!historico.getPedido().getVendedor().getId().equals(vendedorId)) {
            throw new IllegalArgumentException("Vendedor não autorizado a atualizar este pedido.");
        }

        rabbitTemplate.convertAndSend("status-queue", historico);
    }


    public PedidoHistoricoStatus salvarHistorico(PedidoHistoricoStatus pedidoHistoricoStatus) {
        Optional<Pedido> pedido = pedidoRepository.findById(pedidoHistoricoStatus.getPedido().getId());
        if (pedido.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado: " + pedidoHistoricoStatus.getPedido().getId());
        }

        if (pedidoHistoricoStatus.getDataHoraStatusPedido() == null) {
            pedidoHistoricoStatus.setDataHoraStatusPedido(LocalDateTime.now());
        }

        return pedidoHistoricoStatusRepository.save(pedidoHistoricoStatus);
    }

    public Iterable<PedidoHistoricoStatus> listarHistoricos() {
        return pedidoHistoricoStatusRepository.findAll();
    }

    public PedidoHistoricoStatus buscarPorId(String id) {
        return pedidoHistoricoStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Histórico de status não encontrado para o ID: " + id));
    }
}
