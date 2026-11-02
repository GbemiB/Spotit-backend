package com.spotit.api.spotlight.padbank.repository;

import com.spotit.api.spotlight.padbank.entity.PadBankVisitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PadBankVisitorRepository extends JpaRepository<PadBankVisitor, String> {
    List<PadBankVisitor> findAllByOrderByDayAscSortOrderAsc();

    List<PadBankVisitor> findByDayAndActiveTrueOrderBySortOrderAsc(int day);
}
