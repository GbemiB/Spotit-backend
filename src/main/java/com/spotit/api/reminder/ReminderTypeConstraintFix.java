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
    public void fixSchema() {
        run("ALTER TABLE reminder_deliveries DROP CONSTRAINT IF EXISTS reminder_deliveries_type_check");
        run("ALTER TABLE reminder_deliveries DROP CONSTRAINT IF EXISTS uq_reminder_deliveries_user_type_date");
        run("ALTER TABLE partner_check_ins DROP CONSTRAINT IF EXISTS uq_partner_check_ins_partner_date");
        run("DROP INDEX IF EXISTS uq_reminder_deliveries_once");
        run("CREATE UNIQUE INDEX IF NOT EXISTS uq_reminder_deliveries_once_v2 ON reminder_deliveries (user_id, type, for_date) "
                + "WHERE type NOT IN ('partner_love', 'partner_reply')");
    }

    private void run(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (RuntimeException e) {
            log.warn("Schema fix skipped ({}): {}", sql, e.getMessage());
        }
    }
}
