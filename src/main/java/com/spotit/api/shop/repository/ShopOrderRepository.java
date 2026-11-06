package com.spotit.api.shop.repository;

import com.spotit.api.shop.entity.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShopOrderRepository extends JpaRepository<ShopOrder, UUID> {
    void deleteByUserId(UUID userId);
}
