package com.spotit.api.spotlight.games.giveday.repository;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayPlaceRepository extends JpaRepository<GiveDayPlace, String> {
    List<GiveDayPlace> findAllByOrderBySortOrderAsc();

    List<GiveDayPlace> findByActiveTrueOrderBySortOrderAsc();

    List<GiveDayPlace> findByAreaIdAndActiveTrueOrderBySortOrderAsc(String areaId);
}
