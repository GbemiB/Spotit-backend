package com.spotit.api.configuration.dto;

import java.time.LocalDate;

// Partial update — every field optional, only the ones you set are changed. Setting stringValue
// on a secret-holding property (jwt-secret, smtp-*-password) encrypts it before storage.
public record UpdateGlobalConfigurationRequest(Boolean enabled, Long value, LocalDate dateValue, String stringValue, String description) {
}
