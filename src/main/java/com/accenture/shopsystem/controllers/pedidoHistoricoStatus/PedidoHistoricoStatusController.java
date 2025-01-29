package com.accenture.shopsystem.controllers.pedidoHistoricoStatus;

import com.accenture.shopsystem.exceptions.ShopSystemExceptions;
import com.accenture.shopsystem.services.pedidoHistoricoStatus.PedidoHistoricoStatusService;
import com.accenture.shopsystem.repositories.VendedorRepository;
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
    private final VendedorRepository vendedorRepository;

    public PedidoHistoricoStatusController(PedidoHistoricoStatusService pedidoHistoricoStatusService, VendedorRepository vendedorRepository) {
        this.pedidoHistoricoStatusService = pedidoHistoricoStatusService;
        this.vendedorRepository = vendedorRepository;
    }

    @Operation(summary = "Processar ou cancelar pedido", description = "Atualiza o status do pedido com base no ID do vendedor autenticado")
    @PostMapping("/processar")
    public void processarPedido(@RequestParam String pedidoId, @RequestParam String acao) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;
        if (authentication.getPrincipal() instanceof DefaultOAuth2User user) {
            email = (String) user.getAttributes().get("email");
        } else {
            email = authentication.getName();
        }

        String vendedorId = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new ShopSystemExceptions.VendedorNaoEncontradoException("Vendedor não encontrado para o email: " + email))
                .getId();

        if (!acao.equalsIgnoreCase("processar") && !acao.equalsIgnoreCase("cancelar")) {
            throw new ShopSystemExceptions.AcaoInvalidaException("Ação inválida: " + acao);
        }

        // Relançar a exceção sem capturá-la e modificar seu tipo
        pedidoHistoricoStatusService.processarPedido(pedidoId, vendedorId, acao);
    }

}
