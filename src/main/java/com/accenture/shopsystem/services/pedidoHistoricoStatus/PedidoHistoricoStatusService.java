package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class PedidoHistoricoStatusService {

	private static final Logger logger = LoggerFactory.getLogger(PedidoHistoricoStatusService.class);

    private final PedidoValidator pedidoValidator;
    private final RabbitTemplate rabbitTemplate;

    public PedidoHistoricoStatusService(PedidoValidator pedidoValidator, RabbitTemplate rabbitTemplate) {
        this.pedidoValidator = pedidoValidator;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processarPedido(String pedidoId, String vendedorId, String acao) {
    	logger.info("Iniciando o processamento do pedido com ID: {}", pedidoId);

        Pedido pedido = pedidoValidator.validarPedidoExistente(pedidoId);
        pedidoValidator.validarVendedorAutorizado(pedido, vendedorId);

        PedidoHistoricoStatus historico = pedidoValidator.validarHistoricoExistente(pedidoId);
        pedidoValidator.validarStatusAlteracao(historico);

        if ("PROCESSAR".equalsIgnoreCase(acao)) {
            historico.setDataHoraPagamento(LocalDateTime.now());
            historico.setStatusPedido(StatusPedidoEnum.PAGO);
            logger.info("O pedido com ID: {} foi processado e marcado como PAGO.", pedidoId);
        } else if ("CANCELAR".equalsIgnoreCase(acao)) {
            historico.setStatusPedido(StatusPedidoEnum.CANCELADO);
            logger.info("O pedido com ID: {} foi cancelado.", pedidoId);
        } else {
            logger.error("Ação inválida fornecida para o pedido com ID: {}", pedidoId);
            throw new IllegalArgumentException("Ação inválida: " + acao);
        }

        rabbitTemplate.convertAndSend("status-queue", historico);
        logger.info("Status do pedido com ID: {} foi enviado para a fila RabbitMQ.", pedidoId);
    }


    public Iterable<PedidoHistoricoStatus> listarHistoricos() {
    	logger.info("Listando todos os históricos de pedidos.");
    	return pedidoValidator.listarTodosHistoricos();
    }

    public PedidoHistoricoStatus buscarPorId(String id) {
    	logger.info("Buscando histórico de status pelo ID: {}", id);
        return pedidoValidator.validarHistoricoExistente(id);
    }
}
