package com.spotit.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * One-time cleanup: app_settings and smtp_settings were replaced by global_configuration (see
 * {@code ConfigurationDomainService}), but Hibernate's {@code ddl-auto: update} only ever adds
 * schema for entities that still exist — it never drops a table whose entity was deleted, so
 * without this the old tables would sit in the DB forever. {@code DROP TABLE IF EXISTS} makes
 * this safe to run on every boot, including every one after the first where they're already gone.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LegacySchemaCleanup implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS app_settings");
        jdbcTemplate.execute("DROP TABLE IF EXISTS smtp_settings");
    }
}
