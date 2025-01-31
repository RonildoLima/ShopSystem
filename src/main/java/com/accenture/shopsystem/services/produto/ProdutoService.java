package com.accenture.shopsystem.services.produto;

import com.accenture.shopsystem.domain.Produto.Produto;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.accenture.shopsystem.dtos.produto.ProdutoRequestDTO;
import com.accenture.shopsystem.repositories.ProdutoRepository;
import com.accenture.shopsystem.repositories.VendedorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final VendedorRepository vendedorRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);


    public ProdutoService(ProdutoRepository produtoRepository, VendedorRepository vendedorRepository) {
        this.produtoRepository = produtoRepository;
        this.vendedorRepository = vendedorRepository;
    }

    public RedirectView adicionarProduto(ProdutoRequestDTO produtoRequestDTO, RedirectAttributes redirectAttributes) {
    	logger.info("Iniciando o processo de adicionar produto...");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email;

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            email = (String) oauth2User.getAttribute("email");
            logger.debug("Autenticado como usuário OAuth2, e-mail: {}", email);
        } else {
            email = authentication.getName();
            logger.debug("Autenticado como usuário padrão, e-mail: {}", email);
        }

        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Vendedor não encontrado para o e-mail: {}", email);
                    return new RuntimeException("Vendedor não encontrado");
                });

        Produto produto = new Produto();
        produto.setProdutoDescricao(produtoRequestDTO.getProdutoDescricao());
        produto.setProdutoValor(produtoRequestDTO.getProdutoValor());
        produto.setQuantidadeEstoque(produtoRequestDTO.getQuantidadeEstoque());

        LocalDateTime dataHoraStatus = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataHoraStatus.format(formatter);
        produto.setProdutoDataHoraSaida(LocalDateTime.parse(dataHoraFormatada, formatter));

        produto.setVendedor(vendedor);

        produtoRepository.save(produto);

        logger.info("Produto adicionado com sucesso: Descrição = {}, Valor = {}, Estoque = {}",
                produto.getProdutoDescricao(), produto.getProdutoValor(), produto.getQuantidadeEstoque());

        redirectAttributes.addFlashAttribute("successMessage", "Produto adicionado com sucesso!");

        return new RedirectView("/produtos/adicionar");
    }

    public void excluirProduto(String vendedorId, String produtoId) {
    	logger.info("Iniciando processo de exclusão do produto com ID: {}", produtoId);

        Optional<Produto> produtoExistente = produtoRepository.findById(produtoId);

        if (produtoExistente.isEmpty()) {
            logger.error("Produto não encontrado com o ID: {}", produtoId);
            throw new RuntimeException("Produto não encontrado.");
        }

        Produto produto = produtoExistente.get();

        if (!produto.getVendedor().getId().equals(vendedorId)) {
            logger.error("Vendedor não autorizado a excluir o produto com ID: {}", produtoId);
            throw new RuntimeException("Você não tem permissão para excluir este produto.");
        }

        produtoRepository.deleteById(produtoId);
        logger.info("Produto com ID: {} foi excluído com sucesso.", produtoId);
    }

    public Iterable<Produto> listarProdutosPorVendedor(String vendedorId) {
    	logger.info("Listando produtos para o vendedor com ID: {}", vendedorId);
        return produtoRepository.findByVendedorId(vendedorId);
    }
}
