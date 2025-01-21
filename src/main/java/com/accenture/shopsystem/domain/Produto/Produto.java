package com.accenture.shopsystem.domain.Produto;

import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String produtoDescricao;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal produtoValor;

    @Column(nullable = false)
    private LocalDateTime produtoDataHoraSaida;

    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false) // Associa o produto a um vendedor
    private Vendedor vendedor;

    public String getDescricao() {
        return null;
    }
}
