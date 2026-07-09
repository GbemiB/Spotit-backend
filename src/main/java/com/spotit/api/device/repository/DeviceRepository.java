package com.spotit.api.device.repository;

import com.spotit.api.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findByPushToken(String pushToken);

    void deleteByPushToken(String pushToken);

    void deleteByUserId(UUID userId);
}
