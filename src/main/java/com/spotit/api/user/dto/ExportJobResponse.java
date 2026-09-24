package com.spotit.api.user.dto;

import java.util.UUID;

public record ExportJobResponse(UUID jobId, String status) {
}
