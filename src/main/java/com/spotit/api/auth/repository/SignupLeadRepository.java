package com.spotit.api.auth.repository;

import com.spotit.api.auth.entity.SignupLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SignupLeadRepository extends JpaRepository<SignupLead, UUID> {
    Optional<SignupLead> findByEmailIgnoreCase(String email);
}
