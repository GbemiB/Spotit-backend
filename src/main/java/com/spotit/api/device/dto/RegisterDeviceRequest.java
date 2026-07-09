package com.spotit.api.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterDeviceRequest(
        @NotBlank String pushToken,
        @NotBlank @Pattern(regexp = "ios|android") String platform
) {
}
