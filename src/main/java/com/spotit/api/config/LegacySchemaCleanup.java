package com.spotit.api.config;

import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import com.spotit.api.rewards.entity.LevelDefinition;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.LevelDefinitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * One-time cleanup: app_settings, smtp_settings, badge_definitions, challenge_definitions, and
 * level_definitions were all replaced by global_configuration (see
 * {@code ConfigurationDomainService}, {@code BadgeDefinitionRepository},
 * {@code ChallengeDefinitionRepository}, {@code LevelDefinitionRepository}), but Hibernate's
 * {@code ddl-auto: update} only ever adds schema for entities that still exist — it never drops
 * a table whose entity was deleted, so without this the old tables would sit in the DB forever.
 *
 * <p>The definition tables get one extra step first: any rows already in them (including ones an
 * admin added or edited beyond the seeded defaults) are copied into global_configuration before
 * the old table is dropped, so dropping it can't silently lose data. {@code @Order} makes sure
 * this migration runs before {@code ReferenceDataSeeder}, whose count()==0 seeding check would
 * otherwise re-seed just the defaults on top of an empty store.
 */
@Component
@Slf4j
@Order(0)
@RequiredArgsConstructor
public class LegacySchemaCleanup implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final BadgeDefinitionRepository badgeDefinitionRepository;
    private final ChallengeDefinitionRepository challengeDefinitionRepository;
    private final LevelDefinitionRepository levelDefinitionRepository;

    @Override
    public void run(ApplicationArguments args) {
        migrateBadgeDefinitions();
        migrateChallengeDefinitions();
        migrateLevelDefinitions();
        jdbcTemplate.execute("DROP TABLE IF EXISTS app_settings");
        jdbcTemplate.execute("DROP TABLE IF EXISTS smtp_settings");
        jdbcTemplate.execute("DROP TABLE IF EXISTS badge_definitions");
        jdbcTemplate.execute("DROP TABLE IF EXISTS challenge_definitions");
        jdbcTemplate.execute("DROP TABLE IF EXISTS level_definitions");
    }

    private void migrateBadgeDefinitions() {
        if (!tableExists("badge_definitions")) {
            return;
        }
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, name, description FROM badge_definitions");
        for (Map<String, Object> row : rows) {
            String id = (String) row.get("id");
            if (badgeDefinitionRepository.existsById(id)) {
                continue;
            }
            badgeDefinitionRepository.save(new BadgeDefinition(id, (String) row.get("name"), (String) row.get("description")));
        }
        log.info("Migrated {} row(s) from badge_definitions into global_configuration.", rows.size());
    }

    private void migrateChallengeDefinitions() {
        if (!tableExists("challenge_definitions")) {
            return;
        }
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, title, reward, total, type FROM challenge_definitions");
        for (Map<String, Object> row : rows) {
            String id = (String) row.get("id");
            if (challengeDefinitionRepository.existsById(id)) {
                continue;
            }
            challengeDefinitionRepository.save(ChallengeDefinition.builder()
                    .id(id)
                    .title((String) row.get("title"))
                    .reward(((Number) row.get("reward")).intValue())
                    .total(((Number) row.get("total")).intValue())
                    .type(ChallengeType.valueOf((String) row.get("type")))
                    .build());
        }
        log.info("Migrated {} row(s) from challenge_definitions into global_configuration.", rows.size());
    }

    private void migrateLevelDefinitions() {
        if (!tableExists("level_definitions")) {
            return;
        }
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id, name, points_low, points_high, sort_order FROM level_definitions");
        for (Map<String, Object> row : rows) {
            String id = (String) row.get("id");
            if (levelDefinitionRepository.existsById(id)) {
                continue;
            }
            levelDefinitionRepository.save(LevelDefinition.builder()
                    .id(id)
                    .name((String) row.get("name"))
                    .pointsLow(((Number) row.get("points_low")).longValue())
                    .pointsHigh(((Number) row.get("points_high")).longValue())
                    .sortOrder(((Number) row.get("sort_order")).intValue())
                    .build());
        }
        log.info("Migrated {} row(s) from level_definitions into global_configuration.", rows.size());
    }

    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?", Integer.class, tableName);
        return count != null && count > 0;
    }
}
