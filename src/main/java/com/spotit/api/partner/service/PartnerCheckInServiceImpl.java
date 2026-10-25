package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;
import com.spotit.api.partner.dto.PartnerLoveItem;
import com.spotit.api.partner.dto.PartnerLoveResponse;
import com.spotit.api.partner.dto.LoveOptionView;
import com.spotit.api.partner.entity.PartnerCheckIn;
import com.spotit.api.partner.entity.LoveKind;
import com.spotit.api.partner.entity.LoveOption;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
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
    private final PartnerCheckInRepository partnerCheckInRepository;
    private final PartnerLinkRepository partnerLinkRepository;
    private final UserRepository userRepository;
    private final PointsWriteService pointsWriteService;
    private final ChallengeReadService challengeReadService;
    private final UserClock userClock;
    private final PartnerLoveNotifier partnerLoveNotifier;
    private final LoveOptionService loveOptionService;

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
        List<LoveOption> supports = parseSupports(request.supports() == null ? List.of() : request.supports());
        String note = request.note() == null || request.note().isBlank() ? null : request.note().strip();
        LoveOption expression = parseExpression(request.expression());
        LoveKind kind = kindOf(request.kind(), supports, note, expression);
        LocalDate today = userClock.today(partner);
        PartnerCheckIn saved = partnerCheckInRepository.save(PartnerCheckIn.builder()
                .partnerId(partnerId)
                .ownerId(link.getOwnerId())
                .checkInDate(today)
                .kind(kind.name())
                .supports(kind == LoveKind.promise
                        ? supports.stream().map(o -> String.valueOf(o.getStoredValue())).collect(Collectors.joining(",")) : "")
                .note(kind == LoveKind.expression ? null : note)
                .expression(kind == LoveKind.expression ? expression.getId() : null)
                .build());
        PointsWriteService.LogPointsResult points = pointsWriteService.recordCheckIn(partnerId);
        List<String> labels = kind == LoveKind.promise ? supports.stream().map(LoveOption::getLabel).toList() : List.of();
        userRepository.findById(link.getOwnerId())
                .ifPresent(owner -> partnerLoveNotifier.notifyOwner(owner, partner, kind, labels, saved.getNote(),
                        kind == LoveKind.expression ? expression : null));
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
        Map<String, LoveOption> promises = loveOptionService.allOf(LoveOption.PROMISE);
        Map<String, LoveOption> expressions = loveOptionService.allOf(LoveOption.EXPRESSION);
        return new PartnerLoveResponse(checkIns.stream().map(checkIn -> {
            List<LoveOption> chosen = supportsOf(checkIn, promises);
            LoveOption expression = checkIn.getExpression() == null ? null : expressions.get(checkIn.getExpression());
            return new PartnerLoveItem(checkIn.getCheckInDate(), names.getOrDefault(checkIn.getPartnerId(), ""), kindOf(checkIn).name(),
                    chosen.stream().map(LoveOption::getId).toList(), chosen.stream().map(LoveOption::getLabel).toList(), checkIn.getNote(),
                    checkIn.getExpression(), expression == null ? null : expression.getLabel(), expression == null ? null : expression.getEmoji(),
                    checkIn.getCreatedAt());
        }).toList());
    }

    private PartnerCheckInResponse response(List<PartnerCheckIn> today, PartnerCheckIn last, int streak, int pointsAwarded, long balance) {
        Map<String, Integer> byKind = new LinkedHashMap<>();
        for (LoveKind kind : LoveKind.values()) {
            byKind.put(kind.name(), 0);
        }
        today.forEach(checkIn -> byKind.merge(kindOf(checkIn).name(), 1, Integer::sum));
        Map<String, LoveOption> promises = loveOptionService.allOf(LoveOption.PROMISE);
        List<String> lastSupports = last == null ? List.of() : supportsOf(last, promises).stream().map(LoveOption::getId).toList();
        return new PartnerCheckInResponse(loveOptionService.active(LoveOption.PROMISE), !today.isEmpty(), lastSupports,
                challengeReadService.getDailyLogReward(), streak, pointsAwarded, balance, last == null ? null : last.getNote(),
                loveOptionService.active(LoveOption.EXPRESSION), last == null ? null : kindOf(last).name(),
                last == null ? null : last.getExpression(), today.size(), byKind,
                loveOptionService.active(LoveOption.STARTER).stream().map(LoveOptionView::label).toList(),
                loveOptionService.active(LoveOption.CATEGORY));
    }

    private static LoveKind kindOf(String requested, List<LoveOption> supports, String note, LoveOption expression) {
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

    private LoveOption parseExpression(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return loveOptionService.activeOption(LoveOption.EXPRESSION, value.strip())
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_UNKNOWN_SUPPORT));
    }

    private List<LoveOption> parseSupports(List<String> ids) {
        return ids.stream().map(String::strip).distinct()
                .map(id -> loveOptionService.activeOption(LoveOption.PROMISE, id)
                        .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PARTNER_UNKNOWN_SUPPORT)))
                .toList();
    }

    private static List<LoveOption> supportsOf(PartnerCheckIn checkIn, Map<String, LoveOption> promises) {
        if (checkIn.getSupports() == null || checkIn.getSupports().isBlank()) {
            return List.of();
        }
        Map<Integer, LoveOption> byValue = promises.values().stream().filter(o -> o.getStoredValue() != null)
                .collect(Collectors.toMap(LoveOption::getStoredValue, o -> o, (a, b) -> a));
        return Arrays.stream(checkIn.getSupports().split(","))
                .filter(value -> !value.isBlank())
                .map(value -> byValue.get(Integer.valueOf(value.strip())))
                .filter(java.util.Objects::nonNull)
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
