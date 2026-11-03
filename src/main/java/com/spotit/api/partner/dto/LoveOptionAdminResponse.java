package com.spotit.api.partner.dto;

public record LoveOptionAdminResponse(String id, String kind, String label, String sub, String emoji, String phrase, Integer storedValue,
                                      int sortOrder, boolean active) {
}
