package com.spotit.api.auth;

import com.spotit.api.auth.dto.SignupRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercises POST /api/v1/auth/signup end to end: boots the app on in-memory H2 (which seeds the
 * spotit.smtp.* relay into global_configuration), calls the endpoint, and asserts the verification
 * OTP email actually went out through smtp.gmail.com — i.e. AuthWriteServiceImpl logged
 * "Signup OTP email sent to lead" and never "Failed to send signup OTP email" (that path swallows
 * MailException, so a 201 alone does not prove delivery).
 *
 * Sends a REAL email. Tagged "mail-it" and named *IT so the normal build skips it. Run explicitly:
 *   mvn test -Dtest=SignupMailDropIT [-Dmail.it.recipient=you@example.com]
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
@Tag("mail-it")
class SignupMailDropIT {

    @Autowired
    TestRestTemplate rest;

    @Test
    void signupSendsTheOtpEmail(CapturedOutput output) {
        String recipient = System.getProperty("mail.it.recipient", "bellotaiwo60@gmail.com");
        SignupRequest body = new SignupRequest("Mail", "Drop", recipient);

        ResponseEntity<String> response = rest.postForEntity("/api/v1/auth/signup", body, String.class);

        System.out.println("[mail-it] recipient   : " + recipient);
        System.out.println("[mail-it] HTTP status  : " + response.getStatusCode());
        System.out.println("[mail-it] response body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(output).contains("Signup OTP email sent to lead");
        assertThat(output).doesNotContain("Failed to send signup OTP email");
    }
}
