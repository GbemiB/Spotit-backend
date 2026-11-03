package com.spotit.api.spotlight.core.repository;

import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotlightChallengeRepository extends JpaRepository<SpotlightChallenge, String> {
    List<SpotlightChallenge> findAllByOrderBySortOrderAsc();

    List<SpotlightChallenge> findByActiveTrueOrderBySortOrderAsc();
}
