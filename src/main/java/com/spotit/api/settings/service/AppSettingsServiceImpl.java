package com.spotit.api.settings.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.settings.entity.AppSettings;
import com.spotit.api.settings.repository.AppSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppSettingsServiceImpl implements AppSettingsService {

    // Same shape as `openssl rand -base64 48` — 48 random bytes is comfortably enough for HS512.
    private static final int GENERATED_JWT_SECRET_BYTES = 48;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final AppSettingsRepository repository;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public ResolvedAppSettings getActiveSettings() {
        AppSettings settings = repository.findTopByOrderByUpdatedAtDesc().orElseGet(this::seedDefaults);
        return new ResolvedAppSettings(
                encryptionService.decrypt(settings.getEncryptedJwtSecret()),
                settings.getJwtAccessTokenTtlSeconds(),
                settings.getJwtRefreshTokenTtlSeconds(),
                settings.getOtpTtlSeconds(),
                settings.getAdsDailyLimit(),
                settings.getCycleDefaultLength(),
                settings.getCycleDefaultPeriodLength(),
                settings.getPointsDailyClaim(),
                settings.getPointsWatchAd());
    }

    private AppSettings seedDefaults() {
        byte[] secretBytes = new byte[GENERATED_JWT_SECRET_BYTES];
        RANDOM.nextBytes(secretBytes);
        String jwtSecret = Base64.getEncoder().encodeToString(secretBytes);

        AppSettings settings = AppSettings.builder()
                .encryptedJwtSecret(encryptionService.encrypt(jwtSecret))
                .jwtAccessTokenTtlSeconds(3600)
                .jwtRefreshTokenTtlSeconds(2_592_000)
                .otpTtlSeconds(600)
                .adsDailyLimit(5)
                .cycleDefaultLength(28)
                .cycleDefaultPeriodLength(5)
                .pointsDailyClaim(50)
                .pointsWatchAd(100)
                .build();
        settings = repository.save(settings);
        log.info("Seeded app_settings with defaults and a freshly generated JWT secret — edit the row to change them.");
        return settings;
    }
}
