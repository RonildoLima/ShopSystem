package com.accenture.shopsystem.domain.Usuario;

import com.accenture.shopsystem.domain.Vendedor.Vendedor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "vendedor_id", nullable = false, unique = true)
    private Vendedor vendedor;
}
