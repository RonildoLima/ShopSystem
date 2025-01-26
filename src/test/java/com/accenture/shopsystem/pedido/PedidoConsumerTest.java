package com.accenture.shopsystem.pedido;

import com.accenture.shopsystem.modulos.pedido.PedidoConsumer;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
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

    @Test
    void processarPedido_ComSucesso() {
        // Mocks
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
        PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository = mock(PedidoHistoricoStatusRepository.class);
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

        PedidoConsumer pedidoConsumer = new PedidoConsumer(pedidoRepository, produtoRepository, pedidoHistoricoStatusRepository, rabbitTemplate);

        Produto produto = new Produto();
        produto.setId("1");
        produto.setQuantidadeEstoque(10);
        produto.setProdutoValor(BigDecimal.valueOf(100));

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setProdutos(itens);

        // Mock configurado
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Execução
        pedidoConsumer.processarPedido(pedido);

        // Validações
        assertEquals(8, produto.getQuantidadeEstoque()); // Estoque deve ser reduzido
        verify(produtoRepository, times(1)).save(produto); // Produto foi salvo
        verify(pedidoRepository, times(1)).save(pedido); // Pedido foi salvo
        verify(rabbitTemplate, times(1)).convertAndSend("estoque-queue", pedido); // Mensagem enviada
    }



    @Test
    void processarPedido_ProdutoNaoEncontrado() {
        // Mocks
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
        PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository = mock(PedidoHistoricoStatusRepository.class);
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

        PedidoConsumer pedidoConsumer = new PedidoConsumer(pedidoRepository, produtoRepository, pedidoHistoricoStatusRepository, rabbitTemplate);

        // Criação de um pedido com produto inexistente
        Pedido pedido = new Pedido();
        pedido.setId("123");

        Produto produto = new Produto();
        produto.setId("1");

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);
        pedido.setProdutos(itens);

        // Mock configurado para simular produto não encontrado
        when(produtoRepository.findById("1")).thenReturn(Optional.empty());

        // Execução
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pedidoConsumer.processarPedido(pedido));

        // Verificação
        assertEquals("Produto não encontrado: 1", exception.getMessage());
    }



    @Test
    void processarPedido_EstoqueInsuficiente() {
        // Mocks
        PedidoRepository pedidoRepository = mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
        PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository = mock(PedidoHistoricoStatusRepository.class);
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

        PedidoConsumer pedidoConsumer = new PedidoConsumer(pedidoRepository, produtoRepository, pedidoHistoricoStatusRepository, rabbitTemplate);

        Produto produto = new Produto();
        produto.setId("1");
        produto.setQuantidadeEstoque(1); // Estoque insuficiente
        produto.setProdutoValor(BigDecimal.valueOf(100));

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setProdutos(itens);

        // Mock configurado para simular estoque insuficiente
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        // Execução
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pedidoConsumer.processarPedido(pedido));

        // Verificação
        assertEquals("Estoque insuficiente para o produto: null", exception.getMessage());
    }



}
