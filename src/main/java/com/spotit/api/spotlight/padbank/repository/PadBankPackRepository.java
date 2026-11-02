package com.spotit.api.spotlight.padbank.repository;

import com.spotit.api.spotlight.padbank.entity.PadBankPack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PadBankPackRepository extends JpaRepository<PadBankPack, String> {
    List<PadBankPack> findAllByOrderBySortOrderAsc();

    List<PadBankPack> findByActiveTrueOrderBySortOrderAsc();
}
