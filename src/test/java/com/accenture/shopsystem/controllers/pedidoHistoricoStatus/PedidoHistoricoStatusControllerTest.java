package com.accenture.shopsystem.controllers.pedidoHistoricoStatus;

import com.accenture.shopsystem.controllers.pedidoHistoricoStatus.PedidoHistoricoStatusController;
import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoHistoricoStatusControllerTest {

    @Test
    void salvarHistorico() {
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController();
        controller.pedidoHistoricoStatusRepository = historicoRepository;
        controller.pedidoRepository = pedidoRepository;

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);

        when(pedidoRepository.findById("pedido1")).thenReturn(Optional.of(pedido));
        when(historicoRepository.save(historico)).thenReturn(historico);

        ResponseEntity<PedidoHistoricoStatus> response = controller.salvarHistorico(historico);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(historico, response.getBody());
        verify(historicoRepository, times(1)).save(historico);
    }

    @Test
    void salvarHistorico_PedidoNaoEncontrado() {
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController();
        controller.pedidoHistoricoStatusRepository = historicoRepository;
        controller.pedidoRepository = pedidoRepository;

        Pedido pedido = new Pedido();
        pedido.setId("pedido1");

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setPedido(pedido);

        when(pedidoRepository.findById("pedido1")).thenReturn(Optional.empty());

        ResponseEntity<PedidoHistoricoStatus> response = controller.salvarHistorico(historico);

        assertEquals(400, response.getStatusCodeValue());
        verify(historicoRepository, never()).save(any());
    }

    @Test
    void listarHistoricos() {
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController();
        controller.pedidoHistoricoStatusRepository = historicoRepository;
        controller.pedidoRepository = pedidoRepository;

        PedidoHistoricoStatus historico1 = new PedidoHistoricoStatus();
        PedidoHistoricoStatus historico2 = new PedidoHistoricoStatus();

        when(historicoRepository.findAll()).thenReturn(java.util.List.of(historico1, historico2));

        ResponseEntity<Iterable<PedidoHistoricoStatus>> response = controller.listarHistoricos();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().iterator().hasNext());
        verify(historicoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId() {
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController();
        controller.pedidoHistoricoStatusRepository = historicoRepository;
        controller.pedidoRepository = pedidoRepository;

        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("historico1");

        when(historicoRepository.findById("historico1")).thenReturn(Optional.of(historico));

        ResponseEntity<PedidoHistoricoStatus> response = controller.buscarPorId("historico1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(historico, response.getBody());
        verify(historicoRepository, times(1)).findById("historico1");
    }

    @Test
    void buscarPorId_NotFound() {
        PedidoHistoricoStatusRepository historicoRepository = Mockito.mock(PedidoHistoricoStatusRepository.class);
        PedidoRepository pedidoRepository = Mockito.mock(PedidoRepository.class);
        PedidoHistoricoStatusController controller = new PedidoHistoricoStatusController();
        controller.pedidoHistoricoStatusRepository = historicoRepository;
        controller.pedidoRepository = pedidoRepository;

        when(historicoRepository.findById("historico1")).thenReturn(Optional.empty());

        ResponseEntity<PedidoHistoricoStatus> response = controller.buscarPorId("historico1");

        assertEquals(404, response.getStatusCodeValue());
        verify(historicoRepository, times(1)).findById("historico1");
    }
}
