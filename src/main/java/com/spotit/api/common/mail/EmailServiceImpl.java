package com.spotit.api.common.mail;

import com.spotit.api.smtp.SmtpProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String SENDER_DISPLAY_NAME = "Spot it";

    private final SmtpProperties smtpProperties;

    @Override
    public void send(String to, String subject, String htmlBody, String textBody) {
        sendVia(requireSettings(), to, subject, htmlBody, textBody, null, null, null);
    }

    @Override
    public void sendWithAttachment(String to, String subject, String htmlBody, String textBody,
                                    String attachmentFilename, byte[] attachmentBytes, String attachmentMimeType) {
        sendVia(requireSettings(), to, subject, htmlBody, textBody, attachmentFilename, attachmentBytes, attachmentMimeType);
    }

    private SmtpProperties requireSettings() {
        if (!smtpProperties.isConfigured()) {
            throw new MailPreparationException(
                    "SMTP is not configured — set SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD and SMTP_FROM_ADDRESS for this environment.");
        }
        return smtpProperties;
    }

    private void sendVia(SmtpProperties settings, String to, String subject, String htmlBody, String textBody,
                          String attachmentFilename, byte[] attachmentBytes, String attachmentMimeType) {
        JavaMailSender mailSender = buildMailSender(settings);
        boolean hasAttachment = attachmentFilename != null && attachmentBytes != null;
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // Always multipart: setText(text, html) writes alternative bodies, which needs multipart
            // mode even when there's no attachment.
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(settings.fromAddress(), SENDER_DISPLAY_NAME);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textBody, htmlBody);
            if (hasAttachment) {
                helper.addAttachment(attachmentFilename, new ByteArrayResource(attachmentBytes), attachmentMimeType);
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }

    private JavaMailSender buildMailSender(SmtpProperties settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.host());
        mailSender.setPort(settings.port());
        mailSender.setUsername(settings.username());
        mailSender.setPassword(settings.password());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");

        boolean implicitSsl = settings.port() == 465;
        props.put("mail.smtp.ssl.enable", String.valueOf(implicitSsl));
        props.put("mail.smtp.starttls.enable", String.valueOf(!implicitSsl && settings.useTls()));
        props.put("mail.smtp.starttls.required", String.valueOf(!implicitSsl && settings.useTls()));

        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");
        return mailSender;
    }
}
