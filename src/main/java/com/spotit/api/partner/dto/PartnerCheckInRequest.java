package com.spotit.api.partner.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record PartnerCheckInRequest(
        @Size(max = 10) List<String> supports,
        @Size(max = 200) String note
) {
    public PartnerCheckInRequest(List<String> supports) {
        this(supports, null);
    }
}
