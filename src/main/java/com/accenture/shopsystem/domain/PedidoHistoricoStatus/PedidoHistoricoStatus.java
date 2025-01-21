package com.accenture.shopsystem.domain.PedidoHistoricoStatus;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import com.accenture.shopsystem.domain.Enums.StatusPedidoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_historico_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoHistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "pedido_idPedido", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedidoEnum statusPedido;

    @Column(nullable = false)
    private LocalDateTime dataHoraStatusPedido;

    @Column(nullable = true)
    private LocalDateTime dataHoraPagamento;
}
