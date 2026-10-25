package com.spotit.api.partner.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PartnerLoveItem(
        java.util.UUID id,
        LocalDate date,
        String partnerName,
        String kind,
        List<String> supports,
        List<String> labels,
        String note,
        String expression,
        String expressionLabel,
        String expressionEmoji,
        Instant sentAt,
        boolean replyHeart,
        String replyNote
) {
}
