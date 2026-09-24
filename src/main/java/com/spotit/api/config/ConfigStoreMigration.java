package com.spotit.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.entity.SmtpConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import com.spotit.api.configuration.repository.SmtpConfigRepository;
import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.LevelDefinition;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.LevelDefinitionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * One-time, one-way migration off the old single {@code global_configuration} table.
 *
 * <p>History: app_settings / smtp_settings and, briefly, badge/challenge/level "definition" rows
 * were all folded into {@code global_configuration}. That table is now split into typed tables —
 * {@link SecurityConfig} ({@code security_config}), {@link SmtpConfig} ({@code smtp_config}),
 * {@link GlobalConfig} ({@code global_config}), and {@code badge_config} / {@code challenge_config}
 * / {@code level_config} for the definitions. Hibernate's {@code ddl-auto: update} creates the new
 * tables but never drops the old one, and never moves data, so this runner does both.
 *
 * <p>It runs in {@code @PostConstruct} and every consumer of config
 * ({@code ConfigurationDomainServiceImpl}, {@code AesGcmEncryptionService}) is
 * {@code @DependsOn("configStoreMigration")}, so the copy always finishes before anything seeds
 * defaults or reads a key. Idempotent: once {@code global_configuration} is dropped the body is a
 * no-op, and a half-finished run just re-copies (existing target rows/definitions are left alone).
 */
@Component("configStoreMigration")
@Slf4j
@RequiredArgsConstructor
public class ConfigStoreMigration {

    private final JdbcTemplate jdbcTemplate;
    private final SecurityConfigRepository securityConfigRepository;
    private final SmtpConfigRepository smtpConfigRepository;
    private final GlobalConfigRepository globalConfigRepository;
    private final BadgeDefinitionRepository badgeDefinitionRepository;
    private final ChallengeDefinitionRepository challengeDefinitionRepository;
    private final LevelDefinitionRepository levelDefinitionRepository;
    private final ObjectMapper objectMapper;

    private record LegacyRow(Long value, String stringValue, Boolean enabled) {
    }

    @PostConstruct
    void run() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS app_settings");
        jdbcTemplate.execute("DROP TABLE IF EXISTS smtp_settings");
        jdbcTemplate.execute("DROP TABLE IF EXISTS badge_definitions");
        jdbcTemplate.execute("DROP TABLE IF EXISTS challenge_definitions");
        jdbcTemplate.execute("DROP TABLE IF EXISTS level_definitions");

        if (!tableExists("global_configuration")) {
            return;
        }

        Map<String, LegacyRow> rows = loadLegacyRows();
        migrateSecurity(rows);
        migrateSmtp(rows);
        migrateGlobal(rows);
        int defs = migrateDefinitions();

