package com.spotit.api.shop.service;

import com.spotit.api.shop.dto.CreateProductRequest;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.RedeemResponse;
import com.spotit.api.shop.dto.UpdateProductRequest;

import java.util.UUID;

public interface ShopWriteService {

    RedeemResponse redeem(UUID userId, String productId);

    // -- global configuration: admin CRUD over the product catalog ------

    ProductAdminResponse createProduct(CreateProductRequest request);

    ProductAdminResponse updateProduct(String productId, UpdateProductRequest request);

    void deleteProduct(String productId);
}
