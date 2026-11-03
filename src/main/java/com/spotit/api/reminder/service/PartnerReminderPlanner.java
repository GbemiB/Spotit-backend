package com.spotit.api.reminder.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
public class PartnerReminderPlanner {
    public static final int DAYS_BEFORE = 5;

    public record Planned(ReminderType type, String body, LocalDate periodDate) {
    }

    public Optional<Planned> plan(PartnerLink link, User owner, ReminderSettings settings, LocalDateTime localNow) {
        if (owner.getLastPeriodDate() == null || owner.getCycleLength() <= 0) {
            return Optional.empty();
        }
        if (!ReminderPlanner.due(settings.morningTime(), localNow.toLocalTime(), settings)) {
            return Optional.empty();
        }
        LocalDate today = localNow.toLocalDate();
        if (!CycleUtil.statusOf(today, owner.getLastPeriodDate(), owner.getCycleLength()).tracking()) {
            return Optional.empty();
        }
        LocalDate periodDate = CycleUtil.nextPeriodDate(today, owner.getLastPeriodDate(), owner.getCycleLength());
        if (ChronoUnit.DAYS.between(today, periodDate) != DAYS_BEFORE || periodDate.equals(link.getLastRemindedPeriodDate())) {
            return Optional.empty();
        }
        return Optional.of(new Planned(ReminderType.partner_period_upcoming, body(link, owner), periodDate));
    }

    static String body(PartnerLink link, User owner) {
        String nickname = link.getPartnerNickname();
        String name = nickname == null || nickname.isBlank() ? owner.getFirstName() : nickname.strip();
        return name + "'s period is expected to start in " + DAYS_BEFORE + " days.";
    }
}
