package com.spotit.api.auth.service;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OtpCodesTest {

    @Test
    void codesAreAlwaysFourDigitsIncludingLeadingZeros() {
        for (int i = 0; i < 5_000; i++) {
            assertThat(OtpCodes.newCode()).matches("\\d{4}");
        }
    }

    @Test
    void codesVary() {
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            seen.add(OtpCodes.newCode());
        }
        assertThat(seen.size()).isGreaterThan(100);
    }
}
