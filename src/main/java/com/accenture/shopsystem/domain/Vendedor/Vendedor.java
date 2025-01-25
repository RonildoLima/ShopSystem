package com.accenture.shopsystem.domain.Vendedor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String vendedorNome;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String vendedorSetor;

    @Column(name = "roles")
    private String roles = "USER";

    @Override
    public String toString() {
        return "Vendedor{idVendedor='" + id + "', vendedorNome='" + vendedorNome + "', vendedorSetor='" + vendedorSetor + "'}";
    }
}