        jdbcTemplate.execute("DROP TABLE IF EXISTS global_configuration");
        log.info("Migrated {} scalar row(s) and {} definition(s) out of global_configuration into the typed config tables, then dropped it.",
                rows.size(), defs);
    }

    private Map<String, LegacyRow> loadLegacyRows() {
        Map<String, LegacyRow> rows = new HashMap<>();
        for (Map<String, Object> row : jdbcTemplate.queryForList(
                "SELECT name, value, string_value, enabled FROM global_configuration")) {
            String name = (String) row.get("name");
            Object value = row.get("value");
            Object enabled = row.get("enabled");
            rows.put(name, new LegacyRow(
                    value == null ? null : ((Number) value).longValue(),
                    (String) row.get("string_value"),
                    enabled == null ? null : (Boolean) enabled));
        }
        return rows;
    }

    private void migrateSecurity(Map<String, LegacyRow> rows) {
        SecurityConfig security = securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)
                .orElseGet(() -> SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).build());
        applyString(rows, PropertyNames.JWT_SECRET, security::setJwtSecret);
        applyLong(rows, PropertyNames.JWT_ACCESS_TOKEN_TTL_SECONDS, security::setJwtAccessTokenTtlSeconds);
        applyLong(rows, PropertyNames.JWT_REFRESH_TOKEN_TTL_SECONDS, security::setJwtRefreshTokenTtlSeconds);
        applyLong(rows, PropertyNames.OTP_TTL_SECONDS, security::setOtpTtlSeconds);
        applyString(rows, PropertyNames.CRYPTO_AES_KEY, security::setCryptoAesKey);
        securityConfigRepository.save(security);
    }

    private void migrateSmtp(Map<String, LegacyRow> rows) {
        SmtpConfig smtp = smtpConfigRepository.findById(SmtpConfig.SINGLETON_ID)
                .orElseGet(() -> SmtpConfig.builder().id(SmtpConfig.SINGLETON_ID).build());
        applyString(rows, PropertyNames.SMTP_HOST, smtp::setHost);
        applyLong(rows, PropertyNames.SMTP_PORT, v -> smtp.setPort((int) v));
        applyString(rows, PropertyNames.SMTP_USERNAME, smtp::setUsername);
        applyString(rows, PropertyNames.SMTP_PASSWORD, smtp::setPassword);
        applyString(rows, PropertyNames.SMTP_FROM_ADDRESS, smtp::setFromAddress);
        LegacyRow tls = rows.get(PropertyNames.SMTP_USE_TLS);
        if (tls != null && tls.enabled() != null) {
            smtp.setUseTls(tls.enabled());
        }
        smtpConfigRepository.save(smtp);
    }

    private void migrateGlobal(Map<String, LegacyRow> rows) {
        GlobalConfig global = globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)
                .orElseGet(() -> GlobalConfig.builder().id(GlobalConfig.SINGLETON_ID).build());
        applyLong(rows, PropertyNames.ADS_DAILY_LIMIT, global::setAdsDailyLimit);
        applyLong(rows, PropertyNames.CYCLE_DEFAULT_LENGTH, global::setCycleDefaultLength);
        applyLong(rows, PropertyNames.CYCLE_DEFAULT_PERIOD_LENGTH, global::setCycleDefaultPeriodLength);
        applyLong(rows, PropertyNames.POINTS_DAILY_CLAIM, global::setPointsDailyClaim);
        applyLong(rows, PropertyNames.POINTS_WATCH_AD, global::setPointsWatchAd);
        applyLong(rows, PropertyNames.ACCOUNT_PURGE_GRACE_DAYS, global::setAccountPurgeGraceDays);
        applyLong(rows, PropertyNames.BADGE_KNOW_YOUR_BODY_THRESHOLD, global::setBadgeKnowYourBodyThreshold);
        applyLong(rows, PropertyNames.BADGE_CYCLE_VETERAN_THRESHOLD, global::setBadgeCycleVeteranThreshold);
        applyLong(rows, PropertyNames.BADGE_WEEK_WARRIOR_STREAK_THRESHOLD, global::setBadgeWeekWarriorStreakThreshold);
        applyLong(rows, PropertyNames.CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD, global::setCycleHighConfidenceLogThreshold);
        applyLong(rows, PropertyNames.INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS, global::setInsightIrregularVariationThresholdDays);
        applyLong(rows, PropertyNames.INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS, global::setInsightUnusualPeriodLengthDeltaDays);
        applyLong(rows, PropertyNames.INSIGHT_DEFAULT_CYCLES, global::setInsightDefaultCycles);
        applyLong(rows, PropertyNames.SUBSCRIPTION_PERIOD_DAYS, global::setSubscriptionPeriodDays);
        applyLong(rows, PropertyNames.LOG_MAX_PERIOD_RANGE_DAYS, global::setLogMaxPeriodRangeDays);
        applyLong(rows, PropertyNames.REWARDS_HISTORY_PAGE_SIZE, global::setRewardsHistoryPageSize);
        applyLong(rows, PropertyNames.CONTENT_FEED_DEFAULT_LIMIT, global::setContentFeedDefaultLimit);
        globalConfigRepository.save(global);
    }

    private int migrateDefinitions() {
        int count = 0;
        for (Map<String, Object> row : jdbcTemplate.queryForList(
                "SELECT string_value FROM global_configuration WHERE name LIKE ?", PropertyNames.BADGE_DEFINITION_PREFIX + "%")) {
            BadgeDefinition def = parse((String) row.get("string_value"), BadgeDefinition.class);
            if (def != null && def.getId() != null && !badgeDefinitionRepository.existsById(def.getId())) {
                badgeDefinitionRepository.save(def);
                count++;
            }
        }
        for (Map<String, Object> row : jdbcTemplate.queryForList(
                "SELECT string_value FROM global_configuration WHERE name LIKE ?", PropertyNames.CHALLENGE_DEFINITION_PREFIX + "%")) {
            ChallengeDefinition def = parse((String) row.get("string_value"), ChallengeDefinition.class);
            if (def != null && def.getId() != null && !challengeDefinitionRepository.existsById(def.getId())) {
                challengeDefinitionRepository.save(def);
                count++;
            }
        }
        for (Map<String, Object> row : jdbcTemplate.queryForList(
                "SELECT string_value FROM global_configuration WHERE name LIKE ?", PropertyNames.LEVEL_DEFINITION_PREFIX + "%")) {
            LevelDefinition def = parse((String) row.get("string_value"), LevelDefinition.class);
            if (def != null && def.getId() != null && !levelDefinitionRepository.existsById(def.getId())) {
                levelDefinitionRepository.save(def);
                count++;
            }
        }
        return count;
    }

    private <T> T parse(String json, Class<T> type) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            log.warn("Skipping unparseable {} definition row during migration: {}", type.getSimpleName(), e.getMessage());
            return null;
        }
    }

    private void applyString(Map<String, LegacyRow> rows, String name, Consumer<String> setter) {
        LegacyRow row = rows.get(name);
        if (row != null && row.stringValue() != null && !row.stringValue().isBlank()) {
            setter.accept(row.stringValue());
        }
    }

    private void applyLong(Map<String, LegacyRow> rows, String name, LongConsumer setter) {
        LegacyRow row = rows.get(name);
        if (row != null && row.value() != null) {
            setter.accept(row.value());
        }
    }

    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE LOWER(table_name) = LOWER(?)",
                Integer.class, tableName);
        return count != null && count > 0;
    }
}
