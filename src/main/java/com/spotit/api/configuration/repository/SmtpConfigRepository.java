package com.spotit.api.configuration.repository;

import com.spotit.api.configuration.entity.SmtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmtpConfigRepository extends JpaRepository<SmtpConfig, Integer> {
}
