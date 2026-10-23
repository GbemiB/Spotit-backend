package com.spotit.api.common.security;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViewedUserIdArgumentResolverTest {
    @Mock PartnerLinkRepository partnerLinkRepository;
    @InjectMocks ViewedUserIdArgumentResolver resolver;

    UUID userId = UUID.randomUUID();
    UUID ownerId = UUID.randomUUID();

    void signIn(boolean partner) {
        SecurityUser user = new SecurityUser(userId, "someone@example.com", false, false, partner);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void anOwnerSeesHerOwnData() {
        signIn(false);
        assertThat(resolver.resolveArgument(null, null, null, null)).isEqualTo(userId);
        verifyNoInteractions(partnerLinkRepository);
    }

    @Test
    void aLinkedPartnerSeesTheOwnersData() {
        signIn(true);
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(userId, PartnerLinkStatus.active))
                .thenReturn(Optional.of(PartnerLink.builder().ownerId(ownerId).partnerId(userId).build()));
        assertThat(resolver.resolveArgument(null, null, null, null)).isEqualTo(ownerId);
    }

    @Test
    void aPartnerWhoseSharingEndedSeesNothing() {
        signIn(true);
        assertThatThrownBy(() -> resolver.resolveArgument(null, null, null, null)).isInstanceOf(ApiException.class);
    }
}
