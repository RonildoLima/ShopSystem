package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.services.pedidoHistoricoStatus.PedidoHistoricoStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidoHistoricoStatus")
@Tag(name = "Status dos pedidos")
public class ExibirProcessamento {

    private final PedidoHistoricoStatusService pedidoHistoricoStatusService;

    public ExibirProcessamento(PedidoHistoricoStatusService pedidoHistoricoStatusService) {
        this.pedidoHistoricoStatusService = pedidoHistoricoStatusService;
    }

    @GetMapping("/gerenciar")
    @Operation(summary = "Exibir formulário para processar ou cancelar pedidos")
    public String exibirFormulario() {
        return "processarPedido"; // Nome do template sem a extensão ".html"
    }
}

