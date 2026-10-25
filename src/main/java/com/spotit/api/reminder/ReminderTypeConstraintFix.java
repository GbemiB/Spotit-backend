package com.spotit.api.reminder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderTypeConstraintFix {
    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void dropStaleTypeCheck() {
        try {
            jdbcTemplate.execute("ALTER TABLE reminder_deliveries DROP CONSTRAINT IF EXISTS reminder_deliveries_type_check");
        } catch (RuntimeException e) {
            log.warn("Could not drop reminder_deliveries_type_check: {}", e.getMessage());
        }
    }
}
