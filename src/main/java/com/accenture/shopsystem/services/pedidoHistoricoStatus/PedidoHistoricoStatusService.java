package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void processarPedido(String pedidoId, String vendedorId, String acao) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado para o ID: " + pedidoId));

        if (!pedido.getVendedor().getId().equals(vendedorId)) {
            throw new IllegalArgumentException("Vendedor não autorizado a atualizar este pedido.");
        }

        PedidoHistoricoStatus historico = pedidoHistoricoStatusRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Histórico de pedido não encontrado para o ID: " + pedidoId));

        if (historico.getStatusPedido() == StatusPedidoEnum.PAGO || historico.getStatusPedido() == StatusPedidoEnum.CANCELADO) {
            throw new IllegalArgumentException("O pedido já está com o status: " + historico.getStatusPedido() + " e não pode ser alterado.");
        }

        if ("PROCESSAR".equalsIgnoreCase(acao)) {
            historico.setDataHoraPagamento(LocalDateTime.now());
            historico.setStatusPedido(StatusPedidoEnum.PAGO);
        } else if ("CANCELAR".equalsIgnoreCase(acao)) {
            historico.setStatusPedido(StatusPedidoEnum.CANCELADO);
        } else {
            throw new IllegalArgumentException("Ação inválida: " + acao);
        }
        rabbitTemplate.convertAndSend("status-queue", historico);
    }


    public Iterable<PedidoHistoricoStatus> listarHistoricos() {
        return pedidoHistoricoStatusRepository.findAll();
    }

    public PedidoHistoricoStatus buscarPorId(String id) {
        return pedidoHistoricoStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Histórico de status não encontrado para o ID: " + id));
    }
}
