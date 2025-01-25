package com.accenture.shopsystem.repositories;
import com.accenture.shopsystem.domain.PedidoHistoricoStatus.PedidoHistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoHistoricoStatusRepository extends JpaRepository<PedidoHistoricoStatus, String> {
}
