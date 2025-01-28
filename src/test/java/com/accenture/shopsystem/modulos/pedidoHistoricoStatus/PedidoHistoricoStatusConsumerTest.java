package com.accenture.shopsystem.modulos.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoHistoricoStatusConsumerTest {

    @Mock
    private PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    @InjectMocks
    private PedidoHistoricoStatusConsumer pedidoHistoricoStatusConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processarHistoricoStatus_AtualizarParaPago() {
        PedidoHistoricoStatus recebido = new PedidoHistoricoStatus();
        recebido.setId("historico123");
        recebido.setStatusPedido(StatusPedidoEnum.PAGO);

        PedidoHistoricoStatus existente = new PedidoHistoricoStatus();
        existente.setId("historico123");
        existente.setStatusPedido(StatusPedidoEnum.PENDENTE);

        when(pedidoHistoricoStatusRepository.findById("historico123")).thenReturn(Optional.of(existente));
        when(pedidoHistoricoStatusRepository.save(any(PedidoHistoricoStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));

        pedidoHistoricoStatusConsumer.processarHistoricoStatus(recebido);

        verify(pedidoHistoricoStatusRepository, times(1)).findById("historico123");
        verify(pedidoHistoricoStatusRepository, times(1)).save(existente);

        assertEquals(StatusPedidoEnum.PAGO, existente.getStatusPedido());
        assertNotNull(existente.getDataHoraPagamento());
    }

    @Test
    void processarHistoricoStatus_AtualizarParaCancelado() {
        PedidoHistoricoStatus recebido = new PedidoHistoricoStatus();
        recebido.setId("historico123");
        recebido.setStatusPedido(StatusPedidoEnum.CANCELADO);

        PedidoHistoricoStatus existente = new PedidoHistoricoStatus();
        existente.setId("historico123");
        existente.setStatusPedido(StatusPedidoEnum.PENDENTE);

        when(pedidoHistoricoStatusRepository.findById("historico123")).thenReturn(Optional.of(existente));
        when(pedidoHistoricoStatusRepository.save(any(PedidoHistoricoStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));

        pedidoHistoricoStatusConsumer.processarHistoricoStatus(recebido);

        verify(pedidoHistoricoStatusRepository, times(1)).findById("historico123");
        verify(pedidoHistoricoStatusRepository, times(1)).save(existente);

        assertEquals(StatusPedidoEnum.CANCELADO, existente.getStatusPedido());
        assertNull(existente.getDataHoraPagamento());
    }

    @Test
    void processarHistoricoStatus_HistoricoNaoEncontrado() {
        PedidoHistoricoStatus recebido = new PedidoHistoricoStatus();
        recebido.setId("historicoInexistente");

        when(pedidoHistoricoStatusRepository.findById("historicoInexistente")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoHistoricoStatusConsumer.processarHistoricoStatus(recebido);
        });

        assertEquals("Histórico não encontrado para o ID: historicoInexistente", exception.getMessage());
        verify(pedidoHistoricoStatusRepository, never()).save(any(PedidoHistoricoStatus.class));
    }
}
