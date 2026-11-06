package com.spotit.api.spotlight.games.giveday.repository;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayNeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayNeedRepository extends JpaRepository<GiveDayNeed, String> {
    List<GiveDayNeed> findAllByOrderByPlaceIdAscProductIdAsc();

    List<GiveDayNeed> findByPlaceId(String placeId);
}
