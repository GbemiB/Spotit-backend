package com.spotit.api.cycle.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CycleReadServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks CycleReadServiceImpl service;

    UUID userId = UUID.randomUUID();

    private void stubUser(LocalDate lastPeriodDate) {
        when(userRepository.findById(userId)).thenReturn(Optional.of(
                User.builder().id(userId).cycleLength(28).periodLength(5).lastPeriodDate(lastPeriodDate).build()));
    }

    @Test
    void currentOnDayOneIsThePeriodPhase() {
        LocalDate today = LocalDate.now();
        stubUser(today);
        when(cycleLogRepository.countByUserId(userId)).thenReturn(2L);
        when(configurationDomainService.getCycleHighConfidenceLogThreshold()).thenReturn(10L);

        CycleCurrentResponse response = service.getCurrent(userId);

        assertThat(response.cycleDay()).isEqualTo(1);
        assertThat(response.phase()).isEqualTo("period");
        assertThat(response.nextPeriodDate()).isEqualTo(today.plusDays(28));
        assertThat(response.daysUntilNextPeriod()).isEqualTo(28);
        assertThat(response.confidence()).isEqualTo("estimated");
    }

    @Test
    void currentOnOvulationDayWithEnoughLogsIsHighConfidence() {
        // 28-day cycle: ovulation is day 14, i.e. 13 days after the period started.
        stubUser(LocalDate.now().minusDays(13));
        when(cycleLogRepository.countByUserId(userId)).thenReturn(10L);
        when(configurationDomainService.getCycleHighConfidenceLogThreshold()).thenReturn(10L);

        CycleCurrentResponse response = service.getCurrent(userId);

        assertThat(response.cycleDay()).isEqualTo(14);
        assertThat(response.phase()).isEqualTo("ovulation");
        assertThat(response.confidence()).isEqualTo("high");
    }

    @Test
    void currentOutsideAnyTrackedPhaseHasNoPhase() {
        stubUser(LocalDate.now().minusDays(20));
        when(cycleLogRepository.countByUserId(userId)).thenReturn(0L);
        when(configurationDomainService.getCycleHighConfidenceLogThreshold()).thenReturn(10L);

        assertThat(service.getCurrent(userId).phase()).isNull();
    }

    @Test
    void currentWithoutALastPeriodDateReportsInsufficientData() {
        stubUser(null);

        assertThat(service.getCurrent(userId)).isEqualTo(new CycleCurrentResponse(null, null, null, null, "insufficient_data"));
        verifyNoInteractions(cycleLogRepository);
    }

    @Test
    void currentForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCurrent(userId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void calendarLabelsEveryDayOfTheMonth() {
        stubUser(LocalDate.of(2026, 7, 1));

        CycleCalendarResponse response = service.getCalendarMonth(userId, 2026, 7);

        assertThat(response.days()).hasSize(31);
        assertThat(response.days().get(0)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-07-01", "period"));
        assertThat(response.days().get(4).phase()).isEqualTo("period");
        assertThat(response.days().get(5).phase()).isNull();
        assertThat(response.days().get(8).phase()).isEqualTo("fertile");
        assertThat(response.days().get(13).phase()).isEqualTo("ovulation");
        assertThat(response.days().get(28).phase()).isEqualTo("period");
    }

    @Test
    void calendarWithoutALastPeriodDateHasNoPhases() {
        stubUser(null);

        CycleCalendarResponse response = service.getCalendarMonth(userId, 2026, 2);

        assertThat(response.days()).hasSize(28).allSatisfy(d -> assertThat(d.phase()).isNull());
    }

    @Test
    void calendarRejectsAnInvalidMonth() {
        assertThatThrownBy(() -> service.getCalendarMonth(userId, 2026, 13))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.getCalendarMonth(userId, 2026, 0)).isInstanceOf(ApiException.class);
        verifyNoInteractions(userRepository);
    }
}
