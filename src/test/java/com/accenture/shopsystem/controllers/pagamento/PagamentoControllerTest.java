package com.accenture.shopsystem.controllers.pagamento;

import com.accenture.shopsystem.dtos.pagamento.PagamentoDTO;
import com.accenture.shopsystem.services.pagamento.PagamentoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private PagamentoController pagamentoController;

    private PagamentoDTO pagamentoDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pagamentoController).build();

        pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setPedidoId(1L);
        pagamentoDTO.setValor(BigDecimal.valueOf(100.00));
        pagamentoDTO.setStatus("PENDENTE");
        pagamentoDTO.setDataPagamento(LocalDateTime.now());
    }
    
    @Test
    public void testCriarPagamento() throws Exception {
        when(pagamentoService.criar(any(PagamentoDTO.class))).thenReturn(pagamentoDTO);

        mockMvc.perform(post("/pagamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pedidoId\":1, \"valor\":100.0, \"status\":\"PENDENTE\", \"dataPagamento\":\"2025-01-26T12:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pedidoId").value(pagamentoDTO.getPedidoId()))
                .andExpect(jsonPath("$.valor").value(pagamentoDTO.getValor().doubleValue()))
                .andExpect(jsonPath("$.status").value(pagamentoDTO.getStatus()));
    }
    
	@Test
    public void testBuscarPorIdNotFound() throws Exception {
		UUID id = UUID.randomUUID();
		
        when(pagamentoService.buscarPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pagamentos/" + id))
                .andExpect(status().isNotFound());
    }
	
	@Test
    public void testListarPagamentos() throws Exception {
        when(pagamentoService.listar()).thenReturn(java.util.List.of(pagamentoDTO));

        mockMvc.perform(get("/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pedidoId").value(pagamentoDTO.getPedidoId()))
                .andExpect(jsonPath("$[0].valor").value(pagamentoDTO.getValor().doubleValue()))
                .andExpect(jsonPath("$[0].status").value(pagamentoDTO.getStatus()));
    }

    @Test
    public void testDeletarPagamento() throws Exception {
		UUID id = UUID.randomUUID();
    	
        when(pagamentoService.deletar(id)).thenReturn(true);

        mockMvc.perform(delete("/pagamentos/" + id))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void testDeletarPagamentoNotFound() throws Exception {
		UUID id = UUID.randomUUID();
		
        when(pagamentoService.deletar(id)).thenReturn(false);

        mockMvc.perform(delete("/pagamentos/" + id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testAtualizarPagamentoNaoEncontrado() throws Exception {
        mockMvc.perform(put("/pagamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pedidoId\":1, \"valor\":100.00, \"status\":\"PENDENTE\"}"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCriarPagamentoComDadosValidos() throws Exception {
        // Supondo que o pagamentoDTO tenha sido previamente configurado
        mockMvc.perform(post("/pagamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pedidoId\":1, \"valor\":100.00, \"status\":\"PENDENTE\"}"))
                .andExpect(status().isCreated());
    }

}