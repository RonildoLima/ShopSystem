package com.accenture.shopsystem.domain.Pedido;

import com.accenture.shopsystem.domain.PedidoTemProdutos.PedidoTemProdutos;
import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 45)
    private String pedidoDescricao;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal pedidoValor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime pedidoDataHora = LocalDateTime.now();

    @Column(nullable = false)
    private Integer pedidoQuantidade;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PedidoTemProdutos> produtos = new ArrayList<> ();

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", pedidoDescricao='" + pedidoDescricao + '\'' +
                ", pedidoValor=" + pedidoValor +
                ", pedidoQuantidade=" + pedidoQuantidade +
                '}';
    }

}
