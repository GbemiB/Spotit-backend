package com.spotit.api.spotlight.giveday.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayLevelAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelAdmin;
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

@Tag(name = "Give Day Config (Admin)", description = "The areas, places, products, levels, ways to travel and what each place needs in Give Day. PUT creates or updates; "
        + "only the fields sent change.")
@RestController
@RequestMapping("/api/v1/config/giveday")
@RequiredArgsConstructor
public class GiveDayConfigController {
    private final GiveDayAdminService adminService;

    @Operation(summary = "List areas")
    @GetMapping("/areas")
    public AdminPage<GiveDayAreaAdmin> areas(@Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.areas(page, size);
    }

    @Operation(summary = "Create or update an area", description = "An area of Lagos on the map. x and y are map units east and south of the map's "
            + "top-left corner (100 per km by default); region groups areas in the home picker.")
    @PutMapping("/areas/{id}")
    public GiveDayAreaAdmin saveArea(@Parameter(example = "yaba") @PathVariable String id, @RequestBody GiveDayAreaAdmin body) {
        return adminService.saveArea(id, body);
    }

    @Operation(summary = "List places")
    @GetMapping("/places")
    public AdminPage<GiveDayPlaceAdmin> places(@Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.places(page, size);
    }

    @Operation(summary = "Create or update a place", description = "A stop inside an area. x and y are map units; closes is the "
            + "closing time in minutes after midnight; hostLook picks the host drawing; forSelf marks the player's own kit.")
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

    @Operation(summary = "Create or update a product",
        description = "cost is in SP for the whole place, counted against the level's gift budget; absorbent products are needed for a star.")
    @PutMapping("/products/{id}")
    public GiveDayProductAdmin saveProduct(@Parameter(example = "pads") @PathVariable String id, @RequestBody GiveDayProductAdmin body) {
        return adminService.saveProduct(id, body);
    }

    @Operation(summary = "List levels")
    @GetMapping("/levels")
    public AdminPage<GiveDayLevelAdmin> levels(@Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.levels(page, size);
    }

    @Operation(summary = "Create or update a level", description = "radiusKm is how far from the player's home the level reaches; "
            + "starsNeeded opens the level; passCost is the SP day pass (0 = free); "
            + "giftBudget is the SP of gifts allowed at each place on this level.")
    @PutMapping("/levels/{number}")
    public GiveDayLevelAdmin saveLevel(@Parameter(example = "3") @PathVariable int number, @RequestBody GiveDayLevelAdmin body) {
        return adminService.saveLevel(number, body);
    }

    @Operation(summary = "List ways to travel")
    @GetMapping("/travel")
    public AdminPage<GiveDayTravelAdmin> travel(@Parameter(example = "0") @RequestParam(required = false) Integer page,
            @Parameter(example = "50") @RequestParam(required = false) Integer size) {
        return adminService.travel(page, size);
    }

    @Operation(summary = "Create or update a way to travel", description = "cost is SP per trip (0 = free); unitsPerMinute is map distance covered "
            + "per game minute, so higher is faster.")
    @PutMapping("/travel/{id}")
    public GiveDayTravelAdmin saveTravel(@Parameter(example = "bike") @PathVariable String id, @RequestBody GiveDayTravelAdmin body) {
        return adminService.saveTravel(id, body);
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
