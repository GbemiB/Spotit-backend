package com.spotit.api.settings.repository;

import com.spotit.api.settings.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppSettingsRepository extends JpaRepository<AppSettings, UUID> {

    Optional<AppSettings> findTopByOrderByUpdatedAtDesc();
}
