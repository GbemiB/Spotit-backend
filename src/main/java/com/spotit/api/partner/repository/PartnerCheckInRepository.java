package com.spotit.api.partner.repository;

import com.spotit.api.partner.entity.PartnerCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PartnerCheckInRepository extends JpaRepository<PartnerCheckIn, UUID> {
    List<PartnerCheckIn> findByPartnerIdAndCheckInDate(UUID partnerId, LocalDate checkInDate);

    List<PartnerCheckIn> findByOwnerIdAndCheckInDateBetweenOrderByCheckInDateAscCreatedAtAsc(UUID ownerId, LocalDate from, LocalDate to);

    @org.springframework.data.jpa.repository.Query(
            "select c.partnerId, max(c.createdAt) from PartnerCheckIn c where c.partnerId in :ids group by c.partnerId")
    List<Object[]> findLatestByPartnerIds(@org.springframework.data.repository.query.Param("ids") List<UUID> ids);

    void deleteByPartnerId(UUID partnerId);

    void deleteByOwnerId(UUID ownerId);
}
