package com.spotit.api.rewards.dto;

import java.util.List;

public record PointsHistoryPageResponse(List<PointsHistoryEntryResponse> entries, String nextCursor) {
}
