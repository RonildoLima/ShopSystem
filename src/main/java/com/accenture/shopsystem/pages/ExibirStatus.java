package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoHistoricoStatusRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ExibirStatus {

    private final PedidoHistoricoStatusRepository historicoStatusRepository;
    private final VendedorRepository vendedorRepository;

    public ExibirStatus(PedidoHistoricoStatusRepository historicoStatusRepository, VendedorRepository vendedorRepository) {
        this.historicoStatusRepository = historicoStatusRepository;
        this.vendedorRepository = vendedorRepository;
    }

    @GetMapping("/statusPedidos")
    public String exibirStatusPedidos(Model model) {
        // Obter a autenticação atual do usuário
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = (String) oauth2User.getAttribute("email");
        } else {
            email = authentication.getName();
        }

        // Buscar o vendedor pelo email
        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        // Buscar o histórico de status dos pedidos do vendedor
        List<PedidoHistoricoStatus> historicoStatus = historicoStatusRepository.findByPedidoVendedorId(vendedor.getId());

        // Log detalhado do conteúdo do modelo
        System.out.println("==== Conteúdo do Modelo ====");
        historicoStatus.forEach(status -> {
            System.out.println("Pedido ID: " + (status.getPedido() != null ? status.getPedido().getId() : "null"));
            System.out.println("Descrição do Pedido: " + (status.getPedido() != null ? status.getPedido().getPedidoDescricao() : "null"));
            System.out.println("Status: " + status.getStatusPedido());
            System.out.println("Data/Hora do Status: " + status.getDataHoraStatusPedido());
            System.out.println("Data/Hora do Pagamento: " + (status.getDataHoraPagamento() != null ? status.getDataHoraPagamento() : "Não disponível"));
            System.out.println("-----------------------------------");
        });

        // Adicionar os dados no modelo para o template
        model.addAttribute("historicoStatus", historicoStatus);

        return "exibirStatus";
    }
}
