package com.spotit.api.spotlight.games.giveday.repository;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayAreaRepository extends JpaRepository<GiveDayArea, String> {
    List<GiveDayArea> findAllByOrderBySortOrderAsc();

    List<GiveDayArea> findByActiveTrueOrderBySortOrderAsc();
}
