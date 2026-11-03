package com.spotit.api.spotlight.core.repository;

import com.spotit.api.spotlight.core.entity.SpotlightRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotlightRankRepository extends JpaRepository<SpotlightRank, String> {
    List<SpotlightRank> findAllByOrderByMinStarsAsc();

    List<SpotlightRank> findByActiveTrueOrderByMinStarsAsc();
}
