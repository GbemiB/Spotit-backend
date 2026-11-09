package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.mail.OtpEmailTemplate;
import com.spotit.api.common.mail.PartnerInviteEmailTemplate;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.configuration.ConfigDefaults;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.partner.dto.AcceptInviteRequest;
import com.spotit.api.partner.dto.InviteRequest;
import com.spotit.api.partner.dto.PartnerCodeResponse;
import com.spotit.api.partner.dto.PartnerSettingsRequest;
import com.spotit.api.partner.dto.PartnerStatusResponse;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.user.entity.AccountOwner;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerWriteServiceImpl implements PartnerWriteService {
    static final Duration INVITE_TTL = Duration.ofHours(48);
    static final int MAX_SENDS_PER_DAY = 5;
    static final int MAX_FAILED_ATTEMPTS = 5;

    private final PartnerLinkRepository partnerLinkRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ConfigurationDomainService configurationDomainService;
    private final PartnerReadService partnerReadService;

    @Override
    @Transactional
    public PartnerStatusResponse invite(UUID ownerId, InviteRequest request) {
        User owner = requireInvitingOwner(ownerId);
        String email = request.email().strip().toLowerCase(Locale.ROOT);
        if (email.equalsIgnoreCase(owner.getEmail())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_INVITE_SELF);
        }
        userRepository.findByEmailIgnoreCase(email).ifPresent(this::requireInvitable);
        send(owner, email);
        return partnerReadService.getStatus(ownerId);
    }

    @Override
    @Transactional
    public PartnerStatusResponse resendInvite(UUID ownerId) {
        User owner = requireInvitingOwner(ownerId);
        PartnerLink pending = partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_INVITE_NOT_FOUND));
        send(owner, pending.getInviteEmail());
        return partnerReadService.getStatus(ownerId);
    }

    @Override
    @Transactional
    public void cancelInvite(UUID ownerId) {
        PartnerLink pending = partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_INVITE_NOT_FOUND));
        pending.setStatus(PartnerLinkStatus.cancelled);
        partnerLinkRepository.save(pending);
    }

    @Override
    @Transactional
    public PartnerCodeResponse sendJoinCode(UUID userId) {
        User user = requireJoinable(userId);
        Instant now = Instant.now();
        List<PartnerLink> invites = partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus(user.getEmail(), PartnerLinkStatus.pending).stream()
                .filter(link -> !PartnerReadServiceImpl.isExpired(link))
                .toList();
        if (invites.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_NO_INVITE_FOR_EMAIL);
        }
        long ttl = configurationDomainService.getPartnerCodeTtlSeconds();
        long wait = configurationDomainService.getPartnerCodeResendSeconds();
        Instant lastSent = invites.stream().map(PartnerLink::getCodeSentAt).filter(Objects::nonNull).max(Instant::compareTo).orElse(null);
        Instant codeExpiry = invites.stream().map(PartnerLink::getCodeExpiresAt).filter(Objects::nonNull).max(Instant::compareTo).orElse(null);
        if (lastSent != null && codeExpiry != null && now.isBefore(codeExpiry) && now.isBefore(lastSent.plusSeconds(wait))) {
            return new PartnerCodeResponse(user.getEmail(), false, Duration.between(now, codeExpiry).getSeconds(),
                    Duration.between(now, lastSent.plusSeconds(wait)).getSeconds());
        }
        int maxSends = configurationDomainService.getPartnerCodeMaxSends();
        if (invites.stream().anyMatch(link -> link.getCodeSends() >= maxSends)) {
            throw new ApiException(ErrorCode.INVITE_LIMIT_REACHED, ErrorMessage.PARTNER_CODE_LIMIT_REACHED);
        }
        String code = InviteCodes.newCode();
        String hash = TokenHasher.sha256Hex(code);
        for (PartnerLink link : invites) {
            link.setInviteCodeHash(hash);
            link.setCodeSentAt(now);
            link.setCodeExpiresAt(now.plusSeconds(ttl));
            link.setCodeSends(link.getCodeSends() + 1);
        }
        partnerLinkRepository.saveAll(invites);
        sendJoinCodeEmail(user, ownerNameOf(invites.get(0)), code, ttl);
        return new PartnerCodeResponse(user.getEmail(), true, ttl, wait);
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class)
    public PartnerStatusResponse accept(UUID userId, AcceptInviteRequest request) {
        User user = requireJoinable(userId);
        String nickname = request.nickname().strip();
        if (nickname.isEmpty()) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_NICKNAME_BLANK);
        }
        List<PartnerLink> invites = partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus(user.getEmail(), PartnerLinkStatus.pending);
        if (invites.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_NO_INVITE_FOR_EMAIL);
        }
        String codeHash = TokenHasher.sha256Hex(InviteCodes.normalize(request.code()));
        Optional<PartnerLink> match = invites.stream().filter(link -> link.getInviteCodeHash().equals(codeHash)).findFirst();
        if (match.isEmpty()) {
            recordFailedAttempt(invites);
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.PARTNER_INVITE_CODE_INVALID);
        }
        PartnerLink link = match.get();
        if (PartnerReadServiceImpl.isExpired(link)) {
            throw new ApiException(ErrorCode.INVITE_EXPIRED, ErrorMessage.PARTNER_INVITE_EXPIRED);
        }
        if (link.getCodeExpiresAt() == null || Instant.now().isAfter(link.getCodeExpiresAt())) {
            throw new ApiException(ErrorCode.INVALID_CODE, ErrorMessage.PARTNER_CODE_EXPIRED);
        }
        User owner = userRepository.findById(link.getOwnerId())
                .filter(found -> found.getPendingDeletionAt() == null)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_NO_INVITE_FOR_EMAIL));
        if (partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(owner.getId(), PartnerLinkStatus.active).isPresent()) {
            throw new ApiException(ErrorCode.PARTNER_ALREADY_LINKED, ErrorMessage.PARTNER_INVITEE_LINKED);
        }
        link.setPartnerId(userId);
        link.setStatus(PartnerLinkStatus.active);
        link.setAcceptedAt(Instant.now());
        link.setPartnerNickname(nickname);
        partnerLinkRepository.save(link);
        user.setAccountType(AccountOwner.partner);
        user.setOnboarded(true);
        if (user.getOnboardedAt() == null) {
            user.setOnboardedAt(Instant.now());
        }
        userRepository.save(user);
        return partnerReadService.getStatus(userId);
    }

    @Override
    @Transactional
    public PartnerStatusResponse updateSettings(UUID userId, PartnerSettingsRequest request) {
        User user = findUser(userId);
        PartnerLink link = activeLinkFor(user);
        if (user.getAccountType().isPartner()) {
            if (request.nickname() != null) {
                String nickname = request.nickname().strip();
                if (nickname.isEmpty()) {
                    throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_NICKNAME_BLANK);
                }
                link.setPartnerNickname(nickname);
            }
            if (request.partnerReminder() != null) {
                link.setPartnerReminder(request.partnerReminder());
            }
        }
        partnerLinkRepository.save(link);
        return partnerReadService.getStatus(userId);
    }

    @Override
    @Transactional
    public void unlink(UUID userId) {
        PartnerLink link = activeLinkFor(findUser(userId));
        link.setStatus(PartnerLinkStatus.ended);
        partnerLinkRepository.save(link);
    }

    @Override
    @Transactional
    public void endAllFor(UUID userId) {
        partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(userId, PartnerLinkStatus.active)
                .ifPresent(link -> close(link, PartnerLinkStatus.ended));
        partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(userId, PartnerLinkStatus.pending)
                .ifPresent(link -> close(link, PartnerLinkStatus.cancelled));
        partnerLinkRepository.findFirstByPartnerIdAndStatus(userId, PartnerLinkStatus.active)
                .ifPresent(link -> close(link, PartnerLinkStatus.ended));
    }

    private void close(PartnerLink link, PartnerLinkStatus status) {
        link.setStatus(status);
        partnerLinkRepository.save(link);
    }

    private void send(User owner, String email) {
        Instant now = Instant.now();
        if (partnerLinkRepository.countByOwnerIdAndCreatedAtAfter(owner.getId(), now.minus(Duration.ofDays(1))) >= MAX_SENDS_PER_DAY) {
            throw new ApiException(ErrorCode.INVITE_LIMIT_REACHED, ErrorMessage.PARTNER_INVITE_LIMIT_REACHED);
        }
        partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(owner.getId(), PartnerLinkStatus.pending)
                .ifPresent(old -> {
                    old.setStatus(PartnerLinkStatus.cancelled);
                    partnerLinkRepository.save(old);
                });
        PartnerLink link = PartnerLink.builder()
                .ownerId(owner.getId())
                .inviteEmail(email)
                .inviteCodeHash(TokenHasher.sha256Hex(UUID.randomUUID().toString()))
                .inviteExpiresAt(now.plus(INVITE_TTL))
                .status(PartnerLinkStatus.pending)
                .partnerReminder(true)
                .failedAttempts(0)
                .codeSends(0)
                .build();
        partnerLinkRepository.save(link);
        sendInviteEmail(owner, email);
    }

    private void sendInviteEmail(User owner, String email) {
        String name = owner.getFirstName();
        String storeUrl = Optional.ofNullable(configurationDomainService.getAppStoreUrlAndroid())
                .filter(url -> !url.isBlank())
                .orElse(ConfigDefaults.APP_STORE_URL_ANDROID);
        long hours = INVITE_TTL.toHours();
        try {
            emailService.send(email, PartnerInviteEmailTemplate.subject(name),
                    PartnerInviteEmailTemplate.html(name, storeUrl, hours),
                    PartnerInviteEmailTemplate.text(name, storeUrl, hours));
            log.info("Partner invite email sent for owner {}", owner.getId());
        } catch (MailException e) {
            log.error("Failed to send partner invite email for owner {}", owner.getId(), e);
        }
    }

    private void sendJoinCodeEmail(User user, String ownerName, String code, long ttlSeconds) {
        String greeting = user.getFirstName() == null || user.getFirstName().isBlank() ? "there" : user.getFirstName();
        String heading = "Join " + ownerName + " on Spot it";
        String intro = "here's your code to link with " + ownerName + " on Spot it.";
        long minutes = Math.max(1, ttlSeconds / 60);
        try {
            emailService.send(user.getEmail(), "Your Spot it partner code",
                    OtpEmailTemplate.html(greeting, code, heading, intro, minutes),
                    OtpEmailTemplate.text(greeting, code, heading, intro, minutes));
            log.info("Partner join code sent to user {}", user.getId());
        } catch (MailException e) {
            log.error("Failed to send partner join code to user {}", user.getId(), e);
        }
    }

    private String ownerNameOf(PartnerLink link) {
        return userRepository.findById(link.getOwnerId())
                .map(User::getFirstName)
                .filter(name -> name != null && !name.isBlank())
                .orElse("your partner");
    }

    private User requireJoinable(UUID userId) {
        User user = findUser(userId);
        if (user.getAccountType().isOwner() && user.isOnboarded()) {
            throw new ApiException(ErrorCode.PARTNER_NOT_ALLOWED, ErrorMessage.PARTNER_ACCOUNT_TRACKS_CYCLE);
        }
        if (partnerLinkRepository.findFirstByPartnerIdAndStatus(userId, PartnerLinkStatus.active).isPresent()) {
            throw new ApiException(ErrorCode.PARTNER_ALREADY_LINKED, ErrorMessage.PARTNER_INVITEE_LINKED);
        }
        return user;
    }

    private void recordFailedAttempt(List<PartnerLink> invites) {
        for (PartnerLink invite : invites) {
            invite.setFailedAttempts(invite.getFailedAttempts() + 1);
            if (invite.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
                invite.setStatus(PartnerLinkStatus.cancelled);
            }
        }
        partnerLinkRepository.saveAll(invites);
    }

    private User requireInvitingOwner(UUID ownerId) {
        User owner = findUser(ownerId);
        if (!owner.getAccountType().isOwner() || !owner.isOnboarded()) {
            throw new ApiException(ErrorCode.PARTNER_NOT_ALLOWED, ErrorMessage.PARTNER_INVITE_NEEDS_OWNER);
        }
        if (partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active).isPresent()) {
            throw new ApiException(ErrorCode.PARTNER_ALREADY_LINKED, ErrorMessage.PARTNER_ALREADY_LINKED);
        }
        return owner;
    }

    private void requireInvitable(User invitee) {
        if (invitee.getAccountType().isOwner() && invitee.isOnboarded()) {
            throw new ApiException(ErrorCode.PARTNER_NOT_ALLOWED, ErrorMessage.PARTNER_INVITEE_TRACKS_CYCLE);
        }
        if (invitee.getAccountType().isPartner()
                && partnerLinkRepository.findFirstByPartnerIdAndStatus(invitee.getId(), PartnerLinkStatus.active).isPresent()) {
            throw new ApiException(ErrorCode.PARTNER_ALREADY_LINKED, ErrorMessage.PARTNER_INVITEE_LINKED);
        }
    }

    private PartnerLink activeLinkFor(User user) {
        Optional<PartnerLink> link = user.getAccountType().isOwner()
                ? partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(user.getId(), PartnerLinkStatus.active)
                : partnerLinkRepository.findFirstByPartnerIdAndStatus(user.getId(), PartnerLinkStatus.active);
        return link.orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
