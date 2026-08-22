package com.spotit.api.smtp.service;

import com.spotit.api.config.SpotItProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * One-time migration path from env-var mail config to the DB-backed {@link SmtpSettingsService}.
 * On boot, if no settings row exists yet and the env-var-backed {@code JavaMailSender} has real
 * credentials, copies them into the DB once. After that first seed, {@code getActiveSettings()}
 * is present, so this becomes a no-op on every subsequent boot — the DB is the live source from
 * then on, not env vars.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SmtpSettingsBootstrapRunner implements ApplicationRunner {

    private final SmtpSettingsService smtpSettingsService;
    private final JavaMailSender defaultMailSender;
    private final SpotItProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        if (smtpSettingsService.getActiveSettings().isPresent()) {
            return;
        }
        if (!(defaultMailSender instanceof JavaMailSenderImpl impl) || impl.getUsername() == null || impl.getUsername().isBlank()) {
            log.info("No smtp_settings row and no env-var mail credentials to seed from — DB-backed mail stays unconfigured.");
            return;
        }

        boolean useTls = Boolean.parseBoolean(impl.getJavaMailProperties().getProperty("mail.smtp.starttls.enable", "true"));
        smtpSettingsService.saveSettings(impl.getHost(), impl.getPort(), impl.getUsername(), impl.getPassword(),
                properties.mail().fromAddress(), useTls);
        log.info("Seeded smtp_settings from env-var mail config ({}:{}) — DB is now the active source.", impl.getHost(), impl.getPort());
    }
}
