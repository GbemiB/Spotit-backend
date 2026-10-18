package com.spotit.api.cycle.service;

import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingStartBackfill {
    private final UserRepository userRepository;
    private final CycleLogRepository cycleLogRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void backfill() {
        List<User> users = userRepository.findByTrackingStartDateIsNullAndLastPeriodDateIsNotNull();
        for (User user : users) {
            LocalDate fromLastPeriod = user.getLastPeriodDate().withDayOfMonth(1);
            user.setTrackingStartDate(cycleLogRepository.findFirstByUserIdAndFlowIsNotNullOrderByLogDateAsc(user.getId())
                    .map(CycleLog::getLogDate)
                    .map(logged -> CycleUtil.earlierStart(fromLastPeriod, logged))
                    .orElse(fromLastPeriod));
        }
        if (!users.isEmpty()) {
            userRepository.saveAll(users);
            log.info("Tracking start date set for {} users", users.size());
        }
    }
}
