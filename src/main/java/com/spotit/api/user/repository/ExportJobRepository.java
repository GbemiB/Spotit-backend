package com.spotit.api.user.repository;

import com.spotit.api.user.entity.ExportJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExportJobRepository extends JpaRepository<ExportJob, UUID> {
    Optional<ExportJob> findByIdAndUserId(UUID id, UUID userId);

    void deleteByUserId(UUID userId);
}
