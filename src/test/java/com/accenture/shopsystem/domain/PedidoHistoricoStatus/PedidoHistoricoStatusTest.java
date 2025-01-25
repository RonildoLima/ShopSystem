package com.accenture.shopsystem.domain.PedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PedidoHistoricoStatusTest {

    @Test
    void getId() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("123");
        assertEquals("123", historico.getId());
    }

    @Test
    void getPedido() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        Pedido pedido = new Pedido();
        pedido.setId("pedido1");
        historico.setPedido(pedido);
        assertEquals(pedido, historico.getPedido());
    }

    @Test
    void getStatusPedido() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setStatusPedido(StatusPedidoEnum.PENDENTE);
        assertEquals(StatusPedidoEnum.PENDENTE, historico.getStatusPedido());
    }

    @Test
    void getDataHoraStatusPedido() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        LocalDateTime dataHora = LocalDateTime.of(2025, 1, 1, 12, 0);
        historico.setDataHoraStatusPedido(dataHora);
        assertEquals(dataHora, historico.getDataHoraStatusPedido());
    }

    @Test
    void getDataHoraPagamento() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        LocalDateTime dataHoraPagamento = LocalDateTime.of(2025, 1, 2, 12, 0);
        historico.setDataHoraPagamento(dataHoraPagamento);
        assertEquals(dataHoraPagamento, historico.getDataHoraPagamento());
    }

    @Test
    void setId() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("456");
        assertEquals("456", historico.getId());
    }

    @Test
    void setPedido() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        Pedido pedido = new Pedido();
        pedido.setId("pedido2");
        historico.setPedido(pedido);
        assertEquals(pedido, historico.getPedido());
    }

    @Test
    void setStatusPedido() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setStatusPedido(StatusPedidoEnum.PAGO);
        assertEquals(StatusPedidoEnum.PAGO, historico.getStatusPedido());
    }

    @Test
    void setDataHoraStatusPedido() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        LocalDateTime dataHora = LocalDateTime.of(2025, 1, 3, 12, 0);
        historico.setDataHoraStatusPedido(dataHora);
        assertEquals(dataHora, historico.getDataHoraStatusPedido());
    }

    @Test
    void setDataHoraPagamento() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        LocalDateTime dataHoraPagamento = LocalDateTime.of(2025, 1, 4, 12, 0);
        historico.setDataHoraPagamento(dataHoraPagamento);
        assertEquals(dataHoraPagamento, historico.getDataHoraPagamento());
    }

    @Test
    void testEquals() {
        PedidoHistoricoStatus historico1 = new PedidoHistoricoStatus();
        historico1.setId("1");

        PedidoHistoricoStatus historico2 = new PedidoHistoricoStatus();
        historico2.setId("1");

        assertEquals(historico1, historico2);

        PedidoHistoricoStatus historico3 = new PedidoHistoricoStatus();
        historico3.setId("2");

        assertNotEquals(historico1, historico3);
    }

    @Test
    void canEqual() {
        PedidoHistoricoStatus historico1 = new PedidoHistoricoStatus();
        PedidoHistoricoStatus historico2 = new PedidoHistoricoStatus();
        assertTrue(historico1.canEqual(historico2));
    }

    @Test
    void testHashCode() {
        PedidoHistoricoStatus historico1 = new PedidoHistoricoStatus();
        historico1.setId("1");

        PedidoHistoricoStatus historico2 = new PedidoHistoricoStatus();
        historico2.setId("1");

        assertEquals(historico1.hashCode(), historico2.hashCode());

        PedidoHistoricoStatus historico3 = new PedidoHistoricoStatus();
        historico3.setId("2");

        assertNotEquals(historico1.hashCode(), historico3.hashCode());
    }

    @Test
    void testToString() {
        PedidoHistoricoStatus historico = new PedidoHistoricoStatus();
        historico.setId("123");
        historico.setStatusPedido(StatusPedidoEnum.PENDENTE);
        LocalDateTime dataHora = LocalDateTime.of(2025, 1, 5, 14, 0);
        historico.setDataHoraStatusPedido(dataHora);

        String expected = "PedidoHistoricoStatus{id='123', statusPedido=PENDENTE, dataHoraStatusPedido=2025-01-05T14:00}";
        assertEquals(expected, historico.toString());
    }
}
