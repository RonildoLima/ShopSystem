package com.accenture.shopsystem.pedido;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.modulos.pedido.PedidoConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoConsumerTest {

    private final PedidoRepository pedidoRepository = mock(PedidoRepository.class);
    private final ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
    private final PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository = mock(PedidoHistoricoStatusRepository.class);
    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

    private final PedidoConsumer pedidoConsumer = new PedidoConsumer(
            pedidoRepository, produtoRepository, pedidoHistoricoStatusRepository, rabbitTemplate
    );

    @Test
    void processarPedido_ComSucesso() {
        Pedido pedido = new Pedido();
        PedidoTemProdutos item = new PedidoTemProdutos();
        Produto produto = new Produto();

        produto.setId("produto1");
        produto.setQuantidadeEstoque(10);
        produto.setProdutoValor(BigDecimal.valueOf(50.0));

        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        pedido.setProdutos(itens);

        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        pedidoConsumer.processarPedido(pedido);

        verify(produtoRepository, times(1)).findById("produto1");
        verify(pedidoRepository, times(1)).save(pedido);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("estoque-queue"), any(Pedido.class));
    }

    @Test
    void processarPedido_ProdutoNaoEncontrado() {
        Pedido pedido = new Pedido();
        PedidoTemProdutos item = new PedidoTemProdutos();
        Produto produto = new Produto();

        produto.setId("produtoInexistente");

        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        pedido.setProdutos(itens);

        when(produtoRepository.findById("produtoInexistente")).thenReturn(Optional.empty());

        pedidoConsumer.processarPedido(pedido);

        verify(produtoRepository, times(1)).findById("produtoInexistente");
        verify(pedidoRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(any());
    }

    @Test
    void processarPedido_EstoqueInsuficiente() {
        Pedido pedido = new Pedido();
        PedidoTemProdutos item = new PedidoTemProdutos();
        Produto produto = new Produto();

        produto.setId("produto1");
        produto.setQuantidadeEstoque(1); // Estoque insuficiente
        produto.setProdutoValor(BigDecimal.valueOf(50.0));

        item.setProduto(produto);
        item.setQuantidade(2); // Requer mais do que o disponível

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        pedido.setProdutos(itens);

        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

        pedidoConsumer.processarPedido(pedido);

        verify(produtoRepository, times(1)).findById("produto1");
        verify(pedidoRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(any());
    }
}
