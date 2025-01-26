package com.accenture.shopsystem.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.shopsystem.domain.pagamento.Pagamento;

@SpringBootTest
@Transactional // Garante rollback automático após cada teste
public class PagamentoRepositoryTest {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Test
    public void deveSalvarPagamentoNoBanco() {
        // Criar um objeto Pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(123L);
        pagamento.setValor(new BigDecimal("500.00"));
        pagamento.setStatus("PENDENTE");
        pagamento.setDataPagamento(LocalDateTime.now());

        // Salvar no banco
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        // Verificar se foi salvo corretamente
        assertNotNull(pagamentoSalvo.getId()); // O ID deve ser gerado
        assertEquals(123L, pagamentoSalvo.getPedidoId());
        assertEquals(new BigDecimal("500.00"), pagamentoSalvo.getValor());
        assertEquals("PENDENTE", pagamentoSalvo.getStatus());
    }
}
