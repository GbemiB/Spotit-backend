package com.spotit.api.device.service;

import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.entity.DevicePlatform;
import com.spotit.api.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceWriteServiceImpl implements DeviceWriteService {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void register(UUID userId, RegisterDeviceRequest request) {
        Device device = deviceRepository.findByPushToken(request.pushToken())
                .orElseGet(() -> Device.builder().pushToken(request.pushToken()).build());
        device.setUserId(userId);
        device.setPlatform(DevicePlatform.valueOf(request.platform()));
        deviceRepository.save(device);
    }

    @Override
    @Transactional
    public void unregister(String pushToken) {
        deviceRepository.deleteByPushToken(pushToken);
    }
}
