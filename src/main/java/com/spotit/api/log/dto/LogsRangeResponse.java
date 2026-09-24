package com.spotit.api.log.dto;

import java.util.Map;

public record LogsRangeResponse(Map<String, LogEntryResponse> logs) {
}
