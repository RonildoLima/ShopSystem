package com.accenture.shopsystem.repositories;

import com.accenture.shopsystem.domain.Email.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
}
