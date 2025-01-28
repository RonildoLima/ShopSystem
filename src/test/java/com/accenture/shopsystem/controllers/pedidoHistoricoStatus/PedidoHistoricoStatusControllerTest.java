package com.accenture.shopsystem.controllers.pedidoHistoricoStatus;

import com.accenture.shopsystem.exceptions.ShopSystemExceptions;
import com.accenture.shopsystem.services.pedidoHistoricoStatus.PedidoHistoricoStatusService;
import com.accenture.shopsystem.repositories.VendedorRepository;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PedidoHistoricoStatusControllerTest {

    @Mock
    private PedidoHistoricoStatusService pedidoHistoricoStatusService;

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PedidoHistoricoStatusController pedidoHistoricoStatusController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void configurarAutenticacao(String email) {
        DefaultOAuth2User user = new DefaultOAuth2User(
                Collections.emptyList(),
                Collections.singletonMap("email", email),
                "email"
        );
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void processarPedido_Sucesso() {
        configurarAutenticacao("vendedor@teste.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.of(vendedor));

        doNothing().when(pedidoHistoricoStatusService).processarPedido("pedidoId123", "vendedorId", "PROCESSAR");

        pedidoHistoricoStatusController.processarPedido("pedidoId123", "PROCESSAR");

        verify(pedidoHistoricoStatusService, times(1))
                .processarPedido("pedidoId123", "vendedorId", "PROCESSAR");
    }

    @Test
    void processarPedido_EmailNaoEncontrado() {
        configurarAutenticacao("vendedor@teste.com");

        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.empty());

        assertThrows(
                ShopSystemExceptions.VendedorNaoEncontradoException.class,
                () -> pedidoHistoricoStatusController.processarPedido("pedidoId123", "PROCESSAR")
        );

        verify(vendedorRepository, times(1)).findByEmail("vendedor@teste.com");
        verify(pedidoHistoricoStatusService, never()).processarPedido(anyString(), anyString(), anyString());
    }

    @Test
    void processarPedido_ComErroDeNegocio() {
        configurarAutenticacao("vendedor@teste.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.of(vendedor));

        doThrow(new IllegalArgumentException("Erro ao processar pedido"))
                .when(pedidoHistoricoStatusService)
                .processarPedido("pedidoId123", "vendedorId", "PROCESSAR");

        // Agora espera especificamente uma IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusController.processarPedido("pedidoId123", "PROCESSAR");
        });

        System.out.println("Exceção capturada corretamente: " + exception.getMessage());

        verify(pedidoHistoricoStatusService, times(1))
                .processarPedido("pedidoId123", "vendedorId", "PROCESSAR");
    }

    @Test
    void processarPedido_CancelarComSucesso() {
        configurarAutenticacao("vendedor@teste.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.of(vendedor));

        doNothing().when(pedidoHistoricoStatusService).processarPedido("pedidoId123", "vendedorId", "CANCELAR");

        pedidoHistoricoStatusController.processarPedido("pedidoId123", "CANCELAR");

        verify(pedidoHistoricoStatusService, times(1))
                .processarPedido("pedidoId123", "vendedorId", "CANCELAR");
    }

    @Test
    void processarPedido_AcaoInvalida() {
        configurarAutenticacao("vendedor@teste.com");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        when(vendedorRepository.findByEmail("vendedor@teste.com")).thenReturn(Optional.of(vendedor));

        assertThrows(
                ShopSystemExceptions.AcaoInvalidaException.class,
                () -> pedidoHistoricoStatusController.processarPedido("pedidoId123", "INVALIDO")
        );

        verify(pedidoHistoricoStatusService, never()).processarPedido(anyString(), anyString(), anyString());
    }
}
