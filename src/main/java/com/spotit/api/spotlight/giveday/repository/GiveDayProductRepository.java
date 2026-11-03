package com.spotit.api.spotlight.giveday.repository;

import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveDayProductRepository extends JpaRepository<GiveDayProduct, String> {
    List<GiveDayProduct> findAllByOrderBySortOrderAsc();

    List<GiveDayProduct> findByActiveTrueOrderBySortOrderAsc();
}
