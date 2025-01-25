package com.accenture.shopsystem.pedido;

import com.accenture.shopsystem.consumers.pedido.PedidoConsumer;
import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoConsumerTest {

    @Test
    void processarPedido_ComSucesso() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);

        PedidoConsumer consumer = new PedidoConsumer(pedidoRepository, produtoRepository, historicoRepository);

        Produto produto1 = new Produto();
        produto1.setId("produto1");
        produto1.setQuantidadeEstoque(10);
        produto1.setProdutoValor(new BigDecimal("50.00"));

        Produto produto2 = new Produto();
        produto2.setId("produto2");
        produto2.setQuantidadeEstoque(20);
        produto2.setProdutoValor(new BigDecimal("30.00"));

        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById("produto2")).thenReturn(Optional.of(produto2));

        PedidoTemProdutos item1 = new PedidoTemProdutos();
        item1.setProduto(produto1);
        item1.setQuantidade(2);

        PedidoTemProdutos item2 = new PedidoTemProdutos();
        item2.setProduto(produto2);
        item2.setQuantidade(3);

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setProdutos(new ArrayList<>(List.of(item1, item2)));

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        consumer.processarPedido(pedido);

        assertEquals(8, produto1.getQuantidadeEstoque());
        assertEquals(17, produto2.getQuantidadeEstoque());

        verify(produtoRepository, times(1)).save(produto1);
        verify(produtoRepository, times(1)).save(produto2);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository, times(1)).save(pedidoCaptor.capture());
        Pedido pedidoSalvo = pedidoCaptor.getValue();

        assertEquals(new BigDecimal("190.00"), pedidoSalvo.getPedidoValor());
        assertEquals(5, pedidoSalvo.getPedidoQuantidade());
        assertEquals(2, pedidoSalvo.getProdutos().size());

        ArgumentCaptor<PedidoHistoricoStatus> historicoCaptor = ArgumentCaptor.forClass(PedidoHistoricoStatus.class);
        verify(historicoRepository, times(1)).save(historicoCaptor.capture());
        PedidoHistoricoStatus historicoSalvo = historicoCaptor.getValue();

        assertEquals(StatusPedidoEnum.PENDENTE, historicoSalvo.getStatusPedido());
        assertNotNull(historicoSalvo.getDataHoraStatusPedido());
        assertEquals(pedidoSalvo, historicoSalvo.getPedido());
    }

    @Test
    void processarPedido_ProdutoNaoEncontrado() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);

        PedidoConsumer consumer = new PedidoConsumer(pedidoRepository, produtoRepository, historicoRepository);

        Produto produto = new Produto();
        produto.setId("produto1");
        produto.setQuantidadeEstoque(10);
        produto.setProdutoValor(new BigDecimal("50.00"));

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(1);

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setProdutos(new ArrayList<>(List.of(item)));

        when(produtoRepository.findById("produto1")).thenReturn(Optional.empty());

        consumer.processarPedido(pedido);

        verify(produtoRepository, times(1)).findById("produto1");
        verify(produtoRepository, never()).save(any());
        verify(pedidoRepository, never()).save(any());
        verify(historicoRepository, never()).save(any());
    }

    @Test
    void processarPedido_EstoqueInsuficiente() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);

        PedidoConsumer consumer = new PedidoConsumer(pedidoRepository, produtoRepository, historicoRepository);

        Produto produto = new Produto();
        produto.setId("produto1");
        produto.setQuantidadeEstoque(1);
        produto.setProdutoValor(new BigDecimal("50.00"));

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(5);

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setProdutos(new ArrayList<>(List.of(item)));

        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

        consumer.processarPedido(pedido);

        verify(produtoRepository, times(1)).findById("produto1");
        verify(produtoRepository, never()).save(any());
        verify(pedidoRepository, never()).save(any());
        verify(historicoRepository, never()).save(any());
    }
}
