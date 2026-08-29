package com.spotit.api.common.mail;

public interface EmailService {
    void send(String to, String subject, String htmlBody, String textBody);
}
