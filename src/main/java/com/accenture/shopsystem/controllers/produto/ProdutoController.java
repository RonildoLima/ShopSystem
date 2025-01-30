package com.accenture.shopsystem.controllers.produto;

import com.accenture.shopsystem.controllers.pagamento.PagamentoController;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping
@Tag(name = "Produtos")
public class ProdutoController {

	private final ProdutoService produtoService;

	private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;

	}

	@PostMapping("/produtos/adicionar")
	@Operation(summary = "Adicionar produto", description = "Método para salvar um novo produto")
	public RedirectView adicionarProduto(@ModelAttribute ProdutoRequestDTO produtoRequestDTO,
			RedirectAttributes redirectAttributes) {
		logger.info("Recebida solicitação para adicionar um produto: {}", produtoRequestDTO);
		try {
			RedirectView redirectView = produtoService.adicionarProduto(produtoRequestDTO, redirectAttributes);
			redirectAttributes.addFlashAttribute("successMessage", "Produto adicionado com sucesso!");
			logger.info("Produto adicionado com sucesso.");
			return redirectView;
		} catch (Exception e) {
			logger.error("Erro ao adicionar produto: {}", e.getMessage(), e);
			throw e;
		}
	}

	@DeleteMapping("{vendedorId}/produtos/{produtoId}")
	@Operation(summary = "Deletar produto", description = "Método para deletar um produto")
	public ResponseEntity<Void> excluirProduto(@PathVariable String vendedorId, @PathVariable String produtoId) {
		logger.info("Recebida solicitação para excluir o produto com ID {} pelo vendedor {}", produtoId, vendedorId);
		try {
			produtoService.excluirProduto(vendedorId, produtoId);
			logger.info("Produto com ID {} excluído com sucesso pelo vendedor {}", produtoId, vendedorId);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			logger.warn("Falha ao excluir o produto com ID {} pelo vendedor {}: {}", produtoId, vendedorId,
					e.getMessage());
			return ResponseEntity.status(403).build();
		}
	}

	@GetMapping(value = "/{vendedorId}/listar", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Listar produtos", description = "Método para listar os produtos do vendedor")
	public ResponseEntity<Iterable<Produto>> listarProdutosPorVendedor(@PathVariable String vendedorId) {
		logger.info("Recebida solicitação para listar produtos do vendedor {}", vendedorId);
        try {
            Iterable<Produto> produtos = produtoService.listarProdutosPorVendedor(vendedorId);
            logger.info("Produtos listados com sucesso para o vendedor {}", vendedorId);
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("Erro ao listar produtos para o vendedor {}: {}", vendedorId, e.getMessage(), e);
            throw e;
        }
	}
}
