package com.spotit.api.admin;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Who may call the {@code /api/v1/config/**} endpoints, bound from {@code spotit.admin.emails} in
 * application.yml (the ADMIN_EMAILS environment variable, comma-separated). The list lives outside the
 * database on purpose: no API call or SQL injection can promote an account. Empty means nobody is an admin.
 */
@ConfigurationProperties(prefix = "spotit.admin")
public record AdminProperties(List<String> emails) {
    public boolean isAdmin(String email) {
        if (emails == null || email == null) {
            return false;
        }
        return emails.stream().anyMatch(e -> e != null && e.strip().equalsIgnoreCase(email.strip()));
    }
}
