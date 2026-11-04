package com.spotit.api.spotlight.core.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.dto.SpotlightChallengeAdmin;
import com.spotit.api.spotlight.core.dto.SpotlightRankAdmin;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.entity.SpotlightRank;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.repository.SpotlightRankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SpotlightAdminServiceTest {
    @Mock SpotlightChallengeRepository challengeRepository;
    @Mock SpotlightRankRepository rankRepository;
    @Mock ConfigurationDomainService config;
    @InjectMocks SpotlightAdminService service;

    @BeforeEach
    void setUp() {
        when(challengeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(rankRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(config.getSpotlightAdminPageSize()).thenReturn(50);
    }

    @Test
    void ranksCanBeListedAddedAndRetuned() {
        when(rankRepository.findAllByOrderByMinStarsAsc()).thenReturn(List.of(SpotlightRank.builder().id("curious").name("Curious").minStars(0).active(true).build()));
        assertThat(service.ranks(null, null).items()).extracting("id").containsExactly("curious");

        SpotlightRankAdmin created = service.saveRank("explorer", new SpotlightRankAdmin(null, " Explorer ", 15, null));
        assertThat(created).extracting("name", "minStars", "active").containsExactly("Explorer", 15, true);

        SpotlightRank existing = SpotlightRank.builder().id("explorer").name("Explorer").minStars(15).active(true).build();
        when(rankRepository.findById("explorer")).thenReturn(Optional.of(existing));
        SpotlightRankAdmin patched = service.saveRank("explorer", new SpotlightRankAdmin(null, null, -3, false));
        assertThat(patched.minStars()).isZero();
        assertThat(patched.active()).isFalse();

        assertThatThrownBy(() -> service.saveRank("new", new SpotlightRankAdmin(null, "New", null, null)))
                .isInstanceOf(ApiException.class).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void challengesCanBeListedAddedRepricedAndChecked() {
        when(challengeRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(SpotlightChallenge.builder().id("know-yourself").kind("quiz")
                .name("Know Yourself").tone("pink").minAge(13).status("live").sortOrder(1).active(true).build()));
        assertThat(service.challenges(null, null).items()).extracting("id").containsExactly("know-yourself");

        SpotlightChallengeAdmin created = service.saveChallenge("pad-factory", new SpotlightChallengeAdmin(null, " Game ", " Pad Factory ", " Design pads ",
                "pad", "pink", 10, true, 13, null, 2, null, 2, " Know {name} better "));
        assertThat(created).extracting("kind", "name", "blurb", "cost", "costFrom", "status", "active", "freePlays", "partnerName")
                .containsExactly("game", "Pad Factory", "Design pads", 10, true, "soon", true, 2, "Know {name} better");

        SpotlightChallenge existing = SpotlightChallenge.builder().id("pad-factory").kind("game").name("Pad Factory").icon("pad").tone("pink").cost(10)
                .minAge(13).status("soon").sortOrder(2).active(true).build();
        when(challengeRepository.findById("pad-factory")).thenReturn(Optional.of(existing));
        SpotlightChallengeAdmin repriced = service.saveChallenge("pad-factory", new SpotlightChallengeAdmin(null, null, null, null, " ", "peach", -4,
                false, 10, "LIVE", null, false, -1, " "));
        assertThat(repriced).extracting("cost", "costFrom", "status", "minAge", "icon", "tone", "active", "freePlays", "partnerName")
                .containsExactly(0, false, "live", 13, null, "peach", false, 0, null);

        assertThatThrownBy(() -> service.saveChallenge("pad-factory", new SpotlightChallengeAdmin(null, "board", null, null, null, null, null, null,
                null, null, null, null, null, null))).isInstanceOf(ApiException.class).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.saveChallenge("pad-factory", new SpotlightChallengeAdmin(null, null, null, null, null, null, null, null,
                null, "later", null, null, null, null))).isInstanceOf(ApiException.class);
        assertThatThrownBy(() -> service.saveChallenge("new", new SpotlightChallengeAdmin(null, "game", null, null, null, null, null, null, null,
                null, null, null, null, null))).isInstanceOf(ApiException.class);
    }
}
