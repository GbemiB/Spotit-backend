package com.spotit.api.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterDeviceRequest(
        @NotBlank String pushToken,
        @NotBlank @Pattern(regexp = "ios|android") String platform,
        // IANA zone id, e.g. "Africa/Lagos". Optional: 1.0.0 clients do not send it.
        String timeZone
) {
}
