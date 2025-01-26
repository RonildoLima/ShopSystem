package com.accenture.shopsystem.dtos.pagamento;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PedidoMensagem implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long pedidoId;
    private String cliente;
    private List<Long> produtosIds;
    private double valorTotal;
    private String status; // "PENDENTE", "PAGO", "RECUSADO"


}