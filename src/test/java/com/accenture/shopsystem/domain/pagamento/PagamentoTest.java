package com.accenture.shopsystem.domain.pagamento;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PagamentoTest {

    @Test
    public void testPagamentoConstructorAndGettersSetters() {
        // Criando um objeto Pagamento usando o construtor
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), 1L, new BigDecimal("200.00"), "PENDENTE", LocalDateTime.now());

        // Verificando se os valores dos atributos estão corretos
        assertNotNull(pagamento.getId());
        assertEquals(1L, pagamento.getPedidoId());
        assertEquals(new BigDecimal("200.00"), pagamento.getValor());
        assertEquals("PENDENTE", pagamento.getStatus());
        assertNotNull(pagamento.getDataPagamento());
    }

    @Test
    public void testPagamentoSettersAndGetters() {
        // Criando um objeto Pagamento
        Pagamento pagamento = new Pagamento();

        // Definindo valores usando setters
        pagamento.setId(UUID.randomUUID());
        pagamento.setPedidoId(1L);
        pagamento.setValor(new BigDecimal("250.00"));
        pagamento.setStatus("PAGO");
        pagamento.setDataPagamento(LocalDateTime.now());

        // Verificando os valores usando getters
        assertNotNull(pagamento.getId());
        assertEquals(1L, pagamento.getPedidoId());
        assertEquals(new BigDecimal("250.00"), pagamento.getValor());
        assertEquals("PAGO", pagamento.getStatus());
        assertNotNull(pagamento.getDataPagamento());
    }
}