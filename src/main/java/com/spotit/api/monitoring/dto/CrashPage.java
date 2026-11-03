package com.spotit.api.monitoring.dto;

import java.util.List;

public record CrashPage(int page, int size, long total, List<CrashItem> crashes) {
}
