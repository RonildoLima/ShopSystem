package com.accenture.shopsystem.controllers;

import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.VendedorRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Tag(name = "Autenticação do google")
public class OAuth2LoginController {

    private final VendedorRepository vendedorRepository;

    public OAuth2LoginController(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @GetMapping("/oauth2/success")
    public RedirectView loginComGoogle(@AuthenticationPrincipal OAuth2User principal) {
        // Obtém o nome e o e-mail do usuário autenticado
        String nome = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        // Verifica se o vendedor já existe no banco
        if (vendedorRepository.findByEmail(email).isEmpty()) {
            // Cria um novo vendedor com setor padrão
            Vendedor vendedor = new Vendedor();
            vendedor.setVendedorNome(nome);
            vendedor.setEmail(email);
            vendedor.setRoles("USER");
            vendedor.setPassword(new BCryptPasswordEncoder ().encode("default-password"));
            vendedor.setVendedorSetor("Setor Padrão"); // Define um setor padrão
            vendedorRepository.save(vendedor);
        }

        return new RedirectView("/");
    }
}
