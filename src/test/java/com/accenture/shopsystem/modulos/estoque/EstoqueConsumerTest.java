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
        // Mock do repositório
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);

        // Instância do consumidor
        EstoqueConsumer estoqueConsumer = new EstoqueConsumer();
        estoqueConsumer.produtoRepository = produtoRepository;

        // Dados de teste
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

        // Configuração do mock
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        // Simular o comportamento real do save no mock
        doAnswer(invocation -> {
            Produto savedProduto = invocation.getArgument(0);
            assertEquals(8, savedProduto.getQuantidadeEstoque()); // Valida que o estoque foi reduzido corretamente
            return savedProduto;
        }).when(produtoRepository).save(any(Produto.class));

        // Execução
        estoqueConsumer.atualizarEstoque(pedido);

        // Verificações
        verify(produtoRepository, times(1)).save(produto); // Produto foi salvo
        verify(produtoRepository, times(1)).findById("1"); // Produto foi buscado
    }

    @Test
    void atualizarEstoque_ProdutoNaoEncontrado() {
        // Mock do repositório
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);

        // Instância do consumidor
        EstoqueConsumer estoqueConsumer = new EstoqueConsumer();
        estoqueConsumer.produtoRepository = produtoRepository;

        // Dados de teste
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

        // Configuração do mock
        when(produtoRepository.findById("1")).thenReturn(Optional.empty()); // Simula produto não encontrado

        // Captura mensagens de erro no console
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream (errContent));

        // Execução
        estoqueConsumer.atualizarEstoque(pedido);

        // Verifica se a mensagem de erro foi registrada
        String expectedMessage = "Erro ao atualizar estoque: Produto não encontrado: 1";
        assertTrue(errContent.toString().contains(expectedMessage));

        // Verifica que o método save não foi chamado
        verify(produtoRepository, never()).save(any());
    }



    @Test
    void atualizarEstoque_EstoqueInsuficiente() {
        // Mock do repositório
        ProdutoRepository produtoRepository = mock(ProdutoRepository.class);

        // Instância do consumidor
        EstoqueConsumer estoqueConsumer = new EstoqueConsumer();
        estoqueConsumer.produtoRepository = produtoRepository;

        // Dados de teste
        Produto produto = new Produto();
        produto.setId("1");
        produto.setQuantidadeEstoque(1); // Estoque insuficiente
        produto.setProdutoDescricao("Produto Teste");

        PedidoTemProdutos item = new PedidoTemProdutos();
        item.setProduto(produto);
        item.setQuantidade(2); // Quantidade maior que o estoque disponível

        List<PedidoTemProdutos> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = new Pedido();
        pedido.setId("123");
        pedido.setProdutos(itens);

        // Configuração do mock
        when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        // Execução e verificação
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estoqueConsumer.atualizarEstoque(pedido);
        });

        assertEquals("Estoque insuficiente para o produto: Produto Teste", exception.getMessage());
    }
}
