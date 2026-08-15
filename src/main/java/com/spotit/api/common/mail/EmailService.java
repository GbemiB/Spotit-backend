package com.spotit.api.common.mail;

/** Thin transport for outbound HTML email; callers own subject/body content. */
public interface EmailService {

    /**
     * Sends a multipart/alternative message: {@code htmlBody} for clients that render HTML,
     * {@code textBody} as the fallback for clients/spam filters that prefer plain text.
     */
    void send(String to, String subject, String htmlBody, String textBody);
}
