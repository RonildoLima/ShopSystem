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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final VendedorRepository vendedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository, VendedorRepository vendedorRepository) {
        this.produtoRepository = produtoRepository;
        this.vendedorRepository = vendedorRepository;
    }

    public RedirectView adicionarProduto(ProdutoRequestDTO produtoRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email;

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
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
        produto.setProdutoDataHoraSaida(LocalDateTime.parse(dataHoraFormatada, formatter));

        produto.setVendedor(vendedor);

        produtoRepository.save(produto);

        System.out.println("Produto adicionado com sucesso: " + produto.getProdutoDescricao()
                + ", Valor: " + produto.getProdutoValor()
                + ", Estoque: " + produto.getQuantidadeEstoque());

        return new RedirectView("/produtos/adicionar");
    }

    public void excluirProduto(String vendedorId, String produtoId) {
        Optional<Produto> produtoExistente = produtoRepository.findById(produtoId);

        if (produtoExistente.isEmpty()) {
            throw new RuntimeException("Produto não encontrado.");
        }

        Produto produto = produtoExistente.get();

        if (!produto.getVendedor().getId().equals(vendedorId)) {
            throw new RuntimeException("Você não tem permissão para excluir este produto.");
        }

        produtoRepository.deleteById(produtoId);
    }

    public Iterable<Produto> listarProdutosPorVendedor(String vendedorId) {
        return produtoRepository.findByVendedorId(vendedorId);
    }
}
