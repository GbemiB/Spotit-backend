package com.spotit.api.spotlight.core.dto;

import java.util.List;

public record AdminPage<T>(List<T> items, int page, int size, long total) {
}
