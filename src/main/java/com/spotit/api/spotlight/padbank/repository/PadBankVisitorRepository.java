package com.spotit.api.spotlight.padbank.repository;

import com.spotit.api.spotlight.padbank.entity.PadBankVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PadBankVisitorRepository extends JpaRepository<PadBankVisitor, String> {
    List<PadBankVisitor> findAllByOrderByDayAscSortOrderAsc();

    List<PadBankVisitor> findByDayAndActiveTrueOrderBySortOrderAsc(int day);

    @Query("SELECT DISTINCT v.day FROM PadBankVisitor v WHERE v.active = true ORDER BY v.day")
    List<Integer> activeDays();
}
