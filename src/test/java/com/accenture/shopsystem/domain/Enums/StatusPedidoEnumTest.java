package com.accenture.shopsystem.domain.Enums;

import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusPedidoEnumTest {

    @Test
    void values() {
        StatusPedidoEnum[] expectedValues = StatusPedidoEnum.values();
        assertArrayEquals(new StatusPedidoEnum[]{
                StatusPedidoEnum.PENDENTE,
                StatusPedidoEnum.PAGO,
                StatusPedidoEnum.CANCELADO,
                StatusPedidoEnum.ENVIADO,
                StatusPedidoEnum.CONCLUIDO
        }, expectedValues);
    }

    @Test
    void valueOf() {
        assertEquals(StatusPedidoEnum.PENDENTE, StatusPedidoEnum.valueOf("PENDENTE"));
        assertEquals(StatusPedidoEnum.PAGO, StatusPedidoEnum.valueOf("PAGO"));
        assertEquals(StatusPedidoEnum.CANCELADO, StatusPedidoEnum.valueOf("CANCELADO"));
        assertEquals(StatusPedidoEnum.ENVIADO, StatusPedidoEnum.valueOf("ENVIADO"));
        assertEquals(StatusPedidoEnum.CONCLUIDO, StatusPedidoEnum.valueOf("CONCLUIDO"));
    }

    @Test
    void valueOf_InvalidValue_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> StatusPedidoEnum.valueOf("INVALIDO"));
    }
}
