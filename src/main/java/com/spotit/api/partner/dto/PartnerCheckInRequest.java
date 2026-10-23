package com.spotit.api.partner.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PartnerCheckInRequest(
        @NotEmpty @Size(max = 10) List<String> supports
) {
}
