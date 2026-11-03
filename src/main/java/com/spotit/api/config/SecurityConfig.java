package com.spotit.api.config;

import com.spotit.api.common.security.JwtAuthenticationFilter;
import com.spotit.api.common.security.RestAccessDeniedHandler;
import com.spotit.api.common.security.RestAuthEntryPoint;
import com.spotit.api.common.security.SecurityUser;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] PUBLIC_PATHS = {
            "/api/v1/auth/signup",
            "/api/v1/auth/signup/complete",
            "/api/v1/auth/otp/verify",
            "/api/v1/auth/otp/resend",
            "/api/v1/auth/login",
            "/api/v1/auth/login/verify-otp",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/reset-password/verify-otp",
            "/api/v1/auth/refresh",
            "/api/v1/api-docs/**",
            "/api/v1/swagger-ui/**",
            "/api/v1/swagger-ui.html",
            "/privacy.html",
            "/terms.html",
            "/delete-account.html",
            "/actuator/health",
            "/api/v1/app/version-policy",
            "/api/v1/app/crashes",
            "/api/v1/app/support"
    };

    private static final String ADMIN_PATHS = "/api/v1/config/**";

    private static final String API_PATHS = "/api/v1/**";

    private static final String[] PARTNER_PATHS = {
            "/api/v1/partner/**",
            "/api/v1/auth/**",
            "/api/v1/devices/**",
            "/api/v1/notifications/**",
            "/api/v1/app/**",
            "/api/v1/rewards/**",
            "/api/v1/shop/**",
            "/api/v1/billing/**",
            "/api/v1/content/**",
            "/api/v1/users/me/export/**"
    };

    private static final String[] PARTNER_READ_PATHS = {
            "/api/v1/cycle/**",
            "/api/v1/logs/**",
            "/api/v1/insights/**"
    };

    private static final String PROFILE_PATH = "/api/v1/users/me";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthEntryPoint restAuthEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(restAuthEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .requestMatchers(PARTNER_PATHS).authenticated()
                        .requestMatchers(HttpMethod.GET, PARTNER_READ_PATHS).authenticated()
                        .requestMatchers(HttpMethod.GET, PROFILE_PATH).authenticated()
                        .requestMatchers(HttpMethod.PATCH, PROFILE_PATH).authenticated()
                        .requestMatchers(API_PATHS).access(SecurityConfig::signedInNonPartner)
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    static AuthorizationDecision signedInNonPartner(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        boolean signedIn = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        boolean partner = signedIn && auth.getAuthorities().stream()
                .anyMatch(authority -> SecurityUser.ROLE_PARTNER.equals(authority.getAuthority()));
        return new AuthorizationDecision(signedIn && !partner);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
