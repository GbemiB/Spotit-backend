package com.spotit.api.rewards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PointsSpentBackfill implements ApplicationRunner {
    static final String SQL = "UPDATE users SET points_spent = COALESCE((SELECT -SUM(h.delta) FROM points_history h "
            + "WHERE h.user_id = users.id AND h.delta < 0), 0) WHERE points_spent IS NULL";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        int updated = jdbcTemplate.update(SQL);
        if (updated > 0) {
            log.info("Filled points_spent for {} user(s) from their points history.", updated);
        }
    }
}
