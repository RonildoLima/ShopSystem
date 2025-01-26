package com.accenture.shopsystem.dtos.pagamentos;

import org.junit.jupiter.api.Test;

import com.accenture.shopsystem.dtos.pagamento.PedidoMensagem;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class PedidoMensagemTest {

    @Test
    public void testPedidoMensagem() {
        // Criando uma instância de PedidoMensagem
        PedidoMensagem pedido = new PedidoMensagem();
        
        // Definindo valores para os atributos
        pedido.setPedidoId(1L);
        pedido.setCliente("Carlos");
        pedido.setProdutosIds(Arrays.asList(1L, 2L, 3L));
        pedido.setValorTotal(200.00);
        pedido.setStatus("PENDENTE");

        // Verificando se os valores são definidos corretamente
        assertEquals(1L, pedido.getPedidoId());
        assertEquals("Carlos", pedido.getCliente());
        assertEquals(Arrays.asList(1L, 2L, 3L), pedido.getProdutosIds());
        assertEquals(200.00, pedido.getValorTotal());
        assertEquals("PENDENTE", pedido.getStatus());
    }
}