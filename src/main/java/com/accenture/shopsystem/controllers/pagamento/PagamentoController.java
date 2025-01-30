package com.accenture.shopsystem.controllers.pagamento;

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

    public PagamentoController(RabbitTemplate rabbitTemplate, PagamentoService pagamentoService) {
        this.rabbitTemplate = rabbitTemplate;
		this.pagamentoService = pagamentoService;
    }
    
    @GetMapping
    public List<PagamentoDTO> listar() {
        return pagamentoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> buscarPorId(@PathVariable UUID id) {
        return pagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> criar(@RequestBody PagamentoDTO pagamentoDTO) {
        PagamentoDTO novoPagamento = pagamentoService.criar(pagamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable UUID id, @RequestBody PagamentoDTO pagamentoDTO) {
        return pagamentoService.atualizar(id, pagamentoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (pagamentoService.deletar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/processar")
    public ResponseEntity<String> processarPagamento(@RequestBody PedidoMensagem pedido) {
        System.out.println("Recebendo pedido: " + pedido);
        pedido.setStatus("PENDENTE");
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_PEDIDOS, "pedidos", pedido);
        System.out.println("Pedido enviado para a fila.");
        return ResponseEntity.ok("Pagamento enviado para processamento.");
    }
}