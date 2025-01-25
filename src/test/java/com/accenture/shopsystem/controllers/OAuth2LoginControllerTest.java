package com.accenture.shopsystem.controllers;

import com.accenture.shopsystem.controllers.OAuth2LoginController;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuth2LoginControllerTest {

    @Test
    void loginComGoogle() {
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        OAuth2User principal = Mockito.mock(OAuth2User.class);
        when(principal.getAttribute("name")).thenReturn("João Silva");
        when(principal.getAttribute("email")).thenReturn("joao.silva@example.com");
        when(vendedorRepository.findByEmail("joao.silva@example.com")).thenReturn(Optional.empty());

        OAuth2LoginController controller = new OAuth2LoginController(vendedorRepository);

        RedirectView redirectView = controller.loginComGoogle(principal);

        assertEquals("/", redirectView.getUrl());

        ArgumentCaptor<Vendedor> vendedorCaptor = ArgumentCaptor.forClass(Vendedor.class);
        verify(vendedorRepository).save(vendedorCaptor.capture());
        Vendedor savedVendedor = vendedorCaptor.getValue();

        assertEquals("João Silva", savedVendedor.getVendedorNome());
        assertEquals("joao.silva@example.com", savedVendedor.getEmail());
        assertEquals("USER", savedVendedor.getRoles());
        assertTrue(new BCryptPasswordEncoder().matches("default-password", savedVendedor.getPassword()));
        assertEquals("Setor Padrão", savedVendedor.getVendedorSetor());

        verify(vendedorRepository, times(1)).findByEmail("joao.silva@example.com");
    }

    @Test
    void loginComGoogle_ExistingUser() {
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        OAuth2User principal = Mockito.mock(OAuth2User.class);
        when(principal.getAttribute("name")).thenReturn("João Silva");
        when(principal.getAttribute("email")).thenReturn("joao.silva@example.com");

        Vendedor existingVendedor = new Vendedor();
        existingVendedor.setEmail("joao.silva@example.com");
        when(vendedorRepository.findByEmail("joao.silva@example.com")).thenReturn(Optional.of(existingVendedor));

        OAuth2LoginController controller = new OAuth2LoginController(vendedorRepository);

        RedirectView redirectView = controller.loginComGoogle(principal);

        assertEquals("/", redirectView.getUrl());
        verify(vendedorRepository, never()).save(any(Vendedor.class));
        verify(vendedorRepository, times(1)).findByEmail("joao.silva@example.com");
    }
}
