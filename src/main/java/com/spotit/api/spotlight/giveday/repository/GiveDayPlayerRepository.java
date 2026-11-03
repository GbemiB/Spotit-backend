package com.spotit.api.spotlight.giveday.repository;

import com.spotit.api.spotlight.giveday.entity.GiveDayPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GiveDayPlayerRepository extends JpaRepository<GiveDayPlayer, UUID> {
}
