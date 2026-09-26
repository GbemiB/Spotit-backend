package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.ConfigDefaults;
import com.spotit.api.configuration.ConfigPropertyCatalog;
import com.spotit.api.configuration.ConfigPropertyCatalog.Context;
import com.spotit.api.configuration.ConfigPropertyCatalog.Property;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

// @DependsOn: ConfigStoreMigration must finish copying any already-deployed global_configuration
// rows into security_config/global_config before this bean's @PostConstruct seeder
// runs, otherwise the seeder would write fresh defaults on top of a database that already had
// real, admin-tuned values.
@Service
@Slf4j
@DependsOn("configStoreMigration")
@RequiredArgsConstructor
public class ConfigurationDomainServiceImpl implements ConfigurationDomainService {
    private static final int GENERATED_JWT_SECRET_BYTES = 48;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SecurityConfigRepository securityConfigRepository;
    private final GlobalConfigRepository globalConfigRepository;
    private final EncryptionService encryptionService;
    private final ConfigPropertyCatalog catalog;

    @PostConstruct
    void seedDefaults() {
        SecurityConfig security = loadSecurity();
        if (security.getJwtSecret() == null) {
            security.setJwtSecret(generateEncryptedJwtSecret());
        }
        if (security.getJwtAccessTokenTtlSeconds() == 0) {
            security.setJwtAccessTokenTtlSeconds(ConfigDefaults.JWT_ACCESS_TOKEN_TTL_SECONDS);
        }
        if (security.getJwtRefreshTokenTtlSeconds() == 0) {
            security.setJwtRefreshTokenTtlSeconds(ConfigDefaults.JWT_REFRESH_TOKEN_TTL_SECONDS);
        }
        if (security.getOtpTtlSeconds() == 0) {
            security.setOtpTtlSeconds(ConfigDefaults.OTP_TTL_SECONDS);
        }
        securityConfigRepository.save(security);

        GlobalConfig global = loadGlobal();
        if (global.getAdsDailyLimit() == 0) {
            global.setAdsDailyLimit(ConfigDefaults.ADS_DAILY_LIMIT);
        }
        if (global.getCycleDefaultLength() == 0) {
            global.setCycleDefaultLength(ConfigDefaults.CYCLE_DEFAULT_LENGTH);
        }
        if (global.getCycleDefaultPeriodLength() == 0) {
            global.setCycleDefaultPeriodLength(ConfigDefaults.CYCLE_DEFAULT_PERIOD_LENGTH);
        }
        if (global.getPointsDailyClaim() == 0) {
            global.setPointsDailyClaim(ConfigDefaults.POINTS_DAILY_CLAIM);
        }
        if (global.getPointsWatchAd() == 0) {
            global.setPointsWatchAd(ConfigDefaults.POINTS_WATCH_AD);
        }
        if (global.getAccountPurgeGraceDays() == 0) {
            global.setAccountPurgeGraceDays(ConfigDefaults.ACCOUNT_PURGE_GRACE_DAYS);
        }
        if (global.getBadgeKnowYourBodyThreshold() == 0) {
            global.setBadgeKnowYourBodyThreshold(ConfigDefaults.BADGE_KNOW_YOUR_BODY_THRESHOLD);
        }
        if (global.getBadgeCycleVeteranThreshold() == 0) {
            global.setBadgeCycleVeteranThreshold(ConfigDefaults.BADGE_CYCLE_VETERAN_THRESHOLD);
        }
        if (global.getBadgeWeekWarriorStreakThreshold() == 0) {
            global.setBadgeWeekWarriorStreakThreshold(ConfigDefaults.BADGE_WEEK_WARRIOR_STREAK_THRESHOLD);
        }
        if (global.getCycleHighConfidenceLogThreshold() == 0) {
            global.setCycleHighConfidenceLogThreshold(ConfigDefaults.CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD);
        }
        if (global.getInsightIrregularVariationThresholdDays() == 0) {
            global.setInsightIrregularVariationThresholdDays(ConfigDefaults.INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS);
        }
        if (global.getInsightUnusualPeriodLengthDeltaDays() == 0) {
            global.setInsightUnusualPeriodLengthDeltaDays(ConfigDefaults.INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS);
        }
        if (global.getInsightDefaultCycles() == 0) {
            global.setInsightDefaultCycles(ConfigDefaults.INSIGHT_DEFAULT_CYCLES);
        }
        if (global.getSubscriptionPeriodDays() == 0) {
            global.setSubscriptionPeriodDays(ConfigDefaults.SUBSCRIPTION_PERIOD_DAYS);
        }
        if (global.getLogMaxPeriodRangeDays() == 0) {
            global.setLogMaxPeriodRangeDays(ConfigDefaults.LOG_MAX_PERIOD_RANGE_DAYS);
        }
        if (global.getRewardsHistoryPageSize() == 0) {
            global.setRewardsHistoryPageSize(ConfigDefaults.REWARDS_HISTORY_PAGE_SIZE);
        }
        if (global.getContentFeedDefaultLimit() == 0) {
            global.setContentFeedDefaultLimit(ConfigDefaults.CONTENT_FEED_DEFAULT_LIMIT);
        }
        globalConfigRepository.save(global);
    }

