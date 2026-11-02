package com.spotit.api.spotlight.padbank.dto;

import com.spotit.api.spotlight.padbank.entity.PadBankPack;

public record PadBankPackAdmin(String id, String name, Integer cost, Integer stock, String color, Integer sortOrder, Boolean active) {
    public static PadBankPackAdmin of(PadBankPack p) {
        return new PadBankPackAdmin(p.getId(), p.getName(), p.getCost(), p.getStock(), p.getColor(), p.getSortOrder(), p.isActive());
    }
}
