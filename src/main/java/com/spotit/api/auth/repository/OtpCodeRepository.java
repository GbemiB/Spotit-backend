package com.spotit.api.auth.repository;

import com.spotit.api.auth.entity.OtpCode;
import com.spotit.api.auth.entity.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    Optional<OtpCode> findFirstByIdAndConsumedFalseOrderByCreatedAtDesc(UUID id);

    Optional<OtpCode> findFirstByUserIdAndPurposeAndConsumedFalseOrderByCreatedAtDesc(UUID userId, OtpPurpose purpose);

    void deleteByUserId(UUID userId);

    @Modifying
    @Query("update OtpCode o set o.consumed = true where o.userId = :userId and o.purpose = :purpose and o.consumed = false")
    void invalidateActive(UUID userId, OtpPurpose purpose);
}
