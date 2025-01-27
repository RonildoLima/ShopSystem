package com.accenture.shopsystem.modulos.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;

import java.time.LocalDateTime;

@Component
public class PedidoHistoricoStatusConsumer {

    private final PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    public PedidoHistoricoStatusConsumer(PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository) {
        this.pedidoHistoricoStatusRepository = pedidoHistoricoStatusRepository;
    }

    @RabbitListener(queues = "status-queue")
    public void processarHistoricoStatus(PedidoHistoricoStatus historicoRecebido) {
        PedidoHistoricoStatus historico = pedidoHistoricoStatusRepository.findById(historicoRecebido.getId())
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado para o ID: " + historicoRecebido.getId()));

        historico.setDataHoraPagamento(LocalDateTime.now());
        historico.setStatusPedido(StatusPedidoEnum.PAGO);

        pedidoHistoricoStatusRepository.save(historico);

        System.out.println("Histórico de status atualizado com sucesso para o pedido: " + historico.getPedido().getId());
    }
}

