package com.spotit.api.partner.dto;

import jakarta.validation.constraints.Size;

public record LoveReplyRequest(boolean heart, @Size(max = 120) String note) {
}
