package com.spotit.api.common.mail;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OtpEmailTemplateTest {

    @Test
    void theCodeIsShownWholeOnOneLineSoItFitsAPhoneScreen() {
        String html = OtpEmailTemplate.html("Jane", "482913", "Verify your email", "here is your code.", 10);

        assertThat(html).contains(">482913</span>");
        assertThat(html).doesNotContain("4 8 2 9 1 3");
        assertThat(html).contains("white-space:nowrap");
    }

    @Test
    void theEmailCarriesTheGreetingHeadingAndExpiry() {
        String html = OtpEmailTemplate.html("Jane", "482913", "Verify your email", "here is your code.", 10);

        assertThat(html).contains("Hi Jane, here is your code.").contains("Verify your email").contains("10 minutes");
    }

    @Test
    void thePlainTextVersionHasTheCode() {
        assertThat(OtpEmailTemplate.text("Jane", "482913", "Verify your email", "here is your code.", 10))
                .contains("Your code: 482913").contains("10 minutes");
    }
}
