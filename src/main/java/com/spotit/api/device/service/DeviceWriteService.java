package com.spotit.api.device.service;

import com.spotit.api.device.dto.RegisterDeviceRequest;

import java.util.UUID;

public interface DeviceWriteService {

    void register(UUID userId, RegisterDeviceRequest request);

    void unregister(String pushToken);
}
