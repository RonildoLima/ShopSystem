package com.accenture.shopsystem.domain.PedidoTemProdutos;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Produto.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_tem_produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoTemProdutos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "pedido_idPedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_idProduto", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal precoUnitario;
}
