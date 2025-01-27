package com.accenture.shopsystem.controllers.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.services.pedidoHistoricoStatus.PedidoHistoricoStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoHistoricoStatusControllerTest {

    @Test
    void salvarHistorico() {
        PedidoHistoricoStatusService service = mock(PedidoHistoricoStatusService.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController(service);

        Pedido pedido = new Pedido();
        pedido.setId("1");

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);
        historico.setDataHoraStatusPedido(LocalDateTime.now());

        when(service.salvarHistorico(any(PedidoHistoricoStatus.class))).thenReturn(historico);

        ResponseEntity<PedidoHistoricoStatus> response = controller.salvarHistorico(historico);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(historico, response.getBody());
        verify(service, times(1)).salvarHistorico(historico);
    }

    @Test
    void salvarHistorico_PedidoNaoEncontrado() {
        PedidoHistoricoStatusService service = mock(PedidoHistoricoStatusService.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController(service);

        Pedido pedido = new Pedido();
        pedido.setId("1");

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);

        when(service.salvarHistorico(any(PedidoHistoricoStatus.class)))
                .thenThrow(new IllegalArgumentException("Pedido não encontrado"));

        ResponseEntity<PedidoHistoricoStatus> response = controller.salvarHistorico(historico);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(service, times(1)).salvarHistorico(historico);
    }

    @Test
    void listarHistoricos() {
        PedidoHistoricoStatusService service = mock(PedidoHistoricoStatusService.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController(service);

        Iterable<PedidoHistoricoStatus> historicos = mock(Iterable.class);
        when(service.listarHistoricos()).thenReturn(historicos);

        ResponseEntity<Iterable<PedidoHistoricoStatus>> response = controller.listarHistoricos();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(historicos, response.getBody());
        verify(service, times(1)).listarHistoricos();
    }

    @Test
    void buscarPorId() {
        PedidoHistoricoStatusService service = mock(PedidoHistoricoStatusService.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController(service);

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("1");

        when(service.buscarPorId("1")).thenReturn(historico);

        ResponseEntity<PedidoHistoricoStatus> response = controller.buscarPorId("1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(historico, response.getBody());
        verify(service, times(1)).buscarPorId("1");
    }

    @Test
    void buscarPorId_NotFound() {
        PedidoHistoricoStatusService service = mock(PedidoHistoricoStatusService.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController(service);

        when(service.buscarPorId("1")).thenThrow(new IllegalArgumentException("Histórico não encontrado"));

        ResponseEntity<PedidoHistoricoStatus> response = controller.buscarPorId("1");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(service, times(1)).buscarPorId("1");
    }
}