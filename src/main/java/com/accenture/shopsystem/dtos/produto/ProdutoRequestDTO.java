package com.accenture.shopsystem.dtos.produto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {
    private String produtoDescricao;
    private BigDecimal produtoValor;
    private Integer quantidadeEstoque;
}
