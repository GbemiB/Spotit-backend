package com.spotit.api.spotlight.giveday.repository;

import com.spotit.api.spotlight.giveday.entity.GiveDayTravel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayTravelRepository extends JpaRepository<GiveDayTravel, String> {
    List<GiveDayTravel> findAllByOrderBySortOrderAsc();

    List<GiveDayTravel> findByActiveTrueOrderBySortOrderAsc();
}
