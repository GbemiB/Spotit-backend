package com.spotit.api.common.mail;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * All SMTP config (host/port/credentials/from-address) comes exclusively from the {@code global_configuration}
 * table (smtp-* properties) via {@link ConfigurationDomainService} — there is no env-var/yml fallback.
 * Seed the primary (and, optionally, backup) role via {@code ConfigurationDomainService.saveSmtpSettings(...)}
 * before mail can be sent.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String SENDER_DISPLAY_NAME = "Spot it";

    private final ConfigurationDomainService configurationDomainService;

    @Override
    public void send(String to, String subject, String htmlBody, String textBody) {
        List<ResolvedSmtpSettings> candidates = configurationDomainService.getSmtpSettingsInPriorityOrder();
        if (candidates.isEmpty()) {
            throw new MailPreparationException(
                    "No SMTP role configured — seed one via ConfigurationDomainService.saveSmtpSettings(...) before sending mail.");
        }

        // Primary is tried first; backup (if configured) only gets used when primary throws —
        // e.g. the provider is down, rate-limited, or (as happened with Gmail from this host)
        // silently unreachable. Only the final failure propagates, so a caller catching
        // MailException still sees exactly one exception either way.
        MailException lastFailure = null;
        for (int i = 0; i < candidates.size(); i++) {
            ResolvedSmtpSettings settings = candidates.get(i);
            try {
                sendVia(settings, to, subject, htmlBody, textBody);
                if (i > 0) {
                    log.warn("Sent via {} SMTP ({}) after {} failed", settings.role(), settings.host(), candidates.get(i - 1).role());
                }
                return;
            } catch (MailException e) {
                lastFailure = e;
                boolean hasNext = i < candidates.size() - 1;
                log.warn("SMTP send via {} ({}) failed{}", settings.role(), settings.host(),
                        hasNext ? " — trying backup" : " — no more providers configured", e);
            }
        }
        throw lastFailure;
    }

    private void sendVia(ResolvedSmtpSettings settings, String to, String subject, String htmlBody, String textBody) {
        JavaMailSender mailSender = buildMailSender(settings);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // multipart=true + setText(text, html) builds a multipart/alternative message —
            // clients that can't/won't render HTML (and spam filters that penalize HTML-only
            // mail) fall back to the plain-text part instead of showing nothing.
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(settings.fromAddress(), SENDER_DISPLAY_NAME);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textBody, htmlBody);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }

    private JavaMailSender buildMailSender(ResolvedSmtpSettings settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.host());
        mailSender.setPort(settings.port());
        mailSender.setUsername(settings.username());
        mailSender.setPassword(settings.password());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        // Port 465 is implicit TLS — the connection must be SSL from the first byte, STARTTLS
        // is never negotiated on it. Port 587 (and 25) is plaintext-then-upgrade: STARTTLS is
        // issued after connecting. Setting starttls.enable on a 465 host (or ssl.enable on a
        // 587 host) leaves the handshake mismatched and the send silently times out/fails —
        // which is what was happening against providers configured on 465.
        boolean implicitSsl = settings.port() == 465;
        props.put("mail.smtp.ssl.enable", String.valueOf(implicitSsl));
        props.put("mail.smtp.starttls.enable", String.valueOf(!implicitSsl && settings.useTls()));
        props.put("mail.smtp.starttls.required", String.valueOf(!implicitSsl && settings.useTls()));
        // JavaMail's default for these is infinite — without them, an unreachable/filtered SMTP
        // host (e.g. outbound SMTP blocked by the hosting provider) hangs the request thread
        // instead of failing fast.
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");
        return mailSender;
    }
}
