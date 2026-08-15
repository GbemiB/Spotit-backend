package com.spotit.api.common.mail;

import com.spotit.api.config.SpotItProperties;
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
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String SENDER_DISPLAY_NAME = "Spot it";
    private static final Set<String> LOOPBACK_HOSTS = Set.of("localhost", "127.0.0.1", "::1");

    private final JavaMailSender mailSender;
    private final SpotItProperties properties;

    // Logs where OTP mail is actually headed on every boot — the "dev" profile silently points
    // at a local Mailpit catcher (see application-dev.yml), so without this it's easy to spend a
    // while wondering why a real inbox never gets an email that the app thinks it sent.
    @PostConstruct
    void logMailTarget() {
        if (mailSender instanceof JavaMailSenderImpl impl) {
            boolean isLocalCatcher = LOOPBACK_HOSTS.contains(impl.getHost());
            log.info("Outbound mail target: {}:{} (From: {}){}", impl.getHost(), impl.getPort(), properties.mail().fromAddress(),
                    isLocalCatcher
                            ? " — this is a LOCAL catcher (e.g. Mailpit); OTP emails will NOT reach a real inbox from here"
                            : "");
        }
    }

    @Override
    public void send(String to, String subject, String htmlBody, String textBody) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // multipart=true + setText(text, html) builds a multipart/alternative message —
            // clients that can't/won't render HTML (and spam filters that penalize HTML-only
            // mail) fall back to the plain-text part instead of showing nothing.
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(properties.mail().fromAddress(), SENDER_DISPLAY_NAME);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textBody, htmlBody);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }
}
