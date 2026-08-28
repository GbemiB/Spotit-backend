package com.spotit.api.smtp.repository;

import com.spotit.api.smtp.entity.SmtpRole;
import com.spotit.api.smtp.entity.SmtpSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SmtpSettingsRepository extends JpaRepository<SmtpSettings, UUID> {

    Optional<SmtpSettings> findByRole(SmtpRole role);
}
