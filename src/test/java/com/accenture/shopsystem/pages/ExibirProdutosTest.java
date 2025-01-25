package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.pages.ExibirProdutos;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExibirProdutosTest {

    @Test
    void exibirDashboard() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        Model model = Mockito.mock(Model.class);

        ExibirProdutos exibirProdutos = new ExibirProdutos(produtoRepository, vendedorRepository);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        DefaultOAuth2User oauth2User = Mockito.mock(DefaultOAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(oauth2User.getAttribute("email")).thenReturn("joao.silva@example.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedor1");
        vendedor.setEmail("joao.silva@example.com");

        when(vendedorRepository.findByEmail("joao.silva@example.com")).thenReturn(Optional.of(vendedor));

        Produto produto1 = new Produto();
        produto1.setId("produto1");
        produto1.setProdutoDescricao("Produto 1");

        Produto produto2 = new Produto();
        produto2.setId("produto2");
        produto2.setProdutoDescricao("Produto 2");

        List<Produto> produtos = List.of(produto1, produto2);

        when(produtoRepository.findByVendedorId("vendedor1")).thenReturn(produtos);

        String viewName = exibirProdutos.exibirDashboard(model);

        assertEquals("exibirProdutos", viewName);

        verify(vendedorRepository, times(1)).findByEmail("joao.silva@example.com");
        verify(produtoRepository, times(1)).findByVendedorId("vendedor1");
        verify(model, times(1)).addAttribute("produtos", produtos);
    }

    @Test
    void exibirDashboard_VendedorNaoEncontrado() {
        ProdutoRepository produtoRepository = Mockito.mock(ProdutoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        Model model = Mockito.mock(Model.class);

        ExibirProdutos exibirProdutos = new ExibirProdutos(produtoRepository, vendedorRepository);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        DefaultOAuth2User oauth2User = Mockito.mock(DefaultOAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(oauth2User.getAttribute("email")).thenReturn("joao.silva@example.com");

        when(vendedorRepository.findByEmail("joao.silva@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exibirProdutos.exibirDashboard(model));
        assertEquals("Vendedor não encontrado", exception.getMessage());

        verify(vendedorRepository, times(1)).findByEmail("joao.silva@example.com");
        verify(produtoRepository, never()).findByVendedorId(anyString());
        verify(model, never()).addAttribute(anyString(), any());
    }
}
