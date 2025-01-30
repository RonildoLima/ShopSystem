package com.accenture.shopsystem.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.shopsystem.domain.pagamento.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
}