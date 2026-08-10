package com.spotit.api.common.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenHasherTest {

    @Test
    void producesTheKnownSha256HexDigest() {
        // Well-known test vector: sha256("hello")
        assertThat(TokenHasher.sha256Hex("hello"))
                .isEqualTo("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
    }

    @Test
    void isDeterministic() {
        assertThat(TokenHasher.sha256Hex("some-refresh-token"))
                .isEqualTo(TokenHasher.sha256Hex("some-refresh-token"));
    }

    @Test
    void differentInputsProduceDifferentHashes() {
        assertThat(TokenHasher.sha256Hex("token-a")).isNotEqualTo(TokenHasher.sha256Hex("token-b"));
    }

    @Test
    void isLowercaseHexOfTheExpectedLength() {
        String hash = TokenHasher.sha256Hex("anything");

        assertThat(hash).hasSize(64);
        assertThat(hash).matches("[0-9a-f]+");
    }
}
