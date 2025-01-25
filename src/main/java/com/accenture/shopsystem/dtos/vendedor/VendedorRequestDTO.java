package com.accenture.shopsystem.dtos.vendedor;

import lombok.Data;

@Data
public class VendedorRequestDTO {
    private String vendedorNome;
    private String vendedorSetor;
    private String email;
    private String password;
}
