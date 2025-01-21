package com.accenture.shopsystem.domain.Vendedor;

import com.accenture.shopsystem.domain.Usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "vendedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idVendedor;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String vendedorNome;

    @NotBlank
    @Column(nullable = false, length = 45)
    private String vendedorSetor;

    @OneToOne(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;

    public void setId(String id) {
        this.idVendedor = id;
    }

    public String getId() {
        return idVendedor;
    }
    @Override
    public String toString() {
        return STR."Vendedor{idVendedor='\{idVendedor}', vendedorNome='\{vendedorNome}', vendedorSetor='\{vendedorSetor}'}";
    }
}
