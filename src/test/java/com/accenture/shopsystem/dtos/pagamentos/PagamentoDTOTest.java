package com.accenture.shopsystem.dtos.pagamentos;

import org.junit.jupiter.api.Test;

import com.accenture.shopsystem.dtos.pagamento.PagamentoDTO;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoDTOTest {

    @Test
    public void testPagamentoDTO() {
        // Criando a instância de PagamentoDTO
        PagamentoDTO pagamento = new PagamentoDTO();

        // Definindo valores para os atributos
        pagamento.setPedidoId(1L);
        pagamento.setValor(new BigDecimal("100.50"));
        pagamento.setStatus("PENDENTE");
        pagamento.setDataPagamento(LocalDateTime.now());

        // Verificando se os valores são definidos corretamente
        assertEquals(1L, pagamento.getPedidoId());
        assertEquals(new BigDecimal("100.50"), pagamento.getValor());
        assertEquals("PENDENTE", pagamento.getStatus());
        assertNotNull(pagamento.getDataPagamento());
    }
    
}