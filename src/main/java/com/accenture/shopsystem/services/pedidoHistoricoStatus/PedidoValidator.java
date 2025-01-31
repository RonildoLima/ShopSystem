package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PedidoValidator {

    private static final Logger logger = LoggerFactory.getLogger(PedidoValidator.class);

    private final PedidoRepository pedidoRepository;
    private final PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    public PedidoValidator(PedidoRepository pedidoRepository, PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoHistoricoStatusRepository = pedidoHistoricoStatusRepository;
    }

    public Pedido validarPedidoExistente(String pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado para o ID: {}", pedidoId);
                    return new IllegalArgumentException("Pedido não encontrado para o ID: " + pedidoId);
                });
    }

    public PedidoHistoricoStatus validarHistoricoExistente(String pedidoId) {
        return pedidoHistoricoStatusRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> {
                    logger.error("Histórico de pedido não encontrado para o ID: {}", pedidoId);
                    return new IllegalArgumentException("Histórico de pedido não encontrado para o ID: " + pedidoId);
                });
    }

    public void validarStatusAlteracao(PedidoHistoricoStatus historico) {
        if (historico.getStatusPedido() == StatusPedidoEnum.PAGO || historico.getStatusPedido() == StatusPedidoEnum.CANCELADO) {
            logger.warn("O pedido já está com o status: {} e não pode ser alterado.", historico.getStatusPedido());
            throw new IllegalArgumentException("O pedido já está com o status: " + historico.getStatusPedido() + " e não pode ser alterado.");
        }
    }

    public void validarVendedorAutorizado(Pedido pedido, String vendedorId) {
        if (!pedido.getVendedor().getId().equals(vendedorId)) {
            logger.error("Vendedor não autorizado a atualizar o pedido com ID: {}", pedido.getId());
            throw new IllegalArgumentException("Vendedor não autorizado a atualizar este pedido.");
        }
    }
    public Iterable<PedidoHistoricoStatus> listarTodosHistoricos() {
        return pedidoHistoricoStatusRepository.findAll();
    }
}
