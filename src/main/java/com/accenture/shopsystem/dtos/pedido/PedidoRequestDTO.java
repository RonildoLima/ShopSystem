package com.accenture.shopsystem.dtos.pedido;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDTO {
    private String pedidoDescricao;
    private List<ProdutoDTO> produtos;

    @Data
    public static class ProdutoDTO {
        private String nome; // Nome do produto
        private int quantidade;

    }
}
