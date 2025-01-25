package com.accenture.shopsystem.domain.PedidoTemProdutos;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Produto.Produto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal precoUnitario;

    @Override
    public String toString() {
        return "PedidoTemProdutos{id='" + id + "', quantidade=" + quantidade + ", precoUnitario=" + precoUnitario + "}";
    }
}
