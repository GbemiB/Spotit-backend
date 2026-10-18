package com.spotit.api.device.repository;

import com.spotit.api.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByPushToken(String pushToken);

    List<Device> findByUserIdIn(Collection<UUID> userIds);

    List<Device> findByUserId(UUID userId);

    void deleteByPushToken(String pushToken);

    void deleteByPushTokenIn(Collection<String> pushTokens);

    void deleteByUserId(UUID userId);
}
