package com.accenture.shopsystem.repositories;

import com.accenture.shopsystem.domain.Pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
    List<Pedido> findByVendedorId(String vendedorId);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.produtos prod LEFT JOIN FETCH prod.produto WHERE p.vendedor.id = :vendedorId")
    List<Pedido> findPedidosByVendedorIdWithProdutos(@Param("vendedorId") String vendedorId);
}
