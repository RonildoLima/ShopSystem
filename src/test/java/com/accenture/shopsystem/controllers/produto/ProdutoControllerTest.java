package com.accenture.shopsystem.controllers.produto;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.produto.ProdutoRequestDTO;
import com.accenture.shopsystem.services.produto.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    @Test
    void adicionarProduto() {
        ProdutoService produtoService = mock(ProdutoService.class);
        ProdutoController produtoController = new ProdutoController(produtoService);

        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setProdutoDescricao("Produto Teste");
        requestDTO.setProdutoValor(BigDecimal.valueOf(100.0));
        requestDTO.setQuantidadeEstoque(10);

        RedirectView redirectView = new RedirectView("/produtos/adicionar");
        when(produtoService.adicionarProduto(requestDTO)).thenReturn(redirectView);

        RedirectView response = produtoController.adicionarProduto(requestDTO);

        assertNotNull(response);
        assertEquals("/produtos/adicionar", response.getUrl());
        verify(produtoService, times(1)).adicionarProduto(requestDTO);
    }

    @Test
    void excluirProduto() {
        ProdutoService produtoService = mock(ProdutoService.class);
        ProdutoController produtoController = new ProdutoController(produtoService);

        String vendedorId = "vendedor123";
        String produtoId = "produto123";

        doNothing().when(produtoService).excluirProduto(vendedorId, produtoId);

        ResponseEntity<Void> response = produtoController.excluirProduto(vendedorId, produtoId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(produtoService, times(1)).excluirProduto(vendedorId, produtoId);
    }

    @Test
    void excluirProduto_NotFound() {
        ProdutoService produtoService = mock(ProdutoService.class);
        ProdutoController produtoController = new ProdutoController(produtoService);

        String vendedorId = "vendedor123";
        String produtoId = "produtoInexistente";

        doThrow(new RuntimeException("Produto não encontrado."))
                .when(produtoService).excluirProduto(vendedorId, produtoId);

        ResponseEntity<Void> response = produtoController.excluirProduto(vendedorId, produtoId);

        assertNotNull(response);
        assertEquals(403, response.getStatusCodeValue());
        verify(produtoService, times(1)).excluirProduto(vendedorId, produtoId);
    }

    @Test
    void excluirProduto_Forbidden() {
        ProdutoService produtoService = mock(ProdutoService.class);
        ProdutoController produtoController = new ProdutoController(produtoService);

        String vendedorId = "vendedor123";
        String produtoId = "produto123";

        doThrow(new RuntimeException("Você não tem permissão para excluir este produto."))
                .when(produtoService).excluirProduto(vendedorId, produtoId);

        ResponseEntity<Void> response = produtoController.excluirProduto(vendedorId, produtoId);

        assertNotNull(response);
        assertEquals(403, response.getStatusCodeValue());
        verify(produtoService, times(1)).excluirProduto(vendedorId, produtoId);
    }

    @Test
    void listarProdutosPorVendedor() {
        ProdutoService produtoService = mock(ProdutoService.class);
        ProdutoController produtoController = new ProdutoController(produtoService);

        String vendedorId = "vendedor123";

        List<Produto> produtos = new ArrayList<>();
        Produto produto1 = new Produto();
        produto1.setProdutoDescricao("Produto 1");
        Produto produto2 = new Produto();
        produto2.setProdutoDescricao("Produto 2");
        produtos.add(produto1);
        produtos.add(produto2);

        when(produtoService.listarProdutosPorVendedor(vendedorId)).thenReturn(produtos);

        ResponseEntity<Iterable<Produto>> response = produtoController.listarProdutosPorVendedor(vendedorId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(produtos, response.getBody());
        verify(produtoService, times(1)).listarProdutosPorVendedor(vendedorId);
    }
}
