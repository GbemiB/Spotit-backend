package com.spotit.api.spotlight.giveday.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.giveday.service.GiveDayAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Give Day Config (Admin)", description = "The places, products and what each place needs in Give Day. PUT creates or updates; "
        + "only the fields sent change.")
@RestController
@RequestMapping("/api/v1/config/giveday")
@RequiredArgsConstructor
public class GiveDayConfigController {
    private final GiveDayAdminService adminService;

    @Operation(summary = "List places")
    @GetMapping("/places")
    public AdminPage<GiveDayPlaceAdmin> places(@Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.places(page, size);
    }

    @Operation(summary = "Create or update a place", description = "counts are the group sizes offered; hostLook picks the character (girl, woman "
            + "or student); forSelf marks the player's own kit.")
    @PutMapping("/places/{id}")
    public GiveDayPlaceAdmin savePlace(@Parameter(example = "school") @PathVariable String id, @RequestBody GiveDayPlaceAdmin body) {
        return adminService.savePlace(id, body);
    }

    @Operation(summary = "List products")
    @GetMapping("/products")
    public AdminPage<GiveDayProductAdmin> products(@Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.products(page, size);
    }

    @Operation(summary = "Create or update a product", description = "cost is in give coins per person; absorbent products are needed for a star.")
    @PutMapping("/products/{id}")
    public GiveDayProductAdmin saveProduct(@Parameter(example = "pads") @PathVariable String id, @RequestBody GiveDayProductAdmin body) {
        return adminService.saveProduct(id, body);
    }

    @Operation(summary = "List needs", description = "How well each product suits each place, with ?placeId= for one place.")
    @GetMapping("/needs")
    public AdminPage<GiveDayNeedAdmin> needs(@RequestParam(required = false) String placeId,
            @Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.needs(placeId, page, size);
    }

    @Operation(summary = "Set how well a product suits a place", description = "rating is good, ok or poor; why is shown to the player.")
    @PutMapping("/needs/{placeId}/{productId}")
    public GiveDayNeedAdmin saveNeed(@PathVariable String placeId, @PathVariable String productId, @RequestBody GiveDayNeedAdmin body) {
        return adminService.saveNeed(placeId, productId, body);
    }
}
