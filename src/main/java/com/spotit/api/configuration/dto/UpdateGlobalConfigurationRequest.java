package com.spotit.api.configuration.dto;

import java.time.LocalDate;

public record UpdateGlobalConfigurationRequest(String groupName, Boolean enabled, Long value, LocalDate dateValue, String stringValue,
                                                String description) {
}
