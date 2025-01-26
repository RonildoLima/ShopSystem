package com.accenture.shopsystem.controllers.produto;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.produto.ProdutoRequestDTO;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping
@Tag(name = "Produtos")
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;
    private VendedorRepository vendedorRepository;

    public ProdutoController(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @PostMapping("/produtos/adicionar")
    @Operation(summary = "Adicionar produto", description = "Método para salvar um novo produto")
    public RedirectView adicionarProduto(@ModelAttribute ProdutoRequestDTO produtoRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email;

        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
            // Autenticado via Google (OAuth2)
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = (String) oauth2User.getAttribute("email");
        } else {
            email = authentication.getName();
        }

        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        Produto produto = new Produto();
        produto.setProdutoDescricao(produtoRequestDTO.getProdutoDescricao());
        produto.setProdutoValor(produtoRequestDTO.getProdutoValor());
        produto.setQuantidadeEstoque(produtoRequestDTO.getQuantidadeEstoque());

        LocalDateTime dataHoraStatus = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataHoraStatus.format(formatter);
        produto.setProdutoDataHoraSaida(LocalDateTime.parse(dataHoraFormatada, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        produto.setVendedor(vendedor);

        produtoRepository.save(produto);

        System.out.println("Produto adicionado com sucesso: " + produto.getProdutoDescricao()
                + ", Valor: " + produto.getProdutoValor()
                + ", Estoque: " + produto.getQuantidadeEstoque());

        return new RedirectView ("/produtos/adicionar");
    }


    @DeleteMapping("{vendedorId}/produtos/{produtoId}")
    @Operation(summary = "Deletar produto", description = "Método para deletar um produto")
    public ResponseEntity<Void> excluirProduto(
            @PathVariable String vendedorId,
            @PathVariable String produtoId) {

        Optional<Produto> produtoExistente = produtoRepository.findById(produtoId);

        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();

            if (!produto.getVendedor().getId().equals(vendedorId)) {
                return ResponseEntity.status(403).build();
            }

            produtoRepository.deleteById(produtoId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{vendedorId}/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar produtos", description = "Método para listar os produtos do vendedor")
    public ResponseEntity<Iterable<Produto>> listarProdutosPorVendedor(@PathVariable String vendedorId) {
        // Busca todos os produtos associados ao vendedor
        Iterable<Produto> produtos = produtoRepository.findByVendedorId(vendedorId);
        return ResponseEntity.ok(produtos);
    }
}

