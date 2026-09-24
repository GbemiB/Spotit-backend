package com.spotit.api.device.service;

import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.entity.DevicePlatform;
import com.spotit.api.device.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceWriteServiceImplTest {
    @Mock DeviceRepository deviceRepository;
    @InjectMocks DeviceWriteServiceImpl service;

    UUID userId = UUID.randomUUID();

    @Test
    void registerCreatesANewDevice() {
        when(deviceRepository.findByPushToken("token")).thenReturn(Optional.empty());

        service.register(userId, new RegisterDeviceRequest("token", "ios"));

        ArgumentCaptor<Device> saved = ArgumentCaptor.forClass(Device.class);
        verify(deviceRepository).save(saved.capture());
        assertThat(saved.getValue().getPushToken()).isEqualTo("token");
        assertThat(saved.getValue().getUserId()).isEqualTo(userId);
        assertThat(saved.getValue().getPlatform()).isEqualTo(DevicePlatform.ios);
    }

    @Test
    void registeringAKnownTokenMovesItToTheNewUser() {
        Device device = Device.builder().pushToken("token").userId(UUID.randomUUID()).platform(DevicePlatform.ios).build();
        when(deviceRepository.findByPushToken("token")).thenReturn(Optional.of(device));

        service.register(userId, new RegisterDeviceRequest("token", "android"));

        assertThat(device.getUserId()).isEqualTo(userId);
        assertThat(device.getPlatform()).isEqualTo(DevicePlatform.android);
        verify(deviceRepository).save(device);
    }

    @Test
    void unregisterDeletesByToken() {
        service.unregister("token");

        verify(deviceRepository).deleteByPushToken("token");
    }
}
