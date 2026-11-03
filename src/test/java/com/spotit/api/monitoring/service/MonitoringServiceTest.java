package com.spotit.api.monitoring.service;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.monitoring.dto.CrashReportRequest;
import com.spotit.api.monitoring.entity.AppCrash;
import com.spotit.api.monitoring.entity.FailedAction;
import com.spotit.api.monitoring.repository.AppCrashRepository;
import com.spotit.api.monitoring.repository.FailedActionRepository;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitoringServiceTest {
    @Mock AppCrashRepository appCrashRepository;
    @Mock FailedActionRepository failedActionRepository;
    @Mock UserRepository userRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks MonitoringService service;

    UUID userId = UUID.randomUUID();

    @Test
    void aCrashIsStoredWithLongFieldsCutAndAFutureTimeClamped() {
        Instant future = Instant.now().plusSeconds(3600);
        service.recordCrash(userId, new CrashReportRequest("x".repeat(900), "s".repeat(9000), "Home", "1.1.0 (7)", "android",
                "Pixel 8", "15", future));

        ArgumentCaptor<AppCrash> saved = ArgumentCaptor.forClass(AppCrash.class);
        verify(appCrashRepository).save(saved.capture());
        assertThat(saved.getValue().getMessage()).hasSize(500);
        assertThat(saved.getValue().getStack()).hasSize(4000);
        assertThat(saved.getValue().getUserId()).isEqualTo(userId);
        assertThat(saved.getValue().getPlatform()).isEqualTo("android");
        assertThat(saved.getValue().getHappenedAt()).isBefore(future);
    }

    @Test
    void aCrashWithoutATimeOrUserIsStillStored() {
        service.recordCrash(null, new CrashReportRequest("Boom", null, null, null, null, null, null, null));

        ArgumentCaptor<AppCrash> saved = ArgumentCaptor.forClass(AppCrash.class);
        verify(appCrashRepository).save(saved.capture());
        assertThat(saved.getValue().getUserId()).isNull();
        assertThat(saved.getValue().getStack()).isNull();
        assertThat(saved.getValue().getHappenedAt()).isNotNull();
    }

    @Test
    void aFailureIsStoredAndAStorageErrorIsSwallowed() {
        service.recordFailure(userId, "POST /api/v1/logs/period", 500);
        ArgumentCaptor<FailedAction> saved = ArgumentCaptor.forClass(FailedAction.class);
        verify(failedActionRepository).save(saved.capture());
        assertThat(saved.getValue().getAction()).isEqualTo("POST /api/v1/logs/period");
        assertThat(saved.getValue().getStatus()).isEqualTo(500);

        when(failedActionRepository.save(any())).thenThrow(new RuntimeException("db down"));
        assertThatCode(() -> service.recordFailure(null, "GET /api/v1/x", 503)).doesNotThrowAnyException();
    }

    @Test
    void lastActivityIsWrittenAtMostOnceAnHour() {
        service.touch(userId);
        service.touch(userId);

        verify(userRepository, times(1)).touchLastActive(eq(userId), any());
    }

    @Test
    void aStorageErrorWhileTouchingIsSwallowed() {
        when(userRepository.touchLastActive(eq(userId), any())).thenThrow(new RuntimeException("db down"));
        assertThatCode(() -> service.touch(userId)).doesNotThrowAnyException();
    }

    @Test
    void theSupportEmailComesFromConfig() {
        when(configurationDomainService.getAppSupportEmail()).thenReturn("spotitinternational@gmail.com");
        assertThat(service.supportEmail()).isEqualTo("spotitinternational@gmail.com");
    }
}
