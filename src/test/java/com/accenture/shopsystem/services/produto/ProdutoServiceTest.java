package com.accenture.shopsystem.services.produto;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.produto.ProdutoRequestDTO;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.math.BigDecimal;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProdutoService produtoService;

    ProdutoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adicionarProduto_ComSucesso() {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setProdutoDescricao("Produto Teste");
        produtoRequestDTO.setProdutoValor(BigDecimal.valueOf(100.0));
        produtoRequestDTO.setQuantidadeEstoque(10);

        DefaultOAuth2User oauth2User = mock(DefaultOAuth2User.class);
        when(oauth2User.getAttribute("email")).thenReturn("vendedor@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        SecurityContextHolder.setContext(securityContext);

        Vendedor vendedor = new Vendedor();
        vendedor.setEmail("vendedor@example.com");

        when(vendedorRepository.findByEmail("vendedor@example.com")).thenReturn(Optional.of(vendedor));
        when(produtoRepository.save(any(Produto.class))).thenReturn(new Produto());

        RedirectView redirectView = produtoService.adicionarProduto(produtoRequestDTO, mock(RedirectAttributes.class));

        assertNotNull(redirectView);
        assertEquals("/produtos/adicionar", redirectView.getUrl());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void adicionarProduto_VendedorNaoEncontrado() {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setProdutoDescricao("Produto Teste");
        produtoRequestDTO.setProdutoValor(BigDecimal.valueOf(100.0));
        produtoRequestDTO.setQuantidadeEstoque(10);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("vendedor@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(vendedorRepository.findByEmail("vendedor@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> produtoService.adicionarProduto(produtoRequestDTO, mock(RedirectAttributes.class)));
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void excluirProduto_ComSucesso() {
        Produto produto = new Produto();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor123");
        produto.setId("produto123");
        produto.setVendedor(vendedor);

        when(produtoRepository.findById("produto123")).thenReturn(Optional.of(produto));

        produtoService.excluirProduto("vendedor123", "produto123");

        verify(produtoRepository, times(1)).deleteById("produto123");
    }

    @Test
    void excluirProduto_ProdutoNaoEncontrado() {
        when(produtoRepository.findById("produtoInexistente")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> produtoService.excluirProduto("vendedor123", "produtoInexistente"));
        verify(produtoRepository, never()).deleteById(anyString());
    }

    @Test
    void excluirProduto_SemPermissao() {
        Produto produto = new Produto();
        Vendedor vendedor = new Vendedor();
        vendedor.setId("outroVendedor");
        produto.setId("produto123");
        produto.setVendedor(vendedor);

        when(produtoRepository.findById("produto123")).thenReturn(Optional.of(produto));

        assertThrows(RuntimeException.class, () -> produtoService.excluirProduto("vendedor123", "produto123"));
        verify(produtoRepository, never()).deleteById(anyString());
    }

    @Test
    void listarProdutosPorVendedor() {
        when(produtoRepository.findByVendedorId("vendedor123")).thenReturn(Collections.emptyList());

        Iterable<Produto> produtos = produtoService.listarProdutosPorVendedor("vendedor123");

        assertNotNull(produtos);
        verify(produtoRepository, times(1)).findByVendedorId("vendedor123");
    }
}

