package com.spotit.api.shop.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.shop.dto.CreateProductRequest;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.UpdateProductRequest;
import com.spotit.api.shop.service.ShopReadService;
import com.spotit.api.shop.service.ShopWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Global-configuration admin surface for the rewards-shop catalog — this is
 * what used to be fixed at boot via ReferenceDataSeeder; these endpoints let
 * an admin (eventually a UI) add/change/retire products without a redeploy.
 */
@RestController
@RequestMapping("/api/v1/config/products")
@RequiredArgsConstructor
public class ProductConfigController {

    private final ShopReadService shopReadService;
    private final ShopWriteService shopWriteService;

    @GetMapping
    public List<ProductAdminResponse> list() {
        return shopReadService.listAllForAdmin();
    }

    @GetMapping("/{id}")
    public ProductAdminResponse get(@PathVariable String id) {
        return shopReadService.getForAdmin(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductAdminResponse create(@Valid @RequestBody CreateProductRequest request) {
        return shopWriteService.createProduct(request);
    }

    @PatchMapping("/{id}")
    public ProductAdminResponse update(@PathVariable String id, @RequestBody UpdateProductRequest request) {
        return shopWriteService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        shopWriteService.deleteProduct(id);
        return new MessageResponse("Product deleted.");
    }
}
