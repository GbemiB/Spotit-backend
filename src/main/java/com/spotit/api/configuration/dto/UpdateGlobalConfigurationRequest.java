package com.spotit.api.configuration.dto;

import java.time.LocalDate;

// Partial update — every field optional, only the ones you set are changed. Setting stringValue
// on an encrypted-secret property (jwt-secret, smtp-*-password) encrypts it before storage;
// crypto-aes-key rejects updates outright — see ConfigurationDomainServiceImpl#update.
public record UpdateGlobalConfigurationRequest(String groupName, Boolean enabled, Long value, LocalDate dateValue, String stringValue,
                                                String description) {
}
