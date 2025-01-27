package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoHistoricoStatusServiceTest {

    @Mock
    private PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PedidoHistoricoStatusService pedidoHistoricoStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processarPedido_Sucesso() {
        // Mock do pedido
        Pedido pedido = new Pedido();
        pedido.setId("123");

        // Criar instância do vendedor associado ao pedido
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        pedido.setVendedor(vendedor);

        // Mock do histórico
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatusPedido(StatusPedidoEnum.PENDENTE);

        // Configuração dos mocks
        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.findByPedidoId("123")).thenReturn(Optional.of(historico));

        // Chamar o método a ser testado
        pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "PROCESSAR");

        // Verificar os comportamentos
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq(historico));
        assertEquals(StatusPedidoEnum.PAGO, historico.getStatusPedido());
        assertNotNull(historico.getDataHoraPagamento());
    }

    @Test
    void processarPedido_PedidoNaoEncontrado() {
        // Configuração dos mocks
        when(pedidoRepository.findById("123")).thenReturn(Optional.empty());

        // Executar e verificar exceção
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "PROCESSAR");
        });

        assertEquals("Pedido não encontrado para o ID: 123", exception.getMessage());
    }

    @Test
    void processarPedido_VendedorNaoAutorizado() {
        // Mock do pedido
        Pedido pedido = new Pedido();
        pedido.setId("123");

        // Criar instância do vendedor associado ao pedido
        Vendedor vendedor = new Vendedor();
        vendedor.setId("outroVendedorId");
        pedido.setVendedor(vendedor);

        // Configuração dos mocks
        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));

        // Executar e verificar exceção
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "PROCESSAR");
        });

        assertEquals("Vendedor não autorizado a atualizar este pedido.", exception.getMessage());
    }

    @Test
    void processarPedido_StatusJaDefinido() {
        // Mock do pedido
        Pedido pedido = new Pedido();
        pedido.setId("123");

        // Criar instância do vendedor associado ao pedido
        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        pedido.setVendedor(vendedor);

        // Mock do histórico
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatusPedido(StatusPedidoEnum.PAGO);

        // Configuração dos mocks
        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.findByPedidoId("123")).thenReturn(Optional.of(historico));

        // Executar e verificar exceção
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "PROCESSAR");
        });

        assertEquals("O pedido já está com o status: PAGO e não pode ser alterado.", exception.getMessage());
    }
}
