package com.spotit.api.log.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogsRangeResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.repository.CycleLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogReadServiceImplTest {
    @Mock CycleLogRepository cycleLogRepository;
    @InjectMocks LogReadServiceImpl service;

    UUID userId = UUID.randomUUID();
    LocalDate date = LocalDate.of(2026, 7, 10);

    private CycleLog log(LocalDate day) {
        return CycleLog.builder().userId(userId).logDate(day).flow(FlowIntensity.heavy).mood(MoodType.calm)
                .symptoms(List.of(1, 2)).notes("note").intimate(true).build();
    }

    @Test
    void getLogMapsEnumsAndSymptomCodesToNames() {
        when(cycleLogRepository.findByUserIdAndLogDate(userId, date)).thenReturn(Optional.of(log(date)));

        assertThat(service.getLog(userId, date))
                .isEqualTo(new LogEntryResponse(date, "heavy", "calm", List.of("cramps", "headache"), "note", true));
    }

    @Test
    void getLogHandlesAnEntryWithoutFlowOrMood() {
        CycleLog bare = CycleLog.builder().userId(userId).logDate(date).build();
        when(cycleLogRepository.findByUserIdAndLogDate(userId, date)).thenReturn(Optional.of(bare));

        LogEntryResponse response = service.getLog(userId, date);

        assertThat(response.flow()).isNull();
        assertThat(response.mood()).isNull();
        assertThat(response.symptoms()).isEmpty();
    }

    @Test
    void getLogForADayWithNoEntryReturnsAnEmptyEntry() {
        when(cycleLogRepository.findByUserIdAndLogDate(userId, date)).thenReturn(Optional.empty());

        assertThat(service.getLog(userId, date)).isEqualTo(LogEntryResponse.empty(date));
    }

    @Test
    void rangeKeysEntriesByIsoDateInOrder() {
        LocalDate from = date.minusDays(2);
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, from, date))
                .thenReturn(List.of(log(from), log(date)));

        LogsRangeResponse response = service.getLogsInRange(userId, from, date);

        assertThat(response.logs()).containsOnlyKeys("2026-07-08", "2026-07-10");
        assertThat(response.logs().keySet()).containsExactly("2026-07-08", "2026-07-10");
    }

    @Test
    void rangeWithFromAfterToIsRejected() {
        assertThatThrownBy(() -> service.getLogsInRange(userId, date, date.minusDays(1)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        verifyNoInteractions(cycleLogRepository);
    }
}
