package com.accenture.shopsystem.controllers.vendedor;

import com.accenture.shopsystem.controllers.vendedor.VendedorController;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.vendedor.VendedorRequestDTO;
import com.accenture.shopsystem.repositories.VendedorRepository;
import com.accenture.shopsystem.services.vendedor.VendedorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendedorControllerTest {

    @Test
    void cadastrarVendedor() {
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        VendedorService vendedorService = Mockito.mock(VendedorService.class);
        VendedorController controller = new VendedorController(vendedorRepository, vendedorService);

        VendedorRequestDTO requestDTO = new VendedorRequestDTO();
        requestDTO.setVendedorNome("João Silva");
        requestDTO.setVendedorSetor("Financeiro");
        requestDTO.setEmail("joao.silva@example.com");
        requestDTO.setPassword("senha123");

        ArgumentCaptor<Vendedor> vendedorCaptor = ArgumentCaptor.forClass(Vendedor.class);

        ResponseEntity<Void> response = controller.cadastrarVendedor(requestDTO);

        assertEquals(302, response.getStatusCodeValue());
        assertTrue(response.getHeaders().containsKey("Location"));
        assertEquals("/", response.getHeaders().get("Location").get(0));

        verify(vendedorRepository, times(1)).save(vendedorCaptor.capture());
        Vendedor savedVendedor = vendedorCaptor.getValue();

        assertEquals("João Silva", savedVendedor.getVendedorNome());
        assertEquals("Financeiro", savedVendedor.getVendedorSetor());
        assertEquals("joao.silva@example.com", savedVendedor.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("senha123", savedVendedor.getPassword()));
    }

    @Test
    void getAllVendedores() {
        VendedorRepository vendedorRepository = Mockito.mock(VendedorRepository.class);
        VendedorService vendedorService = Mockito.mock(VendedorService.class);
        VendedorController controller = new VendedorController(vendedorRepository, vendedorService);

        Vendedor vendedor1 = new Vendedor();
        vendedor1.setVendedorNome("João Silva");

        Vendedor vendedor2 = new Vendedor();
        vendedor2.setVendedorNome("Maria Oliveira");

        List<Vendedor> vendedores = Arrays.asList(vendedor1, vendedor2);
        when(vendedorRepository.findAll()).thenReturn(vendedores);

        ResponseEntity<Iterable<Vendedor>> response = controller.getAllVendedores();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertIterableEquals(vendedores, response.getBody());
        verify(vendedorRepository, times(1)).findAll();
    }
}
