package com.accenture.shopsystem.services.pagamento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.accenture.shopsystem.config.RabbitMqConfig;
import com.accenture.shopsystem.domain.pagamento.Pagamento;
import com.accenture.shopsystem.dtos.pagamento.PagamentoDTO;
import com.accenture.shopsystem.dtos.pagamento.PedidoMensagem;
import com.accenture.shopsystem.repositories.PagamentoRepository;

@Service
public class PagamentoService {

	private final PagamentoRepository pagamentoRepository;
	private final RabbitTemplate rabbitTemplate;

	private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

	public PagamentoService(RabbitTemplate rabbitTemplate, PagamentoRepository pagamentoRepository) {
		this.pagamentoRepository = pagamentoRepository;
		this.rabbitTemplate = rabbitTemplate;
		logger.info("PagamentoService inicializado com sucesso.");
	}

	public List<PagamentoDTO> listar() {
		logger.info("Listando todos os pagamentos.");
		return pagamentoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	public Optional<PagamentoDTO> buscarPorId(UUID id) {
		logger.info("Buscando pagamento com ID: {}", id);
		return pagamentoRepository.findById(id).map(this::toDTO);
	}

	public PagamentoDTO criar(PagamentoDTO pagamentoDTO) {
		logger.info("Criando pagamento com dados: {}", pagamentoDTO);
		Pagamento pagamento = toEntity(pagamentoDTO);
		Pagamento salvo = pagamentoRepository.save(pagamento);
		logger.info("Pagamento criado com sucesso, ID: {}", salvo.getId());
		return toDTO(salvo);
	}

	public Optional<PagamentoDTO> atualizar(UUID id, PagamentoDTO pagamentoDTO) {
		logger.info("Atualizando pagamento com ID: {}", id);
		if (pagamentoRepository.existsById(id)) {
			Pagamento pagamento = toEntity(pagamentoDTO);
			Pagamento atualizado = pagamentoRepository.save(pagamento);
			logger.info("Pagamento atualizado com sucesso, ID: {}", id);
			return Optional.of(toDTO(atualizado));
		} else {
			logger.warn("Pagamento com ID: {} não encontrado para atualização.", id);
		}
		return Optional.empty();
	}

	public boolean deletar(UUID id) {
		logger.info("Deletando pagamento com ID: {}", id);
		if (pagamentoRepository.existsById(id)) {
			pagamentoRepository.deleteById(id);
			logger.info("Pagamento com ID: {} deletado com sucesso.", id);
			return true;
		} else {
			logger.warn("Pagamento com ID: {} não encontrado para deletação.", id);
		}
		return false;
	}

	private PagamentoDTO toDTO(Pagamento pagamento) {
		return new PagamentoDTO();
	}

	private Pagamento toEntity(PagamentoDTO pagamentoDTO) {
		return new Pagamento();
	}

	@PostMapping("/processar")
	public ResponseEntity<Map<String, Object>> processarPagamento(@RequestBody PedidoMensagem pedido) {
		logger.info("Iniciando processamento de pagamento para o pedido ID: {}", pedido.getPedidoId());

		pedido.setStatus("PENDENTE");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_PEDIDOS, "pedidos", pedido);

		logger.info("Pedido ID: {} enviado para processamento via RabbitMQ.", pedido.getPedidoId());

		// Construindo uma resposta personalizada
		Map<String, Object> resposta = new HashMap<>();
		resposta.put("mensagem", "Pagamento enviado para processamento.");
		resposta.put("pedidoId", pedido.getPedidoId());
		resposta.put("status", pedido.getStatus());

		return ResponseEntity.ok(resposta);
	}

}
