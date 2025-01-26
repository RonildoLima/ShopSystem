package com.accenture.shopsystem.dtos.pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PagamentoDTO {

    private Long pedidoId;
    private BigDecimal valor;
    private String status;
    private LocalDateTime dataPagamento;

}