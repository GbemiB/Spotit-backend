package com.spotit.api.insight.dto;

import java.util.List;

public record RegularityResponse(String status, List<String> flags, String disclaimer) {
}
