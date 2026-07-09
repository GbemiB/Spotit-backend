package com.spotit.api.shop.service;

import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ShopReadService {

    /** User-facing catalog: products the caller can see, with level/Premium locking applied. */
    List<ProductResponse> listProducts(UUID userId);

    List<OrderResponse> listOrders(UUID userId);

    /** Admin/config view: every product (including inactive), with no per-user locking. */
    List<ProductAdminResponse> listAllForAdmin();

    ProductAdminResponse getForAdmin(String productId);
}
