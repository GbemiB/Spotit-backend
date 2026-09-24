package com.spotit.api.insight.dto;

import java.util.List;

public record CycleTrendsResponse(List<Integer> cycleLengths, int avgCycleLength, int avgPeriodLength, int variationDays) {
}
