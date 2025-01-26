package com.accenture.shopsystem.domain.Produto;

import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime produtoDataHoraSaida;

    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false) // Associa o produto a um vendedor
    private Vendedor vendedor;

    @Override
    public String toString() {
        return "Produto{id='" + id + "', produtoDescricao='" + produtoDescricao + "', produtoValor=" + produtoValor +
                ", quantidadeEstoque=" + quantidadeEstoque + "}";
    }

    public String getDescricao() {
        return null;
    }
}
