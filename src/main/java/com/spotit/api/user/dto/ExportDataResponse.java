package com.spotit.api.user.dto;

import java.util.List;
import java.util.Map;

public record ExportDataResponse(UserResponse profile, List<Map<String, Object>> logs, List<Map<String, Object>> pointsHistory) {
}
