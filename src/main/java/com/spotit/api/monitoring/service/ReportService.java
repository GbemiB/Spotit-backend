package com.spotit.api.monitoring.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.monitoring.dto.CrashItem;
import com.spotit.api.monitoring.dto.CrashPage;
import com.spotit.api.monitoring.dto.ReportSummaryResponse;
import com.spotit.api.monitoring.entity.AppCrash;
import com.spotit.api.monitoring.entity.FailedAction;
import com.spotit.api.monitoring.repository.AppCrashRepository;
import com.spotit.api.monitoring.repository.FailedActionRepository;
import com.spotit.api.partner.repository.PartnerCheckInRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    static final int MAX_RANGE_DAYS = 366;
    static final int MAX_PAGE_SIZE = 200;
    static final int TOP_CRASHES = 10;

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final AppCrashRepository appCrashRepository;
    private final FailedActionRepository failedActionRepository;
    private final PartnerCheckInRepository partnerCheckInRepository;

    @Transactional(readOnly = true)
    public ReportSummaryResponse summary(LocalDate from, LocalDate to, int page, int size) {
        check(from, to);
        Instant start = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        return new ReportSummaryResponse(from, to, downloads(start, end), userRepository.countByCreatedAtBetween(start, end),
                active(end), crashes(start, end), failures(start, end), lastActivity(page, size));
    }

    @Transactional(readOnly = true)
    public CrashPage crashes(LocalDate from, LocalDate to, int page, int size) {
        check(from, to);
        int pageSize = pageSize(size);
        Page<AppCrash> result = appCrashRepository.findByHappenedAtBetweenOrderByHappenedAtDesc(
                from.atStartOfDay(ZoneOffset.UTC).toInstant(), to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant(),
                PageRequest.of(Math.max(page, 0), pageSize));
        return new CrashPage(Math.max(page, 0), pageSize, result.getTotalElements(), result.getContent().stream()
                .map(c -> new CrashItem(c.getId(), c.getUserId(), c.getMessage(), c.getStack(), c.getScreen(), c.getAppVersion(),
                        c.getPlatform(), c.getDeviceModel(), c.getOsVersion(), c.getHappenedAt()))
                .toList());
    }

    private ReportSummaryResponse.Downloads downloads(Instant start, Instant end) {
        List<User> users = userRepository.findFirstOnboardedBetween(start, end);
        long partners = users.stream().filter(u -> u.getAccountType() != null && u.getAccountType().isPartner()).count();
        Map<UUID, String> platformByUser = new HashMap<>();
        List<UUID> ids = users.stream().map(User::getId).toList();
        if (!ids.isEmpty()) {
            for (Device device : deviceRepository.findByUserIdIn(ids)) {
                if (device.getPlatform() != null) {
                    platformByUser.putIfAbsent(device.getUserId(), device.getPlatform().name().toLowerCase());
                }
            }
        }
        Map<String, Long> byPlatform = new LinkedHashMap<>();
        byPlatform.put("android", 0L);
        byPlatform.put("ios", 0L);
        byPlatform.put("unknown", 0L);
        for (User user : users) {
            byPlatform.merge(platformByUser.getOrDefault(user.getId(), "unknown"), 1L, Long::sum);
        }
        return new ReportSummaryResponse.Downloads(users.size(), users.size() - partners, partners, byPlatform);
    }

    private ReportSummaryResponse.Active active(Instant end) {
        return new ReportSummaryResponse.Active(
                userRepository.countByLastActiveAtAfter(end.minus(Duration.ofDays(1))),
                userRepository.countByLastActiveAtAfter(end.minus(Duration.ofDays(7))),
                userRepository.countByLastActiveAtAfter(end.minus(Duration.ofDays(30))));
    }

    private ReportSummaryResponse.Crashes crashes(Instant start, Instant end) {
        List<AppCrash> crashes = appCrashRepository.findByHappenedAtBetween(start, end);
        Map<String, List<AppCrash>> byMessage = crashes.stream().collect(Collectors.groupingBy(AppCrash::getMessage));
        List<ReportSummaryResponse.TopCrash> top = byMessage.entrySet().stream()
                .map(e -> {
                    AppCrash latest = e.getValue().stream().max(Comparator.comparing(AppCrash::getHappenedAt)).orElseThrow();
                    return new ReportSummaryResponse.TopCrash(e.getKey(), e.getValue().size(), latest.getAppVersion(), latest.getHappenedAt());
                })
                .sorted(Comparator.comparingLong(ReportSummaryResponse.TopCrash::count).reversed()
                        .thenComparing(ReportSummaryResponse.TopCrash::lastSeen, Comparator.reverseOrder()))
                .limit(TOP_CRASHES)
                .toList();
        return new ReportSummaryResponse.Crashes(crashes.size(), top);
    }

    private ReportSummaryResponse.Failures failures(Instant start, Instant end) {
        List<FailedAction> failures = failedActionRepository.findByHappenedAtBetween(start, end);
        Map<String, Long> byAction = failures.stream()
                .collect(Collectors.groupingBy(FailedAction::getAction, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
        return new ReportSummaryResponse.Failures(failures.size(), byAction);
    }

    private ReportSummaryResponse.LastActivityPage lastActivity(int page, int size) {
        int pageSize = pageSize(size);
        Page<User> users = userRepository.findByLastActive(PageRequest.of(Math.max(page, 0), pageSize));
        List<UUID> partnerIds = users.getContent().stream()
                .filter(u -> u.getAccountType() != null && u.getAccountType().isPartner())
                .map(User::getId).toList();
        Map<UUID, Instant> lastCheckIn = new HashMap<>();
        if (!partnerIds.isEmpty()) {
            for (Object[] row : partnerCheckInRepository.findLatestByPartnerIds(partnerIds)) {
                lastCheckIn.put((UUID) row[0], (Instant) row[1]);
            }
        }
        List<ReportSummaryResponse.UserActivity> rows = users.getContent().stream()
                .map(u -> new ReportSummaryResponse.UserActivity(u.getId(), u.getFirstName(), u.getEmail(),
                        u.getAccountType() == null ? null : u.getAccountType().name(), u.getLastActiveAt(), u.getLastLogDate(),
                        u.getLastPeriodLogDate(), lastCheckIn.get(u.getId())))
                .filter(Objects::nonNull)
                .toList();
        return new ReportSummaryResponse.LastActivityPage(Math.max(page, 0), pageSize, users.getTotalElements(), rows);
    }

    private static int pageSize(int size) {
        return size <= 0 ? 50 : Math.min(size, MAX_PAGE_SIZE);
    }

    private static void check(LocalDate from, LocalDate to) {
        if (from.isAfter(to) || from.plusDays(MAX_RANGE_DAYS).isBefore(to)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.INVALID_DATE_RANGE);
        }
    }
}
