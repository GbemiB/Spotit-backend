package com.spotit.api.configuration.dto;

import java.time.LocalDate;
import java.util.UUID;

public record GlobalConfigurationResponse(UUID id, String name, String groupName, boolean enabled, Long value, LocalDate dateValue,
                                           String stringValue, String description) {
}
