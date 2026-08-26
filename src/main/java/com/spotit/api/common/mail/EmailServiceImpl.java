package com.spotit.api.common.mail;

import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import com.spotit.api.smtp.service.SmtpSettingsService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * All SMTP config (host/port/credentials/from-address) comes exclusively from the {@code smtp_settings}
 * DB table via {@link SmtpSettingsService} — there is no env-var/yml fallback. Seed that row via
 * {@code SmtpSettingsService.saveSettings(...)} (or a direct SQL insert) before mail can be sent.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String SENDER_DISPLAY_NAME = "Spot it";

    private final SmtpSettingsService smtpSettingsService;

    @Override
    public void send(String to, String subject, String htmlBody, String textBody) {
        ResolvedSmtpSettings settings = smtpSettingsService.getActiveSettings()
                .orElseThrow(() -> new MailPreparationException(
                        "No smtp_settings row configured — seed one via SmtpSettingsService.saveSettings(...) before sending mail."));
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
        props.put("mail.smtp.starttls.enable", String.valueOf(settings.useTls()));
        // JavaMail's default for these is infinite — without them, an unreachable/filtered SMTP
        // host (e.g. outbound SMTP blocked by the hosting provider) hangs the request thread
        // instead of failing fast.
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");
        return mailSender;
    }
}
