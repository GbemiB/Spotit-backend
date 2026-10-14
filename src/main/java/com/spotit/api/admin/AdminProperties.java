package com.spotit.api.admin;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spotit.admin")
public record AdminProperties(List<String> emails) {
    public boolean isAdmin(String email) {
        if (emails == null || email == null) {
            return false;
        }
        return emails.stream().anyMatch(e -> e != null && e.strip().equalsIgnoreCase(email.strip()));
    }
}
