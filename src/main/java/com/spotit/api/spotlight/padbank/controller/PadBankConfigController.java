package com.spotit.api.spotlight.padbank.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.padbank.dto.PadBankPackAdmin;
import com.spotit.api.spotlight.padbank.dto.PadBankVisitorAdmin;
import com.spotit.api.spotlight.padbank.service.PadBankAdminService;
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

@Tag(name = "Pad Bank Run Config (Admin)", description = "The packs on the shelf and the visitors for each day of Pad Bank Run. PUT creates or "
        + "updates; only the fields sent change.")
@RestController
@RequestMapping("/api/v1/config/padbank")
@RequiredArgsConstructor
public class PadBankConfigController {
    private final PadBankAdminService padBankAdminService;

    @Operation(summary = "List packs")
    @GetMapping("/packs")
    public AdminPage<PadBankPackAdmin> packs(@Parameter(description = "Page number, from 0.", example = "0") @RequestParam(required = false) Integer page,
            @Parameter(description = "Items per page.", example = "50") @RequestParam(required = false) Integer size) {
        return padBankAdminService.packs(page, size);
    }

    @Operation(summary = "Create or update a pack", description = "cost is taken from the bank budget; stock is how many are on the shelf each day.")
    @PutMapping("/packs/{id}")
    public PadBankPackAdmin savePack(@Parameter(example = "starter") @PathVariable String id, @RequestBody PadBankPackAdmin body) {
        return padBankAdminService.savePack(id, body);
    }

    @Operation(summary = "List visitors")
    @GetMapping("/visitors")
    public AdminPage<PadBankVisitorAdmin> visitors(@Parameter(description = "Page number, from 0.", example = "0") @RequestParam(required = false) Integer page,
            @Parameter(description = "Items per page.", example = "50") @RequestParam(required = false) Integer size) {
        return padBankAdminService.visitors(page, size);
    }

    @Operation(summary = "Create or update a visitor", description = "pack is the right pack id; route is pickup or delivery; wrongWhy explains "
            + "each wrong pack by pack id; look picks the character (girl, woman or student).")
    @PutMapping("/visitors/{id}")
    public PadBankVisitorAdmin saveVisitor(@Parameter(example = "day1-girl") @PathVariable String id, @RequestBody PadBankVisitorAdmin body) {
        return padBankAdminService.saveVisitor(id, body);
    }
}