    private String generateEncryptedJwtSecret() {
        byte[] secretBytes = new byte[GENERATED_JWT_SECRET_BYTES];
        RANDOM.nextBytes(secretBytes);
        String jwtSecret = Base64.getEncoder().encodeToString(secretBytes);
        log.info("Seeded security_config.jwt_secret with a freshly generated JWT secret — edit it via PATCH /api/v1/config/global/jwt-secret to change it.");
        return encryptionService.encrypt(jwtSecret);
    }

    private SecurityConfig loadSecurity() {
        return securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)
                .orElseGet(() -> SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).build());
    }

    private GlobalConfig loadGlobal() {
        return globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)
                .orElseGet(() -> GlobalConfig.builder().id(GlobalConfig.SINGLETON_ID).build());
    }

    private Context loadContext() {
        return new Context(loadSecurity(), loadGlobal());
    }

    @Override
    @Transactional(readOnly = true)
    public String getJwtSecret() {
        return encryptionService.decrypt(loadSecurity().getJwtSecret());
    }

    @Override
    @Transactional(readOnly = true)
    public long getJwtAccessTokenTtlSeconds() {
        return loadSecurity().getJwtAccessTokenTtlSeconds();
    }

    @Override
    @Transactional(readOnly = true)
    public long getJwtRefreshTokenTtlSeconds() {
        return loadSecurity().getJwtRefreshTokenTtlSeconds();
    }

    @Override
    @Transactional(readOnly = true)
    public long getOtpTtlSeconds() {
        return loadSecurity().getOtpTtlSeconds();
    }

    @Override
    @Transactional(readOnly = true)
    public int getAdsDailyLimit() {
        return (int) loadGlobal().getAdsDailyLimit();
    }

    @Override
    @Transactional(readOnly = true)
    public int getCycleDefaultLength() {
        return (int) loadGlobal().getCycleDefaultLength();
    }

    @Override
    @Transactional(readOnly = true)
    public int getCycleDefaultPeriodLength() {
        return (int) loadGlobal().getCycleDefaultPeriodLength();
    }

    @Override
    @Transactional(readOnly = true)
    public int getPointsDailyClaim() {
        return (int) loadGlobal().getPointsDailyClaim();
    }

    @Override
    @Transactional(readOnly = true)
    public int getPointsWatchAd() {
        return (int) loadGlobal().getPointsWatchAd();
    }

    @Override
    @Transactional(readOnly = true)
    public long getAccountPurgeGraceDays() {
        return loadGlobal().getAccountPurgeGraceDays();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBadgeKnowYourBodyThreshold() {
        return (int) loadGlobal().getBadgeKnowYourBodyThreshold();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBadgeCycleVeteranThreshold() {
        return (int) loadGlobal().getBadgeCycleVeteranThreshold();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBadgeWeekWarriorStreakThreshold() {
        return (int) loadGlobal().getBadgeWeekWarriorStreakThreshold();
    }

    @Override
    @Transactional(readOnly = true)
    public long getCycleHighConfidenceLogThreshold() {
        return loadGlobal().getCycleHighConfidenceLogThreshold();
    }

    @Override
    @Transactional(readOnly = true)
    public int getInsightIrregularVariationThresholdDays() {
        return (int) loadGlobal().getInsightIrregularVariationThresholdDays();
    }

    @Override
    @Transactional(readOnly = true)
    public int getInsightUnusualPeriodLengthDeltaDays() {
        return (int) loadGlobal().getInsightUnusualPeriodLengthDeltaDays();
    }

    @Override
    @Transactional(readOnly = true)
    public int getInsightDefaultCycles() {
        return (int) loadGlobal().getInsightDefaultCycles();
    }

    @Override
    @Transactional(readOnly = true)
    public long getSubscriptionPeriodDays() {
        return loadGlobal().getSubscriptionPeriodDays();
    }

    @Override
    @Transactional(readOnly = true)
    public int getLogMaxPeriodRangeDays() {
        return (int) loadGlobal().getLogMaxPeriodRangeDays();
    }

    @Override
    @Transactional(readOnly = true)
    public int getRewardsHistoryPageSize() {
        return (int) loadGlobal().getRewardsHistoryPageSize();
    }

    @Override
    @Transactional(readOnly = true)
    public int getContentFeedDefaultLimit() {
        return (int) loadGlobal().getContentFeedDefaultLimit();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigurationResponse> listAll() {
        Context context = loadContext();
        return catalog.all().stream().map(property -> toResponse(property, context)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listGroupNames() {
        return catalog.groups();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigurationResponse> listByGroup(String groupName) {
        Context context = loadContext();
        return catalog.inGroup(groupName).stream().map(property -> toResponse(property, context)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GlobalConfigurationResponse getByName(String name) {
        return toResponse(requireProperty(name), loadContext());
    }

    @Override
    @Transactional
    public GlobalConfigurationResponse update(String name, UpdateGlobalConfigurationRequest request) {
        if (PropertyNames.CRYPTO_AES_KEY.equals(name) && request.stringValue() != null) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR,
                    "crypto-aes-key can't be changed via this endpoint — rotating it would strand every secret already encrypted with the old key.");
        }
        Property property = requireProperty(name);
        Context context = loadContext();

        switch (property.kind()) {
            case LONG -> {
                if (request.value() != null) {
                    property.write(context, request.value());
                }
            }
            case BOOL -> {
                if (request.enabled() != null) {
                    property.write(context, request.enabled());
                }
            }
            case STRING -> {
                if (request.stringValue() != null) {
                    property.write(context, property.encrypted()
                            ? encryptionService.encrypt(request.stringValue())
                            : request.stringValue());
                }
            }
        }
        persist(context, property.section());
        return toResponse(property, context);
    }

    private void persist(Context context, ConfigPropertyCatalog.Section section) {
        switch (section) {
            case SECURITY -> securityConfigRepository.save(context.security());
            case GLOBAL -> globalConfigRepository.save(context.global());
        }
    }

    private Property requireProperty(String name) {
        return catalog.find(name)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "No such configuration property: " + name));
    }

    private GlobalConfigurationResponse toResponse(Property property, Context context) {
        Object raw = property.read(context);
        Long value = property.kind() == ConfigPropertyCatalog.Kind.LONG && raw != null ? ((Number) raw).longValue() : null;
        boolean enabled = property.kind() != ConfigPropertyCatalog.Kind.BOOL || Boolean.TRUE.equals(raw);
        String stringValue = property.kind() == ConfigPropertyCatalog.Kind.STRING && !property.redacted() ? (String) raw : null;
        return new GlobalConfigurationResponse(synthId(property.name()), property.name(), property.group(), enabled,
                value, null, stringValue, property.description());
    }

    // There is no longer one DB row (hence one UUID) per property. The admin API still exposes an
    // `id`, so derive a stable one from the property name — same name always yields the same UUID.
    private static UUID synthId(String name) {
        return UUID.nameUUIDFromBytes(("global-config:" + name).getBytes(StandardCharsets.UTF_8));
    }
}
