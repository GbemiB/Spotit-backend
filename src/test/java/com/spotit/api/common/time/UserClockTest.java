package com.spotit.api.common.time;

import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClockTest {
    @Mock DeviceRepository deviceRepository;
    @InjectMocks UserClock userClock;

    User user = User.builder().id(UUID.randomUUID()).build();

    private void header(String zone) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (zone != null) {
            request.addHeader(UserClock.TIME_ZONE_HEADER, zone);
        }
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void clearRequest() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void thePhonesTimeZoneHeaderWins() {
        user.setTimeZone("America/Los_Angeles");
        header("Africa/Lagos");

        assertThat(userClock.zoneOf(user)).isEqualTo(ZoneId.of("Africa/Lagos"));
        verifyNoInteractions(deviceRepository);
    }

    @Test
    void anInvalidHeaderFallsBackToTheSavedTimeZone() {
        user.setTimeZone("Africa/Lagos");
        header("Not/AZone");

        assertThat(userClock.zoneOf(user)).isEqualTo(ZoneId.of("Africa/Lagos"));
    }

    @Test
    void withoutAHeaderOrSavedZoneTheDeviceZoneIsUsed() {
        header(null);
        Device device = new Device();
        device.setTimeZone("Europe/London");
        when(deviceRepository.findByUserId(user.getId())).thenReturn(List.of(device));

        assertThat(userClock.zoneOf(user)).isEqualTo(ZoneId.of("Europe/London"));
    }

    @Test
    void withNothingKnownItIsUtc() {
        when(deviceRepository.findByUserId(user.getId())).thenReturn(List.of());

        assertThat(userClock.zoneOf(user)).isEqualTo(ZoneOffset.UTC);
    }

    @Test
    void theSameMomentCanBeADifferentDayForDifferentUsers() {
        java.time.Instant lateEvening = java.time.Instant.parse("2026-10-18T05:30:00Z");

        assertThat(lateEvening.atZone(ZoneId.of("Africa/Lagos")).toLocalDate()).isEqualTo(java.time.LocalDate.of(2026, 10, 18));
        assertThat(lateEvening.atZone(ZoneId.of("America/Los_Angeles")).toLocalDate()).isEqualTo(java.time.LocalDate.of(2026, 10, 17));
    }
}
