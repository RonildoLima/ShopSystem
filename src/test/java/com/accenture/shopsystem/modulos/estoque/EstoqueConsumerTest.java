package com.accenture.shopsystem.modulos.estoque;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueConsumerTest {

    @Test
    void atualizarEstoque_ComSucesso() {
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
        EstoqueConsumer estoqueConsumer = new EstoqueConsumer();
        estoqueConsumer.produtoRepository = produtoRepository;

        Produto produto = new Produto();
        produto.setId("1");
        produto.setQuantidadeEstoque(10);
        produto.setProdutoDescricao("Produto Teste");

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setProdutos(itens);

        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        doAnswer(invocation -> {
            Produto savedProduto = invocation.getArgument(0);
            assertEquals(8, savedProduto.getQuantidadeEstoque());
            return savedProduto;
        }).when(produtoRepository).save(any(Produto.class));

        estoqueConsumer.atualizarEstoque(pedido);

        verify(produtoRepository, times(1)).save(produto);
        verify(produtoRepository, times(1)).findById("1");
    }

    @Test
    void atualizarEstoque_ProdutoNaoEncontrado() {
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
        EstoqueConsumer estoqueConsumer = new EstoqueConsumer();
        estoqueConsumer.produtoRepository = produtoRepository;

        Produto produto = new Produto();
        produto.setId("1");

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setProdutos(itens);

        when(produtoRepository.findById("1")).thenReturn(Optional.empty());

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        estoqueConsumer.atualizarEstoque(pedido);

        String expectedMessage = "Erro ao atualizar estoque: Produto não encontrado: 1";
        assertTrue(errContent.toString().contains(expectedMessage));

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void atualizarEstoque_EstoqueInsuficiente() {
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);
        EstoqueConsumer estoqueConsumer = new EstoqueConsumer();
        estoqueConsumer.produtoRepository = produtoRepository;

        Produto produto = new Produto();
        produto.setId("1");
        produto.setQuantidadeEstoque(1);
        produto.setProdutoDescricao("Produto Teste");

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2);

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setProdutos(itens);

        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        estoqueConsumer.atualizarEstoque(pedido);

        String expectedMessage = "Erro ao atualizar estoque: Estoque insuficiente para o produto: Produto Teste";
        assertTrue(errContent.toString().contains(expectedMessage));

        verify(produtoRepository, never()).save(any());
    }
}
