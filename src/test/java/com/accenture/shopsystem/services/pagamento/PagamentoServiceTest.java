package com.accenture.shopsystem.services.pagamento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.accenture.shopsystem.domain.pagamento.Pagamento;
import com.accenture.shopsystem.dtos.pagamento.PagamentoDTO;
import com.accenture.shopsystem.dtos.pagamento.PedidoMensagem;
import com.accenture.shopsystem.repositories.PagamentoRepository;

public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PagamentoService pagamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
    }

    @Test
    public void deveListarPagamentos() {
        // Cenário
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), 1L, new BigDecimal("100.00"), "PENDENTE", LocalDateTime.now());
        when(pagamentoRepository.findAll()).thenReturn(List.of(pagamento));

        // Ação
        List<PagamentoDTO> pagamentos = pagamentoService.listar();

        // Verificação
        assertNotNull(pagamentos);
        assertEquals(1, pagamentos.size());
        verify(pagamentoRepository, times(1)).findAll();
    }

    @Test
    public void deveBuscarPagamentoPorId() {
        // Cenário
    	UUID id = UUID.randomUUID();
        Long pedidoID = 1L;
        
        Pagamento pagamento = new Pagamento(UUID.randomUUID(), pedidoID, new BigDecimal("100.00"), "PENDENTE", LocalDateTime.now());
        when(pagamentoRepository.findById(id)).thenReturn(Optional.of(pagamento));

        // Ação
        Optional<PagamentoDTO> pagamentoDTO = pagamentoService.buscarPorId(id);

        // Verificação
        assertTrue(pagamentoDTO.isPresent());
        verify(pagamentoRepository, times(1)).findById(id);
    }

    @Test
    public void deveCriarPagamento() {
        // Cenário
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setPedidoId(1L);
        pagamentoDTO.setValor(new BigDecimal("100.00"));
        pagamentoDTO.setStatus("PENDENTE");

        Pagamento pagamento = new Pagamento();
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        // Ação
        PagamentoDTO pagamentoCriado = pagamentoService.criar(pagamentoDTO);

        // Verificação
        assertNotNull(pagamentoCriado);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    public void deveDeletarPagamento() {
        // Cenário
    	UUID id = UUID.randomUUID();

        when(pagamentoRepository.existsById(id)).thenReturn(true);

        // Ação
        boolean deletado = pagamentoService.deletar(id);

        // Verificação
        assertTrue(deletado);
        verify(pagamentoRepository, times(1)).existsById(id);
        verify(pagamentoRepository, times(1)).deleteById(id);
    }

    @Test
    public void deveProcessarPagamento() {
        // Cenário
        PedidoMensagem pedido = new PedidoMensagem();
        pedido.setPedidoId(1L);
        pedido.setStatus("PENDENTE");

        // Ação
        pagamentoService.processarPagamento(pedido);

        // Verificação
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), eq(pedido));
    }
}