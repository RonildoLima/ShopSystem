package com.accenture.shopsystem.dtos.pedido;

import lombok.Data;

@Data
public class PedidoItemRequest {
    private String produtoId;
    private Integer quantidade;
}
