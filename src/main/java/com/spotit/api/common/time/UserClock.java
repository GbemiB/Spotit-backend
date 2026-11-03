package com.spotit.api.common.time;

import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserClock {
    public static final String TIME_ZONE_HEADER = "X-Time-Zone";

    private final DeviceRepository deviceRepository;

    public LocalDate today(User user) {
        return LocalDate.now(zoneOf(user));
    }

    public ZoneId zoneOf(User user) {
        return requestZone()
                .or(() -> parse(user.getTimeZone()))
                .orElseGet(() -> zoneOf(user, deviceRepository.findByUserId(user.getId())));
    }

    public static ZoneId zoneOf(User user, List<Device> devices) {
        return parse(user.getTimeZone())
                .or(() -> devices.stream().map(Device::getTimeZone).filter(Objects::nonNull).map(UserClock::parse)
                        .flatMap(Optional::stream).findFirst())
                .orElse(ZoneOffset.UTC);
    }

    static Optional<ZoneId> parse(String zone) {
        if (zone == null || zone.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(ZoneId.of(zone.trim()));
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }

    private static Optional<ZoneId> requestZone() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            return parse(request.getHeader(TIME_ZONE_HEADER));
        }
        return Optional.empty();
    }
}
