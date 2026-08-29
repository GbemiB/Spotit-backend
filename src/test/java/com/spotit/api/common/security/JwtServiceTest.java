package com.spotit.api.common.security;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String SECRET = "a-very-long-test-only-secret-that-is-definitely-long-enough-for-hmac";

    @Mock ConfigurationDomainService configurationDomainService;

    private JwtService serviceWithTtl(long accessTtlSeconds, long refreshTtlSeconds) {
        when(configurationDomainService.getJwtSecret()).thenReturn(SECRET);
        when(configurationDomainService.getJwtAccessTokenTtlSeconds()).thenReturn(accessTtlSeconds);
        when(configurationDomainService.getJwtRefreshTokenTtlSeconds()).thenReturn(refreshTtlSeconds);
        return new JwtService(configurationDomainService);
    }

    private JwtService service() {
        return serviceWithTtl(3600, 2_592_000);
    }

    @Test
    void accessTokenRoundTripsWithTheOriginalClaims() {
        JwtService jwtService = service();
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateAccessToken(userId, "user@example.com", true);
        Optional<SecurityUser> parsed = jwtService.parseAccessToken(token);

        assertThat(parsed).isPresent();
        assertThat(parsed.get().userId()).isEqualTo(userId);
        assertThat(parsed.get().email()).isEqualTo("user@example.com");
        assertThat(parsed.get().premium()).isTrue();
    }

    @Test
    void refreshTokenRoundTripsWithTheOriginalUserId() {
        JwtService jwtService = service();
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateRefreshToken(userId);
        Optional<UUID> parsed = jwtService.parseRefreshToken(token);

        assertThat(parsed).contains(userId);
    }

    @Test
    void accessTokenTypeIsRejectedByTheRefreshParser() {
        JwtService jwtService = service();
        String accessToken = jwtService.generateAccessToken(UUID.randomUUID(), "user@example.com", false);

        assertThat(jwtService.parseRefreshToken(accessToken)).isEmpty();
    }

    @Test
    void refreshTokenTypeIsRejectedByTheAccessParser() {
        JwtService jwtService = service();
        String refreshToken = jwtService.generateRefreshToken(UUID.randomUUID());

        assertThat(jwtService.parseAccessToken(refreshToken)).isEmpty();
    }

    @Test
    void garbageInputIsRejectedRatherThanThrowing() {
        JwtService jwtService = service();

        assertThat(jwtService.parseAccessToken("not-a-jwt")).isEmpty();
        assertThat(jwtService.parseRefreshToken("not-a-jwt")).isEmpty();
    }

    @Test
    void anAlreadyExpiredTokenFailsToParse() {
        JwtService jwtService = serviceWithTtl(-10, 2_592_000);
        String token = jwtService.generateAccessToken(UUID.randomUUID(), "user@example.com", false);

        assertThat(jwtService.parseAccessToken(token)).isEmpty();
    }

    @Test
    void exposesTheConfiguredAccessTokenTtl() {
        assertThat(serviceWithTtl(1234, 5678).accessTokenTtlSeconds()).isEqualTo(1234);
    }

    @Test
    void exposesTheConfiguredRefreshTokenTtl() {
        assertThat(serviceWithTtl(1234, 5678).refreshTokenTtlSeconds()).isEqualTo(5678);
    }
}
