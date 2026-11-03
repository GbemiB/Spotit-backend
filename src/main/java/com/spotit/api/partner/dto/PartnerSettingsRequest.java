package com.spotit.api.partner.dto;

import jakarta.validation.constraints.Size;

public record PartnerSettingsRequest(
        @Size(max = 40) String nickname,
        Boolean partnerReminder
) {
}
