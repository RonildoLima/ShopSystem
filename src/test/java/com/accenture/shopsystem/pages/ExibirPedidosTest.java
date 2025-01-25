package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.pages.ExibirPedidos;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExibirPedidosTest {

    @Test
    void exibirPedidos() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        Model model = Mockito.mock(Model.class);

        ExibirPedidos exibirPedidos = new ExibirPedidos(pedidoRepository, vendedorRepository);

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

        Produto produto = new Produto();
        produto.setId("produto1");
        produto.setProdutoDescricao("Produto Teste");

        PedidoTemProdutos pedidoTemProdutos = new PedidoTemProdutos();
        pedidoTemProdutos.setProduto(produto);
        pedidoTemProdutos.setQuantidade(2);
        pedidoTemProdutos.setPrecoUnitario(new BigDecimal("50.00"));

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        pedido.setPedidoDescricao("Pedido Teste");
        pedido.setPedidoDataHora(LocalDateTime.now());
        pedido.setPedidoQuantidade(2);
        pedido.setPedidoValor(new BigDecimal("100.00"));
        pedido.setProdutos(new ArrayList<>(List.of(pedidoTemProdutos)));

        when(pedidoRepository.findPedidosByVendedorIdWithProdutos("vendedor1"))
                .thenReturn(List.of(pedido));

        String viewName = exibirPedidos.exibirPedidos(model);

        assertEquals("exibirPedidos", viewName);

        verify(vendedorRepository, times(1)).findByEmail("joao.silva@example.com");
        verify(pedidoRepository, times(1)).findPedidosByVendedorIdWithProdutos("vendedor1");
        verify(model, times(1)).addAttribute(eq("pedidos"), anyList());
    }

    @Test
    void exibirPedidos_VendedorNaoEncontrado() {
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        Model model = Mockito.mock(Model.class);

        ExibirPedidos exibirPedidos = new ExibirPedidos(pedidoRepository, vendedorRepository);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        DefaultOAuth2User oauth2User = Mockito.mock(DefaultOAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(oauth2User.getAttribute("email")).thenReturn("joao.silva@example.com");

        when(vendedorRepository.findByEmail("joao.silva@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exibirPedidos.exibirPedidos(model));
        assertEquals("Vendedor não encontrado", exception.getMessage());

        verify(vendedorRepository, times(1)).findByEmail("joao.silva@example.com");
        verify(pedidoRepository, never()).findPedidosByVendedorIdWithProdutos(anyString());
        verify(model, never()).addAttribute(anyString(), any());
    }
}
