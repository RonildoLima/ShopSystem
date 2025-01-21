package com.accenture.shopsystem.domain.Pedido;

import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 45)
    private String pedidoDescricao;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal pedidoValor;

    @Column(nullable = false)
    private LocalDateTime pedidoDataHora = LocalDateTime.now();

    @Column(nullable = false)
    private Integer pedidoQuantidade;

    @ManyToOne
    @JoinColumn(name = "vendedor_idVendedor", nullable = false)
    private Vendedor vendedor;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoTemProdutos> produtos;

    public void setDescricao(String descricao) {
        this.pedidoDescricao = descricao;
    }

    public void setValorTotal(BigDecimal zero) {
        this.pedidoValor = zero;
    }
}
