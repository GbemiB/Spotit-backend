package com.spotit.api.shop.service;

import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ShopReadService {
    List<ProductResponse> listProducts(UUID userId);

    List<OrderResponse> listOrders(UUID userId);

    List<ProductAdminResponse> listAllForAdmin();

    ProductAdminResponse getForAdmin(String productId);
}
