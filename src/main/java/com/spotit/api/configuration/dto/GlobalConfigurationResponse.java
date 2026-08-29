package com.spotit.api.configuration.dto;

import java.time.LocalDate;
import java.util.UUID;

// stringValue is redacted (null) for secret-holding properties (jwt-secret, smtp-*-password,
// crypto-aes-key) — see ConfigurationDomainServiceImpl#isRedacted.
public record GlobalConfigurationResponse(UUID id, String name, String groupName, boolean enabled, Long value, LocalDate dateValue,
                                           String stringValue, String description) {
}
