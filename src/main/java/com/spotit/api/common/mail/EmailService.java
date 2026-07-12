package com.spotit.api.common.mail;

/** Thin transport for outbound email; callers own subject/body content. */
public interface EmailService {

    void send(String to, String subject, String body);
}
