package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExibirStatusTest {

    @Mock
    private PedidoHistoricoStatusRepository historicoStatusRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @InjectMocks
    private ExibirStatus exibirStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void exibirStatusPedidos_ComSucesso() {
        DefaultOAuth2User user = new DefaultOAuth2User(
                Collections.emptyList(),
                Collections.singletonMap("email", "vendedor@teste.com"),
                "email"
        );
        when(authentication.getPrincipal()).thenReturn(user);

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.of(vendedor));

        Pedido pedido = new Pedido();
        pedido.setId("pedidoId");
        pedido.setPedidoDescricao("Descrição do Pedido");

        PedidoHistoricoStatus historicoStatus = new PedidoHistoricoStatus();
        historicoStatus.setPedido(pedido);
        historicoStatus.setStatusPedido(StatusPedidoEnum.PENDENTE);
        historicoStatus.setDataHoraStatusPedido(LocalDateTime.now());

        List<PedidoHistoricoStatus> historicos = new ArrayList<>();
        historicos.add(historicoStatus);

        when(historicoStatusRepository.findByPedidoVendedorId("vendedorId")).thenReturn(historicos);

        String viewName = exibirStatus.exibirStatusPedidos(model);

        assertEquals("exibirStatus", viewName);
        verify(model, times(1)).addAttribute("historicoStatus", historicos);
    }

    @Test
    void exibirStatusPedidos_SemPedidos() {
        DefaultOAuth2User user = new DefaultOAuth2User(
                Collections.emptyList(),
                Collections.singletonMap("email", "vendedor@teste.com"),
                "email"
        );
        when(authentication.getPrincipal()).thenReturn(user);

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.of(vendedor));

        when(historicoStatusRepository.findByPedidoVendedorId("vendedorId")).thenReturn(new ArrayList<>());

        String viewName = exibirStatus.exibirStatusPedidos(model);

        assertEquals("exibirStatus", viewName);
        verify(model, times(1)).addAttribute("historicoStatus", new ArrayList<>());
    }

    @Test
    void exibirStatusPedidos_EmailNaoEncontrado() {
        DefaultOAuth2User user = new DefaultOAuth2User(
                Collections.emptyList(),
                Collections.singletonMap("email", "vendedor@teste.com"),
                "email"
        );
        when(authentication.getPrincipal()).thenReturn(user);

        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exibirStatus.exibirStatusPedidos(model));

        assertEquals("Vendedor não encontrado", exception.getMessage());
        verifyNoInteractions(historicoStatusRepository);
    }
}
