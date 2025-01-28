package com.accenture.shopsystem.services;

import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_Sucesso() {
        Vendedor vendedor = new Vendedor();
        vendedor.setEmail("teste@teste.com");
        vendedor.setPassword("senha123");

        when(vendedorRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(vendedor));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("teste@teste.com");

        assertNotNull(userDetails);
        assertEquals("teste@teste.com", userDetails.getUsername());
        assertEquals("senha123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UsuarioNaoEncontrado() {
        when(vendedorRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("naoexiste@teste.com");
        });

        assertEquals("Usuário não encontrado: naoexiste@teste.com", exception.getMessage());
        verify(vendedorRepository, times(1)).findByEmail("naoexiste@teste.com");
    }
}
