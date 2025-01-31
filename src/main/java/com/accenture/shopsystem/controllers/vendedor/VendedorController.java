package com.accenture.shopsystem.controllers.vendedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.vendedor.VendedorRequestDTO;
import com.accenture.shopsystem.repositories.VendedorRepository;
import com.accenture.shopsystem.services.vendedor.VendedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.accenture.shopsystem.services.vendedor.VendedorService;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag (name = "Vendedores")
public class VendedorController {

    private final VendedorRepository vendedorRepository;
    private final VendedorService vendedorService;
    
    private static final Logger logger = LoggerFactory.getLogger(VendedorController.class);


    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar vendedor", description = "Cadastrar um novo vendedor")
    public ResponseEntity<Void> cadastrarVendedor(@ModelAttribute VendedorRequestDTO vendedorRequestDTO) {
    	
    	logger.info("Iniciando o cadastro de um novo vendedor");

        try {
            // Cria um novo Vendedor
            Vendedor vendedor = new Vendedor();
            vendedor.setVendedorNome(vendedorRequestDTO.getVendedorNome());
            vendedor.setVendedorSetor(vendedorRequestDTO.getVendedorSetor());
            vendedor.setEmail(vendedorRequestDTO.getEmail());
            vendedor.setPassword(new BCryptPasswordEncoder().encode(vendedorRequestDTO.getPassword()));

            // Salva o Vendedor no banco
            Vendedor vendedorSalvo = vendedorRepository.save(vendedor);

            logger.info("Vendedor cadastrado com sucesso: ID {}", vendedorSalvo.getId());
            return ResponseEntity.status(HttpStatus.FOUND) // Código 302 (redirecionamento)
                    .header("Location", "/") // Define a URL de redirecionamento
                    .build();
        } catch (Exception e) {
            logger.error("Erro ao cadastrar o vendedor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/vendedores")
    @Operation(summary = "Recuperar vendedores", description = "Obter uma lista de todos os vendedores")
    public ResponseEntity<Iterable<Vendedor>> getAllVendedores() {
        logger.info("Recuperando lista de todos os vendedores");

        try {
            Iterable<Vendedor> vendedores = vendedorRepository.findAll();
            logger.info("Lista de vendedores recuperada com sucesso");
            return ResponseEntity.ok(vendedores);
        } catch (Exception e) {
            logger.error("Erro ao recuperar a lista de vendedores: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

