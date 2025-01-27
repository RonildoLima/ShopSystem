package com.accenture.shopsystem.controllers.pedidoHistoricoStatus;

import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.services.pedidoHistoricoStatus.PedidoHistoricoStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidoHistoricoStatus")
@Tag(name = "Status dos pedidos")
public class PedidoHistoricoStatusController {

    private final PedidoHistoricoStatusService pedidoHistoricoStatusService;

    public PedidoHistoricoStatusController(PedidoHistoricoStatusService pedidoHistoricoStatusService) {
        this.pedidoHistoricoStatusService = pedidoHistoricoStatusService;
    }

    @Operation(summary = "Salvar novo status", description = "Método para salvar um novo histórico de status do pedido")
    @PostMapping
    public ResponseEntity<PedidoHistoricoStatus> salvarHistorico(@RequestBody PedidoHistoricoStatus pedidoHistoricoStatus) {
        try {
            PedidoHistoricoStatus salvo = pedidoHistoricoStatusService.salvarHistorico(pedidoHistoricoStatus);
            return ResponseEntity.ok(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    @Operation(summary = "Listar status", description = "Método para listar todos os históricos de status")
    public ResponseEntity<Iterable<PedidoHistoricoStatus>> listarHistoricos() {
        Iterable<PedidoHistoricoStatus> historicos = pedidoHistoricoStatusService.listarHistoricos();
        return ResponseEntity.ok(historicos);
    }

    @Operation(summary = "Buscar status", description = "Método para buscar um histórico específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoHistoricoStatus> buscarPorId(@PathVariable String id) {
        try {
            PedidoHistoricoStatus historico = pedidoHistoricoStatusService.buscarPorId(id);
            return ResponseEntity.ok(historico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{vendedorId}/pedido/{pedidoId}/atualizar")
    @Operation(summary = "Atualizar status do pedido", description = "Envia o pedido para a fila para atualização do status")
    public ResponseEntity<Void> enviarPedidoParaFila(
            @PathVariable String vendedorId,
            @PathVariable String pedidoId
    ) {
        try {
            pedidoHistoricoStatusService.enviarParaFila(pedidoId, vendedorId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
