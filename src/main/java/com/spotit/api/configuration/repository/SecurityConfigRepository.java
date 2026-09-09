package com.spotit.api.configuration.repository;

import com.spotit.api.configuration.entity.SecurityConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityConfigRepository extends JpaRepository<SecurityConfig, Integer> {
}
