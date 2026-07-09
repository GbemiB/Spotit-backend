package com.spotit.api.auth.repository;

import com.spotit.api.auth.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    Optional<OtpCode> findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(UUID id);

    void deleteByUserId(UUID userId);
}
