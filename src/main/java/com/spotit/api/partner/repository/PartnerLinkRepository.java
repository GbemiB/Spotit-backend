package com.spotit.api.partner.repository;

import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerLinkRepository extends JpaRepository<PartnerLink, UUID> {
    Optional<PartnerLink> findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(UUID ownerId, PartnerLinkStatus status);

    Optional<PartnerLink> findFirstByPartnerIdAndStatus(UUID partnerId, PartnerLinkStatus status);

    List<PartnerLink> findByInviteEmailIgnoreCaseAndStatus(String inviteEmail, PartnerLinkStatus status);

    List<PartnerLink> findByStatusAndOwnerIdIn(PartnerLinkStatus status, Collection<UUID> ownerIds);

    List<PartnerLink> findByStatusAndPartnerReminderTrue(PartnerLinkStatus status);

    long countByOwnerIdAndCreatedAtAfter(UUID ownerId, Instant after);

    void deleteByOwnerId(UUID ownerId);

    void deleteByPartnerId(UUID partnerId);
}
