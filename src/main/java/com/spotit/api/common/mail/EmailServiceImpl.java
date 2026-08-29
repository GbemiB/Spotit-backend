package com.spotit.api.common.mail;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String SENDER_DISPLAY_NAME = "Spot it";

    private final ConfigurationDomainService configurationDomainService;

    @Override
    public void send(String to, String subject, String htmlBody, String textBody) {
        ResolvedSmtpSettings settings = configurationDomainService.getSmtpSettings()
                .orElseThrow(() -> new MailPreparationException(
                        "No SMTP settings configured — seed one via ConfigurationDomainService.saveSmtpSettings(...) before sending mail."));
        sendVia(settings, to, subject, htmlBody, textBody);
    }

    private void sendVia(ResolvedSmtpSettings settings, String to, String subject, String htmlBody, String textBody) {
        JavaMailSender mailSender = buildMailSender(settings);
        MimeMessage message = mailSender.createMimeMessage();
        try {
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
