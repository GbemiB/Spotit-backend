package com.spotit.api.common.mail;

import com.spotit.api.config.SpotItProperties;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import com.spotit.api.smtp.service.SmtpSettingsService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String SENDER_DISPLAY_NAME = "Spot it";
    private static final Set<String> LOOPBACK_HOSTS = Set.of("localhost", "127.0.0.1", "::1");

    // env-var-backed bean (spring.mail.*) — used only when no DB-configured settings exist yet.
    private final JavaMailSender defaultMailSender;
    private final SpotItProperties properties;
    private final SmtpSettingsService smtpSettingsService;

    // Logs where OTP mail is actually headed on every boot, so it's easy to confirm at a glance
    // whether MAIL_HOST/MAIL_USERNAME etc. resolved to the intended real mailbox.
    @PostConstruct
    void logMailTarget() {
        if (defaultMailSender instanceof JavaMailSenderImpl impl) {
            boolean isLoopbackHost = LOOPBACK_HOSTS.contains(impl.getHost());
            log.info("Default (env-var) outbound mail target: {}:{} (From: {}){}", impl.getHost(), impl.getPort(),
                    properties.mail().fromAddress(),
                    isLoopbackHost ? " — this is a LOOPBACK host; OTP emails will NOT reach a real inbox from here" : "");
        }
    }

    @Override
    public void send(String to, String subject, String htmlBody, String textBody) {
        ResolvedSmtpSettings dbSettings = smtpSettingsService.getActiveSettings().orElse(null);
        JavaMailSender mailSender = dbSettings != null ? buildMailSender(dbSettings) : defaultMailSender;
        String fromAddress = dbSettings != null ? dbSettings.fromAddress() : properties.mail().fromAddress();

        MimeMessage message = mailSender.createMimeMessage();
        try {
            // multipart=true + setText(text, html) builds a multipart/alternative message —
            // clients that can't/won't render HTML (and spam filters that penalize HTML-only
            // mail) fall back to the plain-text part instead of showing nothing.
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress, SENDER_DISPLAY_NAME);
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
        return mailSender;
    }
}
