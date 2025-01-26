package com.accenture.shopsystem.services.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

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
}
