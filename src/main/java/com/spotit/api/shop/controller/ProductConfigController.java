package com.spotit.api.shop.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.shop.dto.CreateProductRequest;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.UpdateProductRequest;
import com.spotit.api.shop.service.ShopReadService;
import com.spotit.api.shop.service.ShopWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product Config (Admin)", description = "Admin CRUD surface for the rewards-shop product catalog. No auth check is currently enforced on these endpoints.")
@RestController
@RequestMapping("/api/v1/config/products")
@RequiredArgsConstructor
public class ProductConfigController {

    private final ShopReadService shopReadService;
    private final ShopWriteService shopWriteService;

    @Operation(summary = ProductConfigControllerSwagger.LIST_SUMMARY, description = ProductConfigControllerSwagger.LIST_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All products.", content = @Content(
                    schema = @Schema(implementation = ProductAdminResponse.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.LIST_200_EXAMPLE)))
    })
    @GetMapping
    public List<ProductAdminResponse> list() {
        return shopReadService.listAllForAdmin();
    }

    @Operation(summary = ProductConfigControllerSwagger.GET_SUMMARY, description = ProductConfigControllerSwagger.GET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found.", content = @Content(
                    schema = @Schema(implementation = ProductAdminResponse.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.GET_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @GetMapping("/{id}")
    public ProductAdminResponse get(@Parameter(description = "Product id", example = "theme_dark_rose") @PathVariable String id) {
        return shopReadService.getForAdmin(id);
    }

    @Operation(summary = ProductConfigControllerSwagger.CREATE_SUMMARY, description = ProductConfigControllerSwagger.CREATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = CreateProductRequest.class),
            examples = @ExampleObject(value = ProductConfigControllerSwagger.CREATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created.", content = @Content(
                    schema = @Schema(implementation = ProductAdminResponse.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.CREATE_201_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "A product with this id already exists.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.CREATE_409_EXAMPLE)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductAdminResponse create(@Valid @RequestBody CreateProductRequest request) {
        return shopWriteService.createProduct(request);
    }

    @Operation(summary = ProductConfigControllerSwagger.UPDATE_SUMMARY, description = ProductConfigControllerSwagger.UPDATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = UpdateProductRequest.class),
            examples = @ExampleObject(value = ProductConfigControllerSwagger.UPDATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated.", content = @Content(
                    schema = @Schema(implementation = ProductAdminResponse.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.UPDATE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @PatchMapping("/{id}")
    public ProductAdminResponse update(@Parameter(description = "Product id", example = "theme_dark_rose") @PathVariable String id,
                                        @RequestBody UpdateProductRequest request) {
        return shopWriteService.updateProduct(id, request);
    }

    @Operation(summary = ProductConfigControllerSwagger.DELETE_SUMMARY, description = ProductConfigControllerSwagger.DELETE_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.DELETE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ProductConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @DeleteMapping("/{id}")
    public MessageResponse delete(@Parameter(description = "Product id", example = "theme_dark_rose") @PathVariable String id) {
        shopWriteService.deleteProduct(id);
        return new MessageResponse("Product deleted.");
    }
}
