package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.i18n.EnumMessages;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.log.dto.EnumOption;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;
import com.spotit.api.partner.dto.PartnerLoveItem;
import com.spotit.api.partner.dto.PartnerLoveResponse;
import com.spotit.api.partner.entity.PartnerCheckIn;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.entity.SupportType;
import com.spotit.api.partner.repository.PartnerCheckInRepository;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerCheckInServiceImpl implements PartnerCheckInService {
    private static final List<EnumOption> OPTIONS = Arrays.stream(SupportType.values())
            .map(type -> new EnumOption(type.name(), EnumMessages.resolve(type.getCode())))
            .toList();

    private final PartnerCheckInRepository partnerCheckInRepository;
    private final PartnerLinkRepository partnerLinkRepository;
    private final UserRepository userRepository;
    private final PointsWriteService pointsWriteService;
    private final ChallengeReadService challengeReadService;
    private final UserClock userClock;
    private final PartnerLoveNotifier partnerLoveNotifier;

    @Override
    @Transactional(readOnly = true)
    public PartnerCheckInResponse getToday(UUID partnerId) {
        User partner = requirePartner(partnerId);
        Optional<PartnerCheckIn> today = partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, userClock.today(partner));
        return new PartnerCheckInResponse(OPTIONS, today.isPresent(), today.map(PartnerCheckInServiceImpl::supportsOf).orElse(List.of()),
                challengeReadService.getDailyLogReward(), partner.getStreak(), 0, partner.getPoints(), today.map(PartnerCheckIn::getNote).orElse(null));
    }

    @Override
    @Transactional
    public PartnerCheckInResponse checkIn(UUID partnerId, PartnerCheckInRequest request) {
        User partner = requirePartner(partnerId);
        PartnerLink link = partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND));
        List<SupportType> supports = parse(request.supports() == null ? List.of() : request.supports());
        String note = request.note() == null || request.note().isBlank() ? null : request.note().strip();
        if (supports.isEmpty() && note == null) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_NOTHING_TO_SEND);
        }
        LocalDate today = userClock.today(partner);
        if (partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today).isPresent()) {
            throw new ApiException(ErrorCode.ALREADY_CLAIMED, ErrorMessage.PARTNER_ALREADY_CHECKED_IN);
        }
        partnerCheckInRepository.save(PartnerCheckIn.builder()
                .partnerId(partnerId)
                .ownerId(link.getOwnerId())
                .checkInDate(today)
                .supports(supports.stream().map(type -> String.valueOf(type.getValue())).collect(Collectors.joining(",")))
                .note(note)
                .build());
        PointsWriteService.LogPointsResult points = pointsWriteService.recordCheckIn(partnerId);
        userRepository.findById(link.getOwnerId())
                .ifPresent(owner -> partnerLoveNotifier.notifyOwner(owner, partner, labelsOf(supports.stream().map(SupportType::name).toList()), note));
        return new PartnerCheckInResponse(OPTIONS, true, supports.stream().map(SupportType::name).toList(),
                challengeReadService.getDailyLogReward(), points.streak(), points.pointsAwarded(), points.newBalance(), note);
    }

    @Override
    @Transactional(readOnly = true)
    public PartnerLoveResponse loveFor(UUID ownerId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.INVALID_DATE_RANGE);
        }
        List<PartnerCheckIn> checkIns = partnerCheckInRepository.findByOwnerIdAndCheckInDateBetweenOrderByCheckInDateAsc(ownerId, from, to);
        Map<UUID, String> names = userRepository.findAllById(checkIns.stream().map(PartnerCheckIn::getPartnerId).distinct().toList()).stream()
                .collect(Collectors.toMap(User::getId, user -> user.getFirstName() == null ? "" : user.getFirstName(), (a, b) -> a));
        return new PartnerLoveResponse(checkIns.stream().map(checkIn -> {
            List<String> ids = supportsOf(checkIn);
            return new PartnerLoveItem(checkIn.getCheckInDate(), names.getOrDefault(checkIn.getPartnerId(), ""), ids, labelsOf(ids), checkIn.getNote());
        }).toList());
    }

    private static List<String> labelsOf(List<String> ids) {
        Map<String, String> labels = OPTIONS.stream().collect(Collectors.toMap(EnumOption::id, EnumOption::label, (a, b) -> a));
        return ids.stream().map(id -> labels.getOrDefault(id, id)).toList();
    }

    private static List<SupportType> parse(List<String> ids) {
        try {
            return ids.stream().map(String::strip).distinct().map(SupportType::valueOf).toList();
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_UNKNOWN_SUPPORT);
        }
    }

    private static List<String> supportsOf(PartnerCheckIn checkIn) {
        return Arrays.stream(checkIn.getSupports().split(","))
                .filter(value -> !value.isBlank())
                .map(value -> SupportType.fromInt(Integer.valueOf(value)).name())
                .toList();
    }

    private User requirePartner(UUID partnerId) {
        User user = userRepository.findById(partnerId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        if (!user.getAccountType().isPartner()) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND);
        }
        return user;
    }
}
