package com.spotit.api.partner.repository;

import com.spotit.api.partner.entity.LoveOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoveOptionRepository extends JpaRepository<LoveOption, String> {
    List<LoveOption> findAllByOrderByKindAscSortOrderAsc();

    List<LoveOption> findByKindOrderBySortOrderAsc(String kind);

    List<LoveOption> findByKindAndActiveTrueOrderBySortOrderAsc(String kind);

    @Query("select max(o.storedValue) from LoveOption o where o.kind = 'promise'")
    Integer maxPromiseValue();
}
