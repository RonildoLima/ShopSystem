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
        // Log inicial
        System.out.println("Mensagem recebida pelo RabbitMQ: " + historicoRecebido);

        PedidoHistoricoStatus historico = pedidoHistoricoStatusRepository.findById(historicoRecebido.getId())
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado para o ID: " + historicoRecebido.getId()));

        System.out.println("Histórico antes da atualização: " + historico);

        // Atualizar status com base na ação recebida
        if (historicoRecebido.getStatusPedido() == StatusPedidoEnum.PAGO) {
            historico.setDataHoraPagamento(LocalDateTime.now());
            historico.setStatusPedido(StatusPedidoEnum.PAGO);
            System.out.println("Atualizando histórico para PAGO: " + historico);
        } else if (historicoRecebido.getStatusPedido() == StatusPedidoEnum.CANCELADO) {
            historico.setStatusPedido(StatusPedidoEnum.CANCELADO);
            System.out.println("Atualizando histórico para CANCELADO: " + historico);
        }

        // Salvar o histórico atualizado no banco de dados
        PedidoHistoricoStatus salvo = pedidoHistoricoStatusRepository.save(historico);
        System.out.println("Histórico salvo no banco: " + salvo);
    }

}



