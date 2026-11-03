package com.spotit.api.partner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AcceptInviteRequest(
        @NotBlank @Size(max = 12) String code,
        @NotBlank @Size(max = 40) String nickname
) {
}
