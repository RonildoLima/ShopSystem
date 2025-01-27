package com.accenture.shopsystem.services.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    @InjectMocks
    private PedidoService pedidoService;

    PedidoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deletarPedido_ComSucesso() {
        Pedido pedido = new Pedido();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor123");
        pedido.setId("pedido123");
        pedido.setVendedor(vendedor);

        when(pedidoRepository.findById("pedido123")).thenReturn(Optional.of(pedido));

        pedidoService.deletarPedido("pedido123", "vendedor123");

        verify(pedidoHistoricoStatusRepository, times(1)).deleteByPedido(pedido);
        verify(pedidoRepository, times(1)).delete(pedido);
    }

    @Test
    void deletarPedido_PedidoNaoEncontrado() {
        when(pedidoRepository.findById("pedidoInexistente")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoService.deletarPedido("pedidoInexistente", "vendedor123"));
        verify(pedidoHistoricoStatusRepository, never()).deleteByPedido(any());
        verify(pedidoRepository, never()).delete(any());
    }

    @Test
    void deletarPedido_SemPermissao() {
        Pedido pedido = new Pedido();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("outroVendedor");
        pedido.setId("pedido123");
        pedido.setVendedor(vendedor);

        when(pedidoRepository.findById("pedido123")).thenReturn(Optional.of(pedido));

        assertThrows(RuntimeException.class, () -> pedidoService.deletarPedido("pedido123", "vendedor123"));
        verify(pedidoHistoricoStatusRepository, never()).deleteByPedido(any());
        verify(pedidoRepository, never()).delete(any());
    }
}
