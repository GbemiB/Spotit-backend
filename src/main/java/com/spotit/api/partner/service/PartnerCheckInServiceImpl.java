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
import com.spotit.api.partner.entity.ExpressionType;
import com.spotit.api.partner.entity.LoveKind;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerCheckInServiceImpl implements PartnerCheckInService {
    private static final List<EnumOption> OPTIONS = Arrays.stream(SupportType.values())
            .map(type -> new EnumOption(type.name(), EnumMessages.resolve(type.getCode())))
            .toList();
    private static final List<EnumOption> EXPRESSIONS = Arrays.stream(ExpressionType.values())
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
        List<PartnerCheckIn> today = partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, userClock.today(partner));
        PartnerCheckIn last = today.isEmpty() ? null : today.get(today.size() - 1);
        return response(today, last, partner.getStreak(), 0, partner.getPoints());
    }

    @Override
    @Transactional
    public PartnerCheckInResponse checkIn(UUID partnerId, PartnerCheckInRequest request) {
        User partner = requirePartner(partnerId);
        PartnerLink link = partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND));
        List<SupportType> supports = parse(request.supports() == null ? List.of() : request.supports());
        String note = request.note() == null || request.note().isBlank() ? null : request.note().strip();
        ExpressionType expression = parseExpression(request.expression());
        LoveKind kind = kindOf(request.kind(), supports, note, expression);
        LocalDate today = userClock.today(partner);
        PartnerCheckIn saved = partnerCheckInRepository.save(PartnerCheckIn.builder()
                .partnerId(partnerId)
                .ownerId(link.getOwnerId())
                .checkInDate(today)
                .kind(kind.name())
                .supports(kind == LoveKind.promise
                        ? supports.stream().map(type -> String.valueOf(type.getValue())).collect(Collectors.joining(",")) : "")
                .note(kind == LoveKind.expression ? null : note)
                .expression(kind == LoveKind.expression ? expression.name() : null)
                .build());
        PointsWriteService.LogPointsResult points = pointsWriteService.recordCheckIn(partnerId);
        userRepository.findById(link.getOwnerId())
                .ifPresent(owner -> partnerLoveNotifier.notifyOwner(owner, partner, kind, labelsOf(supportsOf(saved)), saved.getNote(), expression));
        List<PartnerCheckIn> sentToday = new ArrayList<>(partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today));
        if (sentToday.stream().noneMatch(c -> c == saved)) {
            sentToday.add(saved);
        }
        return response(sentToday, saved, points.streak(), points.pointsAwarded(), points.newBalance());
    }

    @Override
    @Transactional(readOnly = true)
    public PartnerLoveResponse loveFor(UUID ownerId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.INVALID_DATE_RANGE);
        }
        List<PartnerCheckIn> checkIns = partnerCheckInRepository.findByOwnerIdAndCheckInDateBetweenOrderByCheckInDateAscCreatedAtAsc(ownerId, from, to);
        Map<UUID, String> names = userRepository.findAllById(checkIns.stream().map(PartnerCheckIn::getPartnerId).distinct().toList()).stream()
                .collect(Collectors.toMap(User::getId, user -> user.getFirstName() == null ? "" : user.getFirstName(), (a, b) -> a));
        return new PartnerLoveResponse(checkIns.stream().map(checkIn -> {
            List<String> ids = supportsOf(checkIn);
            ExpressionType expression = expressionOf(checkIn);
            return new PartnerLoveItem(checkIn.getCheckInDate(), names.getOrDefault(checkIn.getPartnerId(), ""), kindOf(checkIn).name(), ids,
                    labelsOf(ids), checkIn.getNote(), expression == null ? null : expression.name(),
                    expression == null ? null : EnumMessages.resolve(expression.getCode()), expression == null ? null : expression.getEmoji(),
                    checkIn.getCreatedAt());
        }).toList());
    }

    private PartnerCheckInResponse response(List<PartnerCheckIn> today, PartnerCheckIn last, int streak, int pointsAwarded, long balance) {
        Map<String, Integer> byKind = new LinkedHashMap<>();
        for (LoveKind kind : LoveKind.values()) {
            byKind.put(kind.name(), 0);
        }
        today.forEach(checkIn -> byKind.merge(kindOf(checkIn).name(), 1, Integer::sum));
        ExpressionType expression = last == null ? null : expressionOf(last);
        return new PartnerCheckInResponse(OPTIONS, !today.isEmpty(), last == null ? List.of() : supportsOf(last),
                challengeReadService.getDailyLogReward(), streak, pointsAwarded, balance, last == null ? null : last.getNote(), EXPRESSIONS,
                last == null ? null : kindOf(last).name(), expression == null ? null : expression.name(), today.size(), byKind);
    }

    private static LoveKind kindOf(String requested, List<SupportType> supports, String note, ExpressionType expression) {
        if (requested == null) {
            if (supports.isEmpty() && note == null) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_NOTHING_TO_SEND);
            }
            return supports.isEmpty() ? LoveKind.text : LoveKind.promise;
        }
        LoveKind kind = LoveKind.valueOf(requested);
        boolean ok = switch (kind) {
            case promise -> !supports.isEmpty();
            case text -> note != null;
            case expression -> expression != null;
        };
        if (!ok) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_NOTHING_TO_SEND);
        }
        return kind;
    }

    private static LoveKind kindOf(PartnerCheckIn checkIn) {
        if (checkIn.getKind() != null) {
            return LoveKind.valueOf(checkIn.getKind());
        }
        return checkIn.getSupports() == null || checkIn.getSupports().isBlank() ? LoveKind.text : LoveKind.promise;
    }

    private static ExpressionType expressionOf(PartnerCheckIn checkIn) {
        return checkIn.getExpression() == null ? null : ExpressionType.valueOf(checkIn.getExpression());
    }

    private static ExpressionType parseExpression(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return ExpressionType.valueOf(value.strip());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_UNKNOWN_SUPPORT);
        }
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
        if (checkIn.getSupports() == null) {
            return List.of();
        }
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
