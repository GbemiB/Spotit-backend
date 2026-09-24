package com.spotit.api.configuration.repository;

import com.spotit.api.configuration.entity.GlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, Integer> {
}
