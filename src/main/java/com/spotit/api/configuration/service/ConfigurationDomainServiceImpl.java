package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.configuration.entity.GlobalConfiguration;
import com.spotit.api.configuration.repository.GlobalConfigurationRepository;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigurationDomainServiceImpl implements ConfigurationDomainService {
    private static final int GENERATED_JWT_SECRET_BYTES = 48;
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final Set<String> ENCRYPTED_SECRET_NAMES = Set.of(
            PropertyNames.JWT_SECRET,
            PropertyNames.SMTP_PASSWORD);

    private static final Set<String> REDACTED_NAMES = Set.of(
            PropertyNames.JWT_SECRET,
            PropertyNames.SMTP_PASSWORD,
            PropertyNames.CRYPTO_AES_KEY);

    private final GlobalConfigurationRepository repository;
    private final EncryptionService encryptionService;

    @PostConstruct
    void seedDefaults() {
        seedIfAbsent(PropertyNames.JWT_SECRET, PropertyNames.GROUP_SECURITY, null, generateEncryptedJwtSecret(), "Encrypted JWT signing secret");
        seedIfAbsent(PropertyNames.JWT_ACCESS_TOKEN_TTL_SECONDS, PropertyNames.GROUP_SECURITY, 3600L, null, "JWT access token time-to-live, in seconds");
        seedIfAbsent(PropertyNames.JWT_REFRESH_TOKEN_TTL_SECONDS, PropertyNames.GROUP_SECURITY, 2_592_000L, null, "JWT refresh token time-to-live, in seconds");
        seedIfAbsent(PropertyNames.OTP_TTL_SECONDS, PropertyNames.GROUP_SECURITY, 600L, null, "OTP code time-to-live, in seconds");
        seedIfAbsent(PropertyNames.ADS_DAILY_LIMIT, PropertyNames.GROUP_POINTS, 5L, null, "Max rewarded ad views per user per day");
        seedIfAbsent(PropertyNames.CYCLE_DEFAULT_LENGTH, PropertyNames.GROUP_CYCLE, 28L, null, "Default cycle length assumed until a user has logged enough data");
        seedIfAbsent(PropertyNames.CYCLE_DEFAULT_PERIOD_LENGTH, PropertyNames.GROUP_CYCLE, 5L, null, "Default period length assumed until a user has logged enough data");
        seedIfAbsent(PropertyNames.POINTS_DAILY_CLAIM, PropertyNames.GROUP_POINTS, 50L, null, "Points awarded for the daily claim");
        seedIfAbsent(PropertyNames.POINTS_WATCH_AD, PropertyNames.GROUP_POINTS, 100L, null, "Points awarded for watching a rewarded ad");
        seedIfAbsent(PropertyNames.ACCOUNT_PURGE_GRACE_DAYS, PropertyNames.GROUP_ACCOUNT, 30L, null, "Days after a deletion request before an account is purged");
        seedIfAbsent(PropertyNames.BADGE_KNOW_YOUR_BODY_THRESHOLD, PropertyNames.GROUP_BADGES, 10L, null, "Logs required to earn the Know Your Body badge");
        seedIfAbsent(PropertyNames.BADGE_CYCLE_VETERAN_THRESHOLD, PropertyNames.GROUP_BADGES, 28L, null, "Cycles required to earn the Cycle Veteran badge");
        seedIfAbsent(PropertyNames.BADGE_WEEK_WARRIOR_STREAK_THRESHOLD, PropertyNames.GROUP_BADGES, 7L, null, "Day streak required to earn the Week Warrior badge");
        seedIfAbsent(PropertyNames.CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD, PropertyNames.GROUP_CYCLE, 3L, null, "Logs needed before a cycle's confidence is reported as high");
        seedIfAbsent(PropertyNames.INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS, PropertyNames.GROUP_INSIGHT, 4L, null, "Cycle-length variation, in days, flagged as irregular");
        seedIfAbsent(PropertyNames.INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS, PropertyNames.GROUP_INSIGHT, 3L, null, "Period-length delta, in days, flagged as unusual");
        seedIfAbsent(PropertyNames.INSIGHT_DEFAULT_CYCLES, PropertyNames.GROUP_INSIGHT, 6L, null, "Cycles of history used to compute trends/regularity by default");
        seedIfAbsent(PropertyNames.SUBSCRIPTION_PERIOD_DAYS, PropertyNames.GROUP_BILLING, 30L, null, "Length of one billing period, in days");
        seedIfAbsent(PropertyNames.LOG_MAX_PERIOD_RANGE_DAYS, PropertyNames.GROUP_LOGS, 14L, null, "Max days a single period log entry may span");
        seedIfAbsent(PropertyNames.REWARDS_HISTORY_PAGE_SIZE, PropertyNames.GROUP_REWARDS, 20L, null, "Default page size for points history");
        seedIfAbsent(PropertyNames.CONTENT_FEED_DEFAULT_LIMIT, PropertyNames.GROUP_CONTENT, 10L, null, "Default number of items returned by the content feed");
    }

    private void seedIfAbsent(String name, String groupName, Long value, String stringValue, String description) {
        if (repository.findByName(name).isPresent()) {
            return;
        }
        GlobalConfiguration config = GlobalConfiguration.builder()
                .name(name)
                .groupName(groupName)
                .enabled(true)
                .value(value)
                .stringValue(stringValue)
                .description(description)
                .build();
        repository.save(config);
    }

    private String generateEncryptedJwtSecret() {
        byte[] secretBytes = new byte[GENERATED_JWT_SECRET_BYTES];
        RANDOM.nextBytes(secretBytes);
        String jwtSecret = Base64.getEncoder().encodeToString(secretBytes);
        log.info("Seeded global_configuration.{} with a freshly generated JWT secret — edit the row to change it.", PropertyNames.JWT_SECRET);
        return encryptionService.encrypt(jwtSecret);
    }

    @Override
    @Transactional(readOnly = true)
    public String getJwtSecret() {
        return encryptionService.decrypt(require(PropertyNames.JWT_SECRET).getStringValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long getJwtAccessTokenTtlSeconds() {
        return require(PropertyNames.JWT_ACCESS_TOKEN_TTL_SECONDS).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getJwtRefreshTokenTtlSeconds() {
        return require(PropertyNames.JWT_REFRESH_TOKEN_TTL_SECONDS).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getOtpTtlSeconds() {
        return require(PropertyNames.OTP_TTL_SECONDS).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getAdsDailyLimit() {
        return require(PropertyNames.ADS_DAILY_LIMIT).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getCycleDefaultLength() {
        return require(PropertyNames.CYCLE_DEFAULT_LENGTH).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getCycleDefaultPeriodLength() {
        return require(PropertyNames.CYCLE_DEFAULT_PERIOD_LENGTH).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getPointsDailyClaim() {
        return require(PropertyNames.POINTS_DAILY_CLAIM).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getPointsWatchAd() {
        return require(PropertyNames.POINTS_WATCH_AD).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getAccountPurgeGraceDays() {
        return require(PropertyNames.ACCOUNT_PURGE_GRACE_DAYS).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBadgeKnowYourBodyThreshold() {
        return require(PropertyNames.BADGE_KNOW_YOUR_BODY_THRESHOLD).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBadgeCycleVeteranThreshold() {
        return require(PropertyNames.BADGE_CYCLE_VETERAN_THRESHOLD).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBadgeWeekWarriorStreakThreshold() {
        return require(PropertyNames.BADGE_WEEK_WARRIOR_STREAK_THRESHOLD).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getCycleHighConfidenceLogThreshold() {
        return require(PropertyNames.CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getInsightIrregularVariationThresholdDays() {
        return require(PropertyNames.INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getInsightUnusualPeriodLengthDeltaDays() {
        return require(PropertyNames.INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getInsightDefaultCycles() {
        return require(PropertyNames.INSIGHT_DEFAULT_CYCLES).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getSubscriptionPeriodDays() {
        return require(PropertyNames.SUBSCRIPTION_PERIOD_DAYS).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getLogMaxPeriodRangeDays() {
        return require(PropertyNames.LOG_MAX_PERIOD_RANGE_DAYS).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getRewardsHistoryPageSize() {
        return require(PropertyNames.REWARDS_HISTORY_PAGE_SIZE).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public int getContentFeedDefaultLimit() {
        return require(PropertyNames.CONTENT_FEED_DEFAULT_LIMIT).getValue().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResolvedSmtpSettings> getSmtpSettings() {
        var host = repository.findByName(PropertyNames.SMTP_HOST);
        if (host.isEmpty() || host.get().getStringValue() == null || host.get().getStringValue().isBlank()) {
            return Optional.empty();
        }
        String username = stringValueOrNull(PropertyNames.SMTP_USERNAME);
        String encryptedPassword = stringValueOrNull(PropertyNames.SMTP_PASSWORD);
        String fromAddress = stringValueOrNull(PropertyNames.SMTP_FROM_ADDRESS);
        Long port = valueOrNull(PropertyNames.SMTP_PORT);
        boolean useTls = repository.findByName(PropertyNames.SMTP_USE_TLS).map(GlobalConfiguration::isEnabled).orElse(true);

        return Optional.of(new ResolvedSmtpSettings(host.get().getStringValue(),
                port == null ? 587 : port.intValue(), username, encryptedPassword == null ? null : encryptionService.decrypt(encryptedPassword),
                fromAddress, useTls));
    }

    @Override
    @Transactional
    public void saveSmtpSettings(String host, int port, String username, String password, String fromAddress, boolean useTls) {
        upsert(PropertyNames.SMTP_HOST, null, host, "SMTP host used to send all transactional mail");
        upsert(PropertyNames.SMTP_PORT, (long) port, null, "SMTP port");
        upsert(PropertyNames.SMTP_USERNAME, null, username, "SMTP username");
        upsert(PropertyNames.SMTP_FROM_ADDRESS, null, fromAddress, "From address for outgoing mail");
        upsertEnabled(PropertyNames.SMTP_USE_TLS, useTls, "Whether the SMTP relay uses TLS");

        if (password != null && !password.isBlank()) {
            upsert(PropertyNames.SMTP_PASSWORD, null, encryptionService.encrypt(password), "Encrypted SMTP password");
        } else if (repository.findByName(PropertyNames.SMTP_PASSWORD).isEmpty()) {
            throw new IllegalArgumentException("password is required when creating SMTP settings for the first time");
        }
    }

    private void upsert(String name, Long value, String stringValue, String description) {
        GlobalConfiguration config = repository.findByName(name)
                .orElseGet(() -> GlobalConfiguration.builder().name(name).groupName(PropertyNames.GROUP_SMTP).enabled(true).build());
        config.setValue(value);
        config.setStringValue(stringValue);
        config.setDescription(description);
        repository.save(config);
    }

    private void upsertEnabled(String name, boolean enabled, String description) {
        GlobalConfiguration config = repository.findByName(name)
                .orElseGet(() -> GlobalConfiguration.builder().name(name).groupName(PropertyNames.GROUP_SMTP).build());
        config.setEnabled(enabled);
        config.setDescription(description);
        repository.save(config);
    }

    private String stringValueOrNull(String name) {
        return repository.findByName(name).map(GlobalConfiguration::getStringValue).orElse(null);
    }

    private Long valueOrNull(String name) {
        return repository.findByName(name).map(GlobalConfiguration::getValue).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigurationResponse> listAll() {
        return repository.findAllByOrderByNameAsc().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listGroupNames() {
        return repository.findDistinctGroupNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigurationResponse> listByGroup(String groupName) {
        return repository.findByGroupNameOrderByNameAsc(groupName).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GlobalConfigurationResponse getByName(String name) {
        return toResponse(require(name));
    }

    @Override
    @Transactional
    public GlobalConfigurationResponse update(String name, UpdateGlobalConfigurationRequest request) {
        if (PropertyNames.CRYPTO_AES_KEY.equals(name) && request.stringValue() != null) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR,
                    "crypto-aes-key can't be changed via this endpoint — rotating it would strand every secret already encrypted with the old key.");
        }
        GlobalConfiguration config = require(name);
        if (request.groupName() != null) {
            config.setGroupName(request.groupName());
        }
        if (request.enabled() != null) {
            config.setEnabled(request.enabled());
        }
        if (request.value() != null) {
            config.setValue(request.value());
        }
        if (request.dateValue() != null) {
            config.setDateValue(request.dateValue());
        }
        if (request.stringValue() != null) {
            config.setStringValue(isEncryptedSecret(name) ? encryptionService.encrypt(request.stringValue()) : request.stringValue());
        }
        if (request.description() != null) {
            config.setDescription(request.description());
        }
        return toResponse(repository.save(config));
    }

    private GlobalConfiguration require(String name) {
        return repository.findByName(name).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "No such configuration property: " + name));
    }

    private static boolean isEncryptedSecret(String name) {
        return ENCRYPTED_SECRET_NAMES.contains(name);
    }

    private static boolean isRedacted(String name) {
        return REDACTED_NAMES.contains(name);
    }

    private GlobalConfigurationResponse toResponse(GlobalConfiguration config) {
        return new GlobalConfigurationResponse(config.getId(), config.getName(), config.getGroupName(), config.isEnabled(), config.getValue(),
                config.getDateValue(), isRedacted(config.getName()) ? null : config.getStringValue(), config.getDescription());
    }
}
