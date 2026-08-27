package com.spotit.api.smtp.controller;

import com.spotit.api.smtp.dto.SaveSmtpSettingsRequest;
import com.spotit.api.smtp.dto.SmtpSettingsStatusResponse;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import com.spotit.api.smtp.service.SmtpSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Admin surface for the single {@code smtp_settings} row that {@link com.spotit.api.common.mail.EmailServiceImpl}
 * sends all mail through (OTP codes, password resets). Requires the caller to be signed in, same
 * as the other {@code /config/*} admin controllers in this codebase — there is no separate admin
 * role, so use any authenticated account's access token.
 */
@Tag(name = "Smtp Config (Admin)", description = "Admin surface for the SMTP relay settings used to send all transactional mail.")
@RestController
@RequestMapping("/api/v1/config/smtp")
@RequiredArgsConstructor
public class SmtpConfigController {

    private final SmtpSettingsService smtpSettingsService;

    @Operation(summary = "Get SMTP status", description = "Returns what's configured (never the password) so an admin can confirm settings without guessing.")
    @GetMapping
    public SmtpSettingsStatusResponse status() {
        return smtpSettingsService.getActiveSettings()
                .map(SmtpConfigController::toStatus)
                .orElseGet(SmtpSettingsStatusResponse::unconfigured);
    }

    @Operation(summary = "Save SMTP settings", description = "Upserts the single smtp_settings row. Omit password to keep the previously stored one.")
    @PutMapping
    public SmtpSettingsStatusResponse save(@Valid @RequestBody SaveSmtpSettingsRequest request) {
        smtpSettingsService.saveSettings(request.host(), request.port(), request.username(), request.password(),
                request.fromAddress(), request.useTls());
        return status();
    }

    private static SmtpSettingsStatusResponse toStatus(ResolvedSmtpSettings s) {
        return new SmtpSettingsStatusResponse(true, s.host(), s.port(), s.username(), s.fromAddress(), s.useTls());
    }
}
