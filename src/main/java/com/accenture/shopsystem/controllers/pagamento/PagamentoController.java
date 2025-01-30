package com.accenture.shopsystem.controllers.pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.shopsystem.config.RabbitMqConfig;
import com.accenture.shopsystem.dtos.pagamento.PagamentoDTO;
import com.accenture.shopsystem.dtos.pagamento.PedidoMensagem;
import com.accenture.shopsystem.services.pagamento.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
	
    private final RabbitTemplate rabbitTemplate;
    private final PagamentoService pagamentoService;
    
    private static final Logger logger = LoggerFactory.getLogger(PagamentoController.class);


    public PagamentoController(RabbitTemplate rabbitTemplate, PagamentoService pagamentoService) {
        this.rabbitTemplate = rabbitTemplate;
		this.pagamentoService = pagamentoService;
    }
    
    @GetMapping
    public List<PagamentoDTO> listar() {
    	logger.info("Iniciando listagem de pagamentos.");
        List<PagamentoDTO> pagamentos = pagamentoService.listar();
        logger.info("Listagem concluída. Total de pagamentos encontrados: {}", pagamentos.size());
        return pagamentos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> buscarPorId(@PathVariable UUID id) {
    	logger.info("Buscando pagamento pelo ID: {}", id);
        return pagamentoService.buscarPorId(id)
                .map(pagamento -> {
                    logger.info("Pagamento encontrado: {}", pagamento);
                    return ResponseEntity.ok(pagamento);
                })
                .orElseGet(() -> {
                    logger.warn("Pagamento com ID {} não encontrado.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> criar(@RequestBody PagamentoDTO pagamentoDTO) {
    	logger.info("Criando um novo pagamento: {}", pagamentoDTO);
        PagamentoDTO novoPagamento = pagamentoService.criar(pagamentoDTO);
        logger.info("Pagamento criado com sucesso: {}", novoPagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable UUID id, @RequestBody PagamentoDTO pagamentoDTO) {
    	logger.info("Atualizando pagamento com ID: {}. Dados: {}", id, pagamentoDTO);
        return pagamentoService.atualizar(id, pagamentoDTO)
                .map(pagamento -> {
                    logger.info("Pagamento atualizado com sucesso: {}", pagamento);
                    return ResponseEntity.ok(pagamento);
                })
                .orElseGet(() -> {
                    logger.warn("Não foi possível atualizar. Pagamento com ID {} não encontrado.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
    	logger.info("Tentando deletar pagamento com ID: {}", id);
        if (pagamentoService.deletar(id)) {
            logger.info("Pagamento com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Pagamento com ID {} não encontrado. Não foi possível deletar.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/processar")
    public ResponseEntity<String> processarPagamento(@RequestBody PedidoMensagem pedido) {
    	logger.info("Recebendo pedido para processamento: {}", pedido);
        pedido.setStatus("PENDENTE");
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_PEDIDOS, "pedidos", pedido);
        logger.info("Pedido enviado para a fila de processamento. Pedido: {}", pedido);
        return ResponseEntity.ok("Pagamento enviado para processamento.");
    }
}