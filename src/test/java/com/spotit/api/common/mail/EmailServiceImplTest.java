package com.spotit.api.common.mail;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * EmailServiceImpl builds a fresh JavaMailSender from the stored settings on every send. Pointing it at a
 * closed local port exercises the whole build-and-send path (STARTTLS and implicit-SSL variants, with and
 * without an attachment) without a real relay: success is a MailSendException from the refused connection.
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks EmailServiceImpl service;

    private void relayAt(int port, boolean useTls) {
        when(configurationDomainService.getSmtpSettings())
                .thenReturn(Optional.of(new ResolvedSmtpSettings("127.0.0.1", port, "mailer", "s3cret", "no-reply@example.com", useTls)));
    }

    @Test
    void sendingWithoutSmtpSettingsFailsClearly() {
        when(configurationDomainService.getSmtpSettings()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.send("jane@example.com", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailPreparationException.class)
                .hasMessageContaining("No SMTP settings configured");
    }

    @Test
    void attachmentSendingWithoutSmtpSettingsFailsClearly() {
        when(configurationDomainService.getSmtpSettings()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.sendWithAttachment("jane@example.com", "Hi", "<p>Hi</p>", "Hi", "a.csv", new byte[0], "text/csv"))
                .isInstanceOf(MailPreparationException.class);
    }

    @Test
    void aStartTlsRelayThatCannotBeReachedSurfacesAsAMailSendException() {
        relayAt(1, true);

        assertThatThrownBy(() -> service.send("jane@example.com", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    void anImplicitSslRelayWithAnAttachmentGoesThroughTheSamePath() {
        relayAt(465, true);

        assertThatThrownBy(() -> service.sendWithAttachment("jane@example.com", "Export", "<p>Export</p>", "Export",
                "spotit-export.csv", "a,b".getBytes(StandardCharsets.UTF_8), "text/csv"))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    void aPlainRelayWithoutTlsIsAttemptedToo() {
        relayAt(1, false);

        assertThatThrownBy(() -> service.send("jane@example.com", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(MailSendException.class);
    }

    @Test
    void anInvalidRecipientIsAParseFailureNotASendAttempt() {
        relayAt(1, true);

        assertThatThrownBy(() -> service.send("not an address <<", "Hi", "<p>Hi</p>", "Hi"))
                .isInstanceOf(org.springframework.mail.MailParseException.class);
    }
}
