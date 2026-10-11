package com.spotit.api.device.service;

import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.entity.DevicePlatform;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceWriteServiceImpl implements DeviceWriteService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void register(UUID userId, RegisterDeviceRequest request) {
        Device device = deviceRepository.findByPushToken(request.pushToken())
                .orElseGet(() -> Device.builder().pushToken(request.pushToken()).build());
        device.setUserId(userId);
        device.setPlatform(DevicePlatform.valueOf(request.platform()));
        device.setLastSeenAt(Instant.now());
        String timeZone = validZone(request.timeZone());
        if (timeZone != null) {
            device.setTimeZone(timeZone);
            // Reminder times follow the phone the user opened last.
            userRepository.findById(userId).ifPresent(user -> {
                user.setTimeZone(timeZone);
                userRepository.save(user);
            });
        }
        deviceRepository.save(device);
    }

    private static String validZone(String timeZone) {
        if (timeZone == null || timeZone.isBlank()) {
            return null;
        }
        try {
            return ZoneId.of(timeZone.trim()).getId();
        } catch (DateTimeException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void unregister(String pushToken) {
        deviceRepository.deleteByPushToken(pushToken);
    }
}
