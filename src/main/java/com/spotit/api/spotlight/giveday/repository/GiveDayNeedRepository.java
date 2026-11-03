package com.spotit.api.spotlight.giveday.repository;

import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayNeedRepository extends JpaRepository<GiveDayNeed, String> {
    List<GiveDayNeed> findAllByOrderByPlaceIdAscProductIdAsc();

    List<GiveDayNeed> findByPlaceId(String placeId);
}
