package com.accenture.shopsystem.services.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoHistoricoStatusServiceTest {

    @Mock
    private PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoHistoricoStatusService pedidoHistoricoStatusService;

    PedidoHistoricoStatusServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void salvarHistorico_ComSucesso() {
        Pedido pedido = new Pedido();
        pedido.setId("pedido123");

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);

        when(pedidoRepository.findById("pedido123")).thenReturn(Optional.of(pedido));
        when(pedidoHistoricoStatusRepository.save(historico)).thenReturn(historico);

        PedidoHistoricoStatus resultado = pedidoHistoricoStatusService.salvarHistorico(historico);

        assertNotNull(resultado.getDataHoraStatusPedido());
        assertEquals(historico, resultado);
        verify(pedidoHistoricoStatusRepository, times(1)).save(historico);
    }

    @Test
    void salvarHistorico_PedidoNaoEncontrado() {
        Pedido pedido = new Pedido();
        pedido.setId("pedidoInexistente");

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);

        when(pedidoRepository.findById("pedidoInexistente")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pedidoHistoricoStatusService.salvarHistorico(historico));
        verify(pedidoHistoricoStatusRepository, never()).save(any());
    }

    @Test
    void listarHistoricos() {
        pedidoHistoricoStatusService.listarHistoricos();
        verify(pedidoHistoricoStatusRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_ComSucesso() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("historico123");

        when(pedidoHistoricoStatusRepository.findById("historico123")).thenReturn(Optional.of(historico));

        PedidoHistoricoStatus resultado = pedidoHistoricoStatusService.buscarPorId("historico123");

        assertEquals(historico, resultado);
        verify(pedidoHistoricoStatusRepository, times(1)).findById("historico123");
    }

    @Test
    void buscarPorId_HistoricoNaoEncontrado() {
        when(pedidoHistoricoStatusRepository.findById("historicoInexistente")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pedidoHistoricoStatusService.buscarPorId("historicoInexistente"));
        verify(pedidoHistoricoStatusRepository, times(1)).findById("historicoInexistente");
    }
}
