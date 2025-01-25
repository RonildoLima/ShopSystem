package com.accenture.shopsystem.repositories;

import com.accenture.shopsystem.domain.Produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {
    List<Produto> findByVendedorId(String vendedorId);
    Optional<Produto> findByProdutoDescricao(String produtoDescricao);
}


