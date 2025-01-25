package com.accenture.shopsystem.pages;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ExibirProdutos {

    private final ProdutoRepository produtoRepository;
    private final VendedorRepository vendedorRepository;

    public ExibirProdutos(ProdutoRepository produtoRepository, VendedorRepository vendedorRepository) {
        this.produtoRepository = produtoRepository;
        this.vendedorRepository = vendedorRepository;
    }

    @GetMapping("/exibirProdutos")
    public String exibirDashboard(Model model) {
        // Obter a autenticação atual do usuário
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Inicializa o email
        String email;

        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
            // Autenticado via Google (OAuth2)
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = (String) oauth2User.getAttribute("email"); // Obtém o email do Google
        } else {
            // Autenticado normalmente
            email = authentication.getName(); // Nome padrão do usuário (email para autenticação básica)
        }

        // Buscar o vendedor pelo email
        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        // Buscar produtos associados ao vendedor
        List<Produto> produtos = produtoRepository.findByVendedorId(vendedor.getId());

        // Adicionar os produtos no modelo para exibição na página
        model.addAttribute("produtos", produtos);

        // Retornar o template Thymeleaf
        return "exibirProdutos";
    }

}
