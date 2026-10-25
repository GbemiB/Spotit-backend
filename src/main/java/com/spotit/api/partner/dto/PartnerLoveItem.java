package com.spotit.api.partner.dto;

import java.time.LocalDate;
import java.util.List;

public record PartnerLoveItem(
        LocalDate date,
        String partnerName,
        List<String> supports,
        List<String> labels,
        String note
) {
}
