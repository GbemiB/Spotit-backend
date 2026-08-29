package com.spotit.api.smtp.controller;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.dto.SaveSmtpSettingsRequest;
import com.spotit.api.smtp.dto.SmtpSettingsStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Smtp Config (Admin)", description = "Admin surface for the single SMTP relay used to send all transactional mail.")
@RestController
@RequestMapping("/api/v1/config/smtp")
@RequiredArgsConstructor
public class SmtpConfigController {
    private final ConfigurationDomainService configurationDomainService;

    @Operation(summary = "Get SMTP status", description = "Returns what's configured (never the password) so an admin can confirm settings without guessing.")
    @GetMapping
    public SmtpSettingsStatusResponse status() {
        return configurationDomainService.getSmtpSettings()
                .map(s -> new SmtpSettingsStatusResponse(true, s.host(), s.port(), s.username(), s.fromAddress(), s.useTls()))
                .orElseGet(SmtpSettingsStatusResponse::unconfigured);
    }

    @Operation(summary = "Save SMTP settings", description = "Upserts the smtp-* properties. Omit password to keep the previously stored one.")
    @PutMapping
    public SmtpSettingsStatusResponse save(@Valid @RequestBody SaveSmtpSettingsRequest request) {
        configurationDomainService.saveSmtpSettings(request.host(), request.port(), request.username(), request.password(),
                request.fromAddress(), request.useTls());
        return status();
    }
}
