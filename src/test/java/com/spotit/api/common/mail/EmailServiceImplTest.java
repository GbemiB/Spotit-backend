package com.spotit.api.common.mail;

import com.spotit.api.smtp.SmtpProperties;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * EmailServiceImpl builds a fresh JavaMailSender from the SMTP properties on every send. Pointing it at a
 * closed local port exercises the whole build-and-send path (STARTTLS and implicit-SSL variants, with and
 * without an attachment) without a real relay: success is a MailSendException from the refused connection.
 */
class EmailServiceImplTest {

    private static EmailServiceImpl relayAt(int port, boolean useTls) {
        return new EmailServiceImpl(new SmtpProperties("127.0.0.1", port, "mailer", "s3cret", "no-reply@example.com", useTls));
    }

    private static EmailServiceImpl unconfigured() {
        // What an environment gets when the SMTP_* variables were never entered in Render.
        return new EmailServiceImpl(new SmtpProperties("smtp.gmail.com", 587, "", "", "", true));
    }

    @Test
    void sendingWithoutSmtpSettingsFailsClearly() {
        assertThatThrownBy(() -> unconfigured().send("jane@example.com", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailPreparationException.class)
                .hasMessageContaining("SMTP is not configured")
                .hasMessageContaining("SMTP_PASSWORD");
    }

    @Test
    void attachmentSendingWithoutSmtpSettingsFailsClearly() {
        assertThatThrownBy(() -> unconfigured().sendWithAttachment("jane@example.com", "Hi", "<p>Hi</p>", "Hi", "a.csv", new byte[0], "text/csv"))
                .isInstanceOf(MailPreparationException.class);
    }

    @Test
    void aStartTlsRelayThatCannotBeReachedSurfacesAsAMailSendException() {
        assertThatThrownBy(() -> relayAt(1, true).send("jane@example.com", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    void anImplicitSslRelayWithAnAttachmentGoesThroughTheSamePath() {
        assertThatThrownBy(() -> relayAt(465, true).sendWithAttachment("jane@example.com", "Export", "<p>Export</p>", "Export",
                "spotit-export.csv", "a,b".getBytes(StandardCharsets.UTF_8), "text/csv"))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    void aPlainRelayWithoutTlsIsAttemptedToo() {
        assertThatThrownBy(() -> relayAt(1, false).send("jane@example.com", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    void anInvalidRecipientIsAParseFailureNotASendAttempt() {
        assertThatThrownBy(() -> relayAt(1, true).send("not an address <<", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailParseException.class);
    }
}
