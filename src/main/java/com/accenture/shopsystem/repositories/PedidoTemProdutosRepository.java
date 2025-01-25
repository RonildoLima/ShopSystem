package com.accenture.shopsystem.repositories;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoTemProdutosRepository extends JpaRepository<Pedido, String> {
}
