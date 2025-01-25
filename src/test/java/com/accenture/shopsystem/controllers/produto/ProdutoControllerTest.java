package com.accenture.shopsystem.controllers.produto;

import com.accenture.shopsystem.controllers.produto.ProdutoController;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.produto.ProdutoRequestDTO;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.ProdutoRepository.*;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    @Test
    void adicionarProduto() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);

        ProdutoController controller = new ProdutoController(vendedorRepository);
        controller.produtoRepository = produtoRepository;

        // Mock SecurityContext e Authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("joao.silva@example.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor1");
        vendedor.setEmail("joao.silva@example.com");
        when(vendedorRepository.findByEmail("joao.silva@example.com")).thenReturn(Optional.of(vendedor));

        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setProdutoDescricao("Produto Teste");
        produtoRequestDTO.setProdutoValor(new BigDecimal("99.99"));
        produtoRequestDTO.setQuantidadeEstoque(10);

        RedirectView redirectView = controller.adicionarProduto(produtoRequestDTO);

        assertEquals("/produtos/adicionar", redirectView.getUrl());

        ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(produtoCaptor.capture());
        Produto savedProduto = produtoCaptor.getValue();

        assertEquals("Produto Teste", savedProduto.getProdutoDescricao());
        assertEquals(new BigDecimal("99.99"), savedProduto.getProdutoValor());
        assertEquals(10, savedProduto.getQuantidadeEstoque());
        assertEquals(vendedor, savedProduto.getVendedor());
    }

    @Test
    void excluirProduto() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);

        ProdutoController controller = new ProdutoController(vendedorRepository);
        controller.produtoRepository = produtoRepository;

        Produto produto = new Produto();
        produto.setId("produto1");
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor1");
        produto.setVendedor(vendedor);

        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

        ResponseEntity<Void> response = controller.excluirProduto("vendedor1", "produto1");

        assertEquals(204, response.getStatusCodeValue());
        verify(produtoRepository, times(1)).deleteById("produto1");
    }

    @Test
    void excluirProduto_NotFound() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);

        ProdutoController controller = new ProdutoController(vendedorRepository);
        controller.produtoRepository = produtoRepository;

        when(produtoRepository.findById("produto1")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = controller.excluirProduto("vendedor1", "produto1");

        assertEquals(404, response.getStatusCodeValue());
        verify(produtoRepository, never()).deleteById(anyString());
    }

    @Test
    void excluirProduto_Forbidden() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);

        ProdutoController controller = new ProdutoController(vendedorRepository);
        controller.produtoRepository = produtoRepository;

        Produto produto = new Produto();
        produto.setId("produto1");
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor2"); // ID diferente
        produto.setVendedor(vendedor);

        when(produtoRepository.findById("produto1")).thenReturn(Optional.of(produto));

        ResponseEntity<Void> response = controller.excluirProduto("vendedor1", "produto1");

        assertEquals(403, response.getStatusCodeValue());
        verify(produtoRepository, never()).deleteById(anyString());
    }

    @Test
    void listarProdutosPorVendedor() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);

        ProdutoController controller = new ProdutoController(vendedorRepository);
        controller.produtoRepository = produtoRepository;

        Produto produto1 = new Produto();
        produto1.setProdutoDescricao("Produto 1");
        Produto produto2 = new Produto();
        produto2.setProdutoDescricao("Produto 2");

        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoRepository.findByVendedorId("vendedor1")).thenReturn(produtos);

        ResponseEntity<Iterable<Produto>> response = controller.listarProdutosPorVendedor("vendedor1");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertIterableEquals(produtos, response.getBody());
        verify(produtoRepository, times(1)).findByVendedorId("vendedor1");
    }
}
