package com.spotit.api.smtp.controller;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.dto.SaveSmtpSettingsRequest;
import com.spotit.api.smtp.dto.SmtpSettingsStatusResponse;
import com.spotit.api.smtp.dto.SmtpSettingsStatusResponse.SmtpProviderStatus;
import com.spotit.api.smtp.entity.SmtpRole;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Smtp Config (Admin)", description = "Admin surface for the primary/backup SMTP relay settings used to send all transactional mail.")
@RestController
@RequestMapping("/api/v1/config/smtp")
@RequiredArgsConstructor
public class SmtpConfigController {
    private final ConfigurationDomainService configurationDomainService;

    @Operation(summary = "Get SMTP status", description = "Returns what's configured for primary and backup (never the password) so an admin can confirm settings without guessing.")
    @GetMapping
    public SmtpSettingsStatusResponse status() {
        List<ResolvedSmtpSettings> all = configurationDomainService.getSmtpSettingsInPriorityOrder();
        SmtpProviderStatus primary = byRole(all, SmtpRole.primary);
        SmtpProviderStatus backup = byRole(all, SmtpRole.backup);
        return new SmtpSettingsStatusResponse(primary, backup);
    }

    @Operation(summary = "Save SMTP settings", description = "Upserts the smtp-* properties for the given role (primary or backup). Omit password to keep the previously stored one.")
    @PutMapping
    public SmtpSettingsStatusResponse save(@Valid @RequestBody SaveSmtpSettingsRequest request) {
        configurationDomainService.saveSmtpSettings(request.role(), request.host(), request.port(), request.username(), request.password(),
                request.fromAddress(), request.useTls());
        return status();
    }

    private static SmtpProviderStatus byRole(List<ResolvedSmtpSettings> all, SmtpRole role) {
        return all.stream()
                .filter(s -> s.role() == role)
                .findFirst()
                .map(s -> new SmtpProviderStatus(true, s.host(), s.port(), s.username(), s.fromAddress(), s.useTls()))
                .orElseGet(SmtpProviderStatus::unconfigured);
    }
}
