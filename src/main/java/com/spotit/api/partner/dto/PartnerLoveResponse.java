package com.spotit.api.partner.dto;

import java.util.List;

public record PartnerLoveResponse(List<PartnerLoveItem> items, List<String> quickReplies) {
}
