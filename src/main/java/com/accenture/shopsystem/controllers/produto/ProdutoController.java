package com.accenture.shopsystem.controllers.produto;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.dtos.produto.ProdutoRequestDTO;
import com.accenture.shopsystem.services.produto.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping
@Tag(name = "Produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/produtos/adicionar")
    @Operation(summary = "Adicionar produto", description = "Método para salvar um novo produto")
    public RedirectView adicionarProduto(
            @ModelAttribute ProdutoRequestDTO produtoRequestDTO,
            RedirectAttributes redirectAttributes
    ) {
        RedirectView redirectView = produtoService.adicionarProduto(produtoRequestDTO, redirectAttributes);
        redirectAttributes.addFlashAttribute("successMessage", "Produto adicionado com sucesso!");
        return redirectView;
    }

    @DeleteMapping("{vendedorId}/produtos/{produtoId}")
    @Operation(summary = "Deletar produto", description = "Método para deletar um produto")
    public ResponseEntity<Void> excluirProduto(
            @PathVariable String vendedorId,
            @PathVariable String produtoId) {
        try {
            produtoService.excluirProduto(vendedorId, produtoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(value = "/{vendedorId}/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar produtos", description = "Método para listar os produtos do vendedor")
    public ResponseEntity<Iterable<Produto>> listarProdutosPorVendedor(@PathVariable String vendedorId) {
        Iterable<Produto> produtos = produtoService.listarProdutosPorVendedor(vendedorId);
        return ResponseEntity.ok(produtos);
    }
}
