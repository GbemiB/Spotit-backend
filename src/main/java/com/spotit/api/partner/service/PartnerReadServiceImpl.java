package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.partner.dto.PartnerProfileResponse;
import com.spotit.api.partner.dto.PartnerStatusResponse;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerReadServiceImpl implements PartnerReadService {
    private final PartnerLinkRepository partnerLinkRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PartnerStatusResponse getStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        if (user.getAccountType().isPartner()) {
            return partnerStatus(userId);
        }
        Optional<PartnerLink> active = partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(userId, PartnerLinkStatus.active);
        if (active.isPresent()) {
            String partnerName = userRepository.findById(active.get().getPartnerId()).map(User::getFirstName).orElse(null);
            return PartnerStatusResponse.ownerActive(partnerName);
        }
        Optional<PartnerLink> pending = partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(userId, PartnerLinkStatus.pending);
        if (pending.isPresent()) {
            PartnerLink link = pending.get();
            String state = isExpired(link) ? PartnerStatusResponse.STATE_EXPIRED : PartnerStatusResponse.STATE_PENDING;
            return PartnerStatusResponse.ownerInvite(state, link.getInviteEmail(), link.getInviteExpiresAt());
        }
        if (!user.isOnboarded()) {
            Optional<String> inviter = latestValidInviteFor(user.getEmail())
                    .flatMap(link -> userRepository.findById(link.getOwnerId()))
                    .map(User::getFirstName);
            if (inviter.isPresent()) {
                return PartnerStatusResponse.invited(inviter.get());
            }
        }
        return PartnerStatusResponse.ownerNone();
    }

    @Override
    @Transactional(readOnly = true)
    public PartnerProfileResponse getOwnerProfile(UUID partnerId) {
        PartnerLink link = partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND));
        User owner = userRepository.findById(link.getOwnerId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND));
        return new PartnerProfileResponse(owner.getFirstName(), link.getPartnerNickname(), owner.getCycleLength(), owner.getPeriodLength(),
                owner.getLastPeriodDate(), CycleUtil.trackingStartOf(owner.getTrackingStartDate(), owner.getLastPeriodDate()));
    }

    private PartnerStatusResponse partnerStatus(UUID partnerId) {
        return partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)
                .map(link -> PartnerStatusResponse.partnerActive(
                        userRepository.findById(link.getOwnerId()).map(User::getFirstName).orElse(null),
                        link.getPartnerNickname(),
                        link.isPartnerReminder()))
                .orElseGet(PartnerStatusResponse::partnerEnded);
    }

    private Optional<PartnerLink> latestValidInviteFor(String email) {
        return partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus(email, PartnerLinkStatus.pending).stream()
                .filter(link -> !isExpired(link))
                .max(Comparator.comparing(PartnerLink::getCreatedAt));
    }

    static boolean isExpired(PartnerLink link) {
        return !link.getInviteExpiresAt().isAfter(Instant.now());
    }
}
