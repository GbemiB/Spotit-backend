package com.spotit.api.monitoring.service;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.monitoring.dto.CrashReportRequest;
import com.spotit.api.monitoring.entity.AppCrash;
import com.spotit.api.monitoring.entity.FailedAction;
import com.spotit.api.monitoring.repository.AppCrashRepository;
import com.spotit.api.monitoring.repository.FailedActionRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonitoringService {
    static final Duration TOUCH_EVERY = Duration.ofHours(1);

    private final AppCrashRepository appCrashRepository;
    private final FailedActionRepository failedActionRepository;
    private final UserRepository userRepository;
    private final ConfigurationDomainService configurationDomainService;
    private final Map<UUID, Instant> lastTouched = new ConcurrentHashMap<>();

    @Transactional
    public void recordCrash(UUID userId, CrashReportRequest request) {
        Instant now = Instant.now();
        Instant happened = request.happenedAt() == null || request.happenedAt().isAfter(now) ? now : request.happenedAt();
        appCrashRepository.save(AppCrash.builder()
                .userId(userId)
                .message(cut(request.message(), 500))
                .stack(cut(request.stack(), 4000))
                .screen(cut(request.screen(), 60))
                .appVersion(cut(request.appVersion(), 20))
                .platform(cut(request.platform(), 10))
                .deviceModel(cut(request.deviceModel(), 80))
                .osVersion(cut(request.osVersion(), 20))
                .happenedAt(happened)
                .build());
    }

    @Transactional
    public void recordFailure(UUID userId, String action, int status) {
        try {
            failedActionRepository.save(FailedAction.builder().userId(userId).action(cut(action, 120)).status(status)
                    .happenedAt(Instant.now()).build());
        } catch (RuntimeException e) {
            log.warn("Could not record failed action {}: {}", action, e.getMessage());
        }
    }

    @Transactional
    public void touch(UUID userId) {
        Instant now = Instant.now();
        Instant last = lastTouched.get(userId);
        if (last != null && last.plus(TOUCH_EVERY).isAfter(now)) {
            return;
        }
        lastTouched.put(userId, now);
        try {
            userRepository.touchLastActive(userId, now);
        } catch (RuntimeException e) {
            log.warn("Could not update last activity for {}: {}", userId, e.getMessage());
        }
    }

    public String supportEmail() {
        return configurationDomainService.getAppSupportEmail();
    }

    static String cut(String value, int max) {
        if (value == null) {
            return null;
        }
        String trimmed = value.strip();
        return trimmed.length() > max ? trimmed.substring(0, max) : trimmed;
    }
}
