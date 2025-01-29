package com.accenture.shopsystem.controllers;

import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.services.email.EmailService;
import com.accenture.shopsystem.dtos.email.EmailDto;
import com.accenture.shopsystem.repositories.VendedorRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
@Tag(name = "Autenticação do google")
public class OAuth2LoginController {

    private final VendedorRepository vendedorRepository;

    public OAuth2LoginController(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @GetMapping("/oauth2/success")
    public RedirectView loginComGoogle(@AuthenticationPrincipal OAuth2User principal) {
        String nome = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        // Verifica se o vendedor já existe no banco
        if (vendedorRepository.findByEmail(email).isEmpty()) {
            Vendedor vendedor = new Vendedor();
            vendedor.setVendedorNome(nome);
            vendedor.setEmail(email);
            vendedor.setRoles("USER");
            String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
            vendedor.setPassword(new BCryptPasswordEncoder().encode(generatedPassword));
            vendedor.setVendedorSetor("Serviços");

            vendedorRepository.save(vendedor);

            EmailDto emailDto = new EmailDto ();
            emailDto.setOwnerRef(nome);
            emailDto.setEmailFrom("shopsystemsuporte@gmail.com");
            emailDto.setEmailTo(email);
            emailDto.setSubject("Cadastro bem-sucedido");
            emailDto.setText("Olá " + nome + ",\n\nSeu cadastro foi realizado com sucesso!\n\nBem-vindo ao sistema!");
            EmailService.enviarEmail(emailDto);
        }
        return new RedirectView("/");
    }
}
