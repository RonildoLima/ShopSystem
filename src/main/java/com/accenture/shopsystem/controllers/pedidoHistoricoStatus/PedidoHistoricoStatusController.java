package com.accenture.shopsystem.controllers.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/pedidoHistoricoStatus")
@Tag(name = "Status dos pedidos")
public class PedidoHistoricoStatusController {

    @Autowired
    PedidoHistoricoStatusRepository pedidoHistoricoStatusRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Operation(summary = "Salvar novo status", description = "Método para salvar um novo histórico de status do pedido")
    @PostMapping
    public ResponseEntity<PedidoHistoricoStatus> salvarHistorico(@RequestBody PedidoHistoricoStatus pedidoHistoricoStatus) {
        Optional<Pedido> pedido = pedidoRepository.findById(pedidoHistoricoStatus.getPedido().getId());
        if (pedido.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (pedidoHistoricoStatus.getDataHoraStatusPedido() == null) {
            pedidoHistoricoStatus.setDataHoraStatusPedido(LocalDateTime.now());
        }

        PedidoHistoricoStatus salvo = pedidoHistoricoStatusRepository.save(pedidoHistoricoStatus);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    @Operation(summary = "Listar status", description = "Método para listar todos os históricos de status")
    public ResponseEntity<Iterable<PedidoHistoricoStatus>> listarHistoricos() {
        Iterable<PedidoHistoricoStatus> historicos = pedidoHistoricoStatusRepository.findAll();
        return ResponseEntity.ok(historicos);
    }

    @Operation(summary = "Buscar status", description = "Método para buscar um histórico específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoHistoricoStatus> buscarPorId(@PathVariable String id) {
        Optional<PedidoHistoricoStatus> historico = pedidoHistoricoStatusRepository.findById(id);
        return historico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
