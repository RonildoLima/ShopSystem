package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.services.pedidoHistoricoStatus.PedidoHistoricoStatusService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ExibirProcessamentoTest {

    @Test
    void exibirFormulario() {
        PedidoHistoricoStatusService pedidoHistoricoStatusService = mock(PedidoHistoricoStatusService.class);
        ExibirProcessamento exibirProcessamento = new ExibirProcessamento(pedidoHistoricoStatusService);

        String viewName = exibirProcessamento.exibirFormulario();

        assertEquals("processarPedido", viewName);
    }
}
