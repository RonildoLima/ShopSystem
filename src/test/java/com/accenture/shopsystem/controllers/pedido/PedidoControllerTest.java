package com.accenture.shopsystem.controllers.pedido;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import com.accenture.shopsystem.services.pedido.PedidoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @Test
    void salvar() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        PedidoService pedidoService = Mockito.mock(PedidoService.class);

        PedidoController controller = new PedidoController();
        controller.pedidoRepository = pedidoRepository;
        controller.produtoRepository = produtoRepository;
        controller.vendedorRepository = vendedorRepository;
        controller.pedidoService = pedidoService;

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor1");

        Produto produto1 = new Produto();
        produto1.setId("produto1");

        Produto produto2 = new Produto();
        produto2.setId("produto2");

        PedidoTemProdutos item1 = new PedidoTemProdutos();
        item1.setProduto(produto1);

        PedidoTemProdutos item2 = new PedidoTemProdutos();
        item2.setProduto(produto2);

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setProdutos(new ArrayList<>());
        pedido.getProdutos().add(item1);
        pedido.getProdutos().add(item2);

        when(vendedorRepository.findById("vendedor1")).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById("produto2")).thenReturn(Optional.of(produto2));

        ResponseEntity<String> response = controller.salvar("vendedor1", pedido);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pedido enviado para processamento!", response.getBody());
        assertEquals(vendedor, pedido.getVendedor());

        verify(pedidoService, times(1)).enviarPedido(pedido);
        verify(vendedorRepository, times(1)).findById("vendedor1");
        verify(produtoRepository, times(1)).findById("produto1");
        verify(produtoRepository, times(1)).findById("produto2");
    }

    @Test
    void salvar_VendedorNaoEncontrado() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        PedidoService pedidoService = Mockito.mock(PedidoService.class);

        PedidoController controller = new PedidoController();
        controller.pedidoRepository = pedidoRepository;
        controller.produtoRepository = produtoRepository;
        controller.vendedorRepository = vendedorRepository;
        controller.pedidoService = pedidoService;

        Pedido pedido = new Pedido();

        when(vendedorRepository.findById("vendedor1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.salvar("vendedor1", pedido));
        assertEquals("Vendedor não encontrado: vendedor1", exception.getMessage());

        verify(vendedorRepository, times(1)).findById("vendedor1");
        verify(pedidoService, never()).enviarPedido(any());
    }

    @Test
    void salvar_ProdutoNaoEncontrado() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        PedidoService pedidoService = Mockito.mock(PedidoService.class);

        PedidoController controller = new PedidoController();
        controller.pedidoRepository = pedidoRepository;
        controller.produtoRepository = produtoRepository;
        controller.vendedorRepository = vendedorRepository;
        controller.pedidoService = pedidoService;

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor1");

        Produto produto1 = new Produto();
        produto1.setId("produto1");

        PedidoTemProdutos item1 = new PedidoTemProdutos();
        item1.setProduto(produto1);

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setProdutos(new ArrayList<>());
        pedido.getProdutos().add(item1);

        when(vendedorRepository.findById("vendedor1")).thenReturn(Optional.of(vendedor));
        when(produtoRepository.findById("produto1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.salvar("vendedor1", pedido));
        assertEquals("Produto não encontrado: produto1", exception.getMessage());

        verify(vendedorRepository, times(1)).findById("vendedor1");
        verify(produtoRepository, times(1)).findById("produto1");
        verify(pedidoService, never()).enviarPedido(any());
    }

    @Test
    void deletarPedido() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        PedidoService pedidoService = Mockito.mock(PedidoService.class);

        PedidoController controller = new PedidoController();
        controller.pedidoRepository = pedidoRepository;
        controller.produtoRepository = produtoRepository;
        controller.vendedorRepository = vendedorRepository;
        controller.pedidoService = pedidoService;

        doNothing().when(pedidoService).deletarPedido("pedido1", "vendedor1");

        ResponseEntity<Void> response = controller.deletarPedido("pedido1", "vendedor1");

        assertEquals(204, response.getStatusCodeValue());
        verify(pedidoService, times(1)).deletarPedido("pedido1", "vendedor1");
    }
}
