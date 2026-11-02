package com.spotit.api.spotlight.padbank.dto;

import com.spotit.api.spotlight.padbank.entity.PadBankPack;
import com.spotit.api.spotlight.padbank.service.PadBankRules;

import java.util.List;

public record PadBankPackAdmin(String id, String name, Integer cost, Integer stock, String color, List<String> contents, Integer sortOrder, Boolean active) {
    public static PadBankPackAdmin of(PadBankPack p) {
        return new PadBankPackAdmin(p.getId(), p.getName(), p.getCost(), p.getStock(), p.getColor(), PadBankRules.decodeLines(p.getContents()),
                p.getSortOrder(), p.isActive());
    }
}
