package com.spotit.api.partner.repository;

import com.spotit.api.partner.entity.PartnerCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PartnerCheckInRepository extends JpaRepository<PartnerCheckIn, UUID> {
    Optional<PartnerCheckIn> findByPartnerIdAndCheckInDate(UUID partnerId, LocalDate checkInDate);

    void deleteByPartnerId(UUID partnerId);

    void deleteByOwnerId(UUID ownerId);
}
