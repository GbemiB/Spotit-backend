package com.spotit.api.device.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.service.DeviceWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceWriteService deviceWriteService;

    @PostMapping("/register")
    public MessageResponse register(@CurrentUserId UUID userId, @Valid @RequestBody RegisterDeviceRequest request) {
        deviceWriteService.register(userId, request);
        return new MessageResponse("Device registered.");
    }

    @DeleteMapping("/{pushToken}")
    public MessageResponse unregister(@PathVariable String pushToken) {
        deviceWriteService.unregister(pushToken);
        return new MessageResponse("Device unregistered.");
    }
}
