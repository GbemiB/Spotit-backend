package com.spotit.api.common.security;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Issues and verifies signed JWT access/refresh tokens. Access tokens carry
 * enough claims (email, premium flag) to build a {@link SecurityUser} without
 * a DB round-trip on every request; refresh tokens are opaque beyond the
 * subject + type so they can't be used as access tokens if leaked.
 *
 * <p>The signing key and TTLs are resolved once from {@link ConfigurationDomainService} at
 * construction time, not re-read per call — {@link com.spotit.api.common.security.JwtAuthenticationFilter}
 * runs on every request, so re-querying the DB there would add a round-trip to every API call.
 * Changing the JWT secret/TTLs in {@code global_configuration} therefore takes effect on next
 * restart, not live — appropriate for a signing key, which shouldn't rotate silently underneath
 * issued tokens anyway. {@code ConfigurationDomainServiceImpl} seeds its rows via
 * {@code @PostConstruct}, so they're guaranteed present by the time this constructor runs.
 */
@Service
public class JwtService {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_PREMIUM = "premium";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final SecretKey key;
    private final long accessTokenTtlSeconds;
    private final long refreshTokenTtlSeconds;

    public JwtService(ConfigurationDomainService configurationDomainService) {
        this.key = Keys.hmacShaKeyFor(configurationDomainService.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtlSeconds = configurationDomainService.getJwtAccessTokenTtlSeconds();
        this.refreshTokenTtlSeconds = configurationDomainService.getJwtRefreshTokenTtlSeconds();
    }

    public String generateAccessToken(UUID userId, String email, boolean premium) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_PREMIUM, premium)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .id(UUID.randomUUID().toString())
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTokenTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public long accessTokenTtlSeconds() {
        return accessTokenTtlSeconds;
    }

    public long refreshTokenTtlSeconds() {
        return refreshTokenTtlSeconds;
    }

    public Optional<SecurityUser> parseAccessToken(String token) {
        try {
            Claims claims = parseClaims(token);
            if (!TYPE_ACCESS.equals(claims.get(CLAIM_TYPE, String.class))) {
                return Optional.empty();
            }
            UUID userId = UUID.fromString(claims.getSubject());
            String email = claims.get(CLAIM_EMAIL, String.class);
            boolean premium = Boolean.TRUE.equals(claims.get(CLAIM_PREMIUM, Boolean.class));
            return Optional.of(new SecurityUser(userId, email, premium));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<UUID> parseRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            if (!TYPE_REFRESH.equals(claims.get(CLAIM_TYPE, String.class))) {
                return Optional.empty();
            }
            return Optional.of(UUID.fromString(claims.getSubject()));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
