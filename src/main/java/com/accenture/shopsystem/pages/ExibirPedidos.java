package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.PedidoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ExibirPedidos {

    private final PedidoRepository pedidoRepository;
    private final VendedorRepository vendedorRepository;

    public ExibirPedidos(PedidoRepository pedidoRepository, VendedorRepository vendedorRepository) {
        this.pedidoRepository = pedidoRepository;
        this.vendedorRepository = vendedorRepository;
    }

    @GetMapping("/exibirPedidos")
    public String exibirPedidos(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = (String) oauth2User.getAttribute("email");
        } else {
            email = authentication.getName();
        }

        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        List<Pedido> pedidos = pedidoRepository.findPedidosByVendedorIdWithProdutos(vendedor.getId());

        // Log detalhado do conteúdo do Model
        System.out.println("==== Conteúdo do Model ====");
        System.out.println("Pedidos:");
        pedidos.forEach(pedido -> {
            System.out.println("Pedido ID: " + pedido.getId());
            System.out.println("Descrição: " + pedido.getPedidoDescricao());
            System.out.println("Data: " + pedido.getPedidoDataHora());
            System.out.println("Quantidade: " + pedido.getPedidoQuantidade());
            System.out.println("Valor: " + pedido.getPedidoValor());
            if (pedido.getProdutos() != null) {
                pedido.getProdutos().forEach(produto -> {
                    System.out.println(" - Produto: " +
                            (produto.getProduto() != null ? produto.getProduto().getProdutoDescricao() : "null"));
                    System.out.println(" - Quantidade: " + produto.getQuantidade());
                    System.out.println(" - Preço Unitário: " + produto.getPrecoUnitario());
                });
            } else {
                System.out.println(" - Sem produtos associados");
            }
        });
        System.out.println("===========================");

        model.addAttribute("pedidos", pedidos);
        return "exibirPedidos";
    }
}
