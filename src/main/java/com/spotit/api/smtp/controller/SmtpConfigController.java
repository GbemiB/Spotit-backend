package com.spotit.api.smtp.controller;

import com.spotit.api.smtp.SmtpProperties;
import com.spotit.api.smtp.dto.SmtpSettingsStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Smtp Config (Admin)", description = "Read-only view of the SMTP relay. The relay is set per environment through the "
        + "SMTP_* environment variables (Render dashboard), not through the API or the database.")
@RestController
@RequestMapping("/api/v1/config/smtp")
@RequiredArgsConstructor
public class SmtpConfigController {
    private final SmtpProperties smtpProperties;

    @Operation(summary = "Get SMTP status", description = "Shows whether this environment's SMTP_* variables are complete and which relay they point at "
            + "(never the password), so a deployment can be checked without guessing.")
    @GetMapping
    public SmtpSettingsStatusResponse status() {
        if (!smtpProperties.isConfigured()) {
            return SmtpSettingsStatusResponse.unconfigured();
        }
        return new SmtpSettingsStatusResponse(true, smtpProperties.host(), smtpProperties.port(), smtpProperties.username(),
                smtpProperties.fromAddress(), smtpProperties.useTls());
    }
}
