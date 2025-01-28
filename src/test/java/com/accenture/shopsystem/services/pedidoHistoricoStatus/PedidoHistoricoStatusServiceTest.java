package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
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
    void processarPedido_SucessoCancelar() {
        Pedido pedido = new Pedido();
        pedido.setId("123");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        pedido.setVendedor(vendedor);

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatusPedido(StatusPedidoEnum.PENDENTE);

        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.findByPedidoId("123")).thenReturn(Optional.of(historico));

        pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "CANCELAR");

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), eq(historico));
        assertEquals(StatusPedidoEnum.CANCELADO, historico.getStatusPedido());
        assertNull(historico.getDataHoraPagamento());
    }

    @Test
    void processarPedido_AcaoInvalida() {
        Pedido pedido = new Pedido();
        pedido.setId("123");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        pedido.setVendedor(vendedor);

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatusPedido(StatusPedidoEnum.PENDENTE);

        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.findByPedidoId("123")).thenReturn(Optional.of(historico));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "INVALIDO");
        });

        assertEquals("Ação inválida: INVALIDO", exception.getMessage());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoHistoricoStatus.class));
    }

    @Test
    void listarHistoricos() {
        pedidoHistoricoStatusService.listarHistoricos();
        verify(pedidoHistoricoStatusRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_ComSucesso() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("123");

        when(pedidoHistoricoStatusRepository.findById("123")).thenReturn(Optional.of(historico));

        PedidoHistoricoStatus result = pedidoHistoricoStatusService.buscarPorId("123");

        assertNotNull(result);
        assertEquals("123", result.getId());
        verify(pedidoHistoricoStatusRepository, times(1)).findById("123");
    }

    @Test
    void buscarPorId_NaoEncontrado() {
        when(pedidoHistoricoStatusRepository.findById("123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.buscarPorId("123");
        });

        assertEquals("Histórico de status não encontrado para o ID: 123", exception.getMessage());
        verify(pedidoHistoricoStatusRepository, times(1)).findById("123");
    }

    @Test
    void processarPedido_StatusCancelado() {
        Pedido pedido = new Pedido();
        pedido.setId("123");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        pedido.setVendedor(vendedor);

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatusPedido(StatusPedidoEnum.CANCELADO);

        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.findByPedidoId("123")).thenReturn(Optional.of(historico));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "PROCESSAR");
        });

        assertEquals("O pedido já está com o status: CANCELADO e não pode ser alterado.", exception.getMessage());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoHistoricoStatus.class));
    }

    @Test
    void processarPedido_HistoricoNaoEncontrado() {
        Pedido pedido = new Pedido();
        pedido.setId("123");

        Vendedor vendedor = new Vendedor();
        vendedor.setId("vendedorId");
        pedido.setVendedor(vendedor);

        when(pedidoRepository.findById("123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.findByPedidoId("123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoHistoricoStatusService.processarPedido("123", "vendedorId", "PROCESSAR");
        });

        assertEquals("Histórico de pedido não encontrado para o ID: 123", exception.getMessage());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(PedidoHistoricoStatus.class));
    }

}
