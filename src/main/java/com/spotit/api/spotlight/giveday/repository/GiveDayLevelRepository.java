package com.spotit.api.spotlight.giveday.repository;

import com.spotit.api.spotlight.giveday.entity.GiveDayLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayLevelRepository extends JpaRepository<GiveDayLevel, Integer> {
    List<GiveDayLevel> findAllByOrderByNumberAsc();

    List<GiveDayLevel> findByActiveTrueOrderByNumberAsc();
}
