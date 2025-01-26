package com.accenture.shopsystem.services.pagamento;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.accenture.shopsystem.config.RabbitMQConfigPagamento;
import com.accenture.shopsystem.domain.pagamento.Pagamento;
import com.accenture.shopsystem.dtos.pagamento.PagamentoDTO;
import com.accenture.shopsystem.dtos.pagamento.PedidoMensagem;
import com.accenture.shopsystem.repositories.PagamentoRepository;

@Service
public class PagamentoService {
	
	private final PagamentoRepository pagamentoRepository;
    private final RabbitTemplate rabbitTemplate;

    public PagamentoService(RabbitTemplate rabbitTemplate, PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
		this.rabbitTemplate = rabbitTemplate;
    }
    
    public List<PagamentoDTO> listar() {
        return pagamentoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PagamentoDTO> buscarPorId(Long id) {
        return pagamentoRepository.findById(id).map(this::toDTO);
    }

    public PagamentoDTO criar(PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = toEntity(pagamentoDTO);
        Pagamento salvo = pagamentoRepository.save(pagamento);
        return toDTO(salvo);
    }

    public Optional<PagamentoDTO> atualizar(Long id, PagamentoDTO pagamentoDTO) {
        if (pagamentoRepository.existsById(id)) {
            Pagamento pagamento = toEntity(pagamentoDTO);
            Pagamento atualizado = pagamentoRepository.save(pagamento);
            return Optional.of(toDTO(atualizado));
        }
        return Optional.empty();
    }

    public boolean deletar(Long id) {
        if (pagamentoRepository.existsById(id)) {
            pagamentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PagamentoDTO toDTO(Pagamento pagamento) {
        return new PagamentoDTO(
        );
    }

    private Pagamento toEntity(PagamentoDTO pagamentoDTO) {
        return new Pagamento(
        );
    }


    @PostMapping("/processar")
    public ResponseEntity<Map<String, Object>> processarPagamento(@RequestBody PedidoMensagem pedido) {
        pedido.setStatus("PENDENTE");
        rabbitTemplate.convertAndSend(RabbitMQConfigPagamento.EXCHANGE_PEDIDOS, "pedidos", pedido);

        // Construindo uma resposta personalizada
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Pagamento enviado para processamento.");
        resposta.put("pedidoId", pedido.getPedidoId());
        resposta.put("status", pedido.getStatus());

        return ResponseEntity.ok(resposta);
    }

}