package com.spotit.api.partner.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PartnerCheckInRequest(
        @Pattern(regexp = "promise|text|expression") String kind,
        @Size(max = 10) List<String> supports,
        @Size(max = 200) String note,
        @Size(max = 20) String expression
) {
    public PartnerCheckInRequest(List<String> supports) {
        this(null, supports, null, null);
    }

    public PartnerCheckInRequest(List<String> supports, String note) {
        this(null, supports, note, null);
    }
}
