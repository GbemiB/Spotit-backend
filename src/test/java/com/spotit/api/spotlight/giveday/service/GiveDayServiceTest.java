package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayRunRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GiveDayServiceTest {
    @Mock UserRepository userRepository;
    @Mock UserClock userClock;
    @Mock ConfigurationDomainService config;
    @Mock PointsWriteService pointsWriteService;
    @Mock SpotlightChallengeRepository challengeRepository;
    @Mock GiveDayPlaceRepository placeRepository;
    @Mock GiveDayProductRepository productRepository;
    @Mock GiveDayNeedRepository needRepository;
    @Mock GiveDayRunRepository runRepository;
    @InjectMocks GiveDayService service;

    final UUID userId = UUID.randomUUID();
    final LocalDate today = LocalDate.of(2026, 11, 2);
    User user;
    SpotlightChallenge challenge;
    GiveDayPlace home;
    GiveDayPlace me;

    static GiveDayNeed need(String place, String product, String rating) {
        return GiveDayNeed.builder().id(place + ":" + product).placeId(place).productId(product).rating(rating).why("Because " + product).build();
    }

    @BeforeEach
    void setUp() {
        user = User.builder().id(userId).points(40).dob(LocalDate.of(2000, 1, 1)).build();
        challenge = SpotlightChallenge.builder().id("give-day").kind("game").cost(15).minAge(13).status("live").active(true).build();
        home = GiveDayPlace.builder().id("hope-house").name("Hope House").kind("Home").teaser("18 girls").hostName("Mama").hostLook("woman")
                .who("girls").total(18).counts("6,12,18").thanks("Thank you").facts("girls|pink|18 girls").mapX(70).mapY(205).minAge(13).active(true).build();
        me = GiveDayPlace.builder().id("my-kit").name("Pharmacy").kind("Own kit").teaser("You").hostName("You").hostLook("girl").who("you").total(1)
                .counts("1").facts("").forSelf(true).minAge(18).active(true).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userClock.today(user)).thenReturn(today);
        when(challengeRepository.findById("give-day")).thenReturn(Optional.of(challenge));
        when(config.getGivedayStartCoins()).thenReturn(200);
        when(config.getGivedayGoodPicks()).thenReturn(2);
        when(config.getGivedayTitleThree()).thenReturn("A perfect give!");
        when(config.getGivedayTitleTwo()).thenReturn("Lovely work");
        when(config.getGivedayTitleOne()).thenReturn("A good start");
        when(config.getGivedayTitleZero()).thenReturn("Something important is missing");
        when(placeRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(home, me));
        when(placeRepository.findById("hope-house")).thenReturn(Optional.of(home));
        when(placeRepository.findById("my-kit")).thenReturn(Optional.of(me));
        when(productRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(
                GiveDayProduct.builder().id("pads").name("Pads").cost(4).absorbent(true).build(),
                GiveDayProduct.builder().id("guide").name("Period guide").cost(1).build(),
                GiveDayProduct.builder().id("heat").name("Heat patch").cost(3).build(),
                GiveDayProduct.builder().id("cup").name("Cup").cost(12).absorbent(true).build()));
        when(needRepository.findByPlaceId("hope-house")).thenReturn(List.of(need("hope-house", "pads", "good"), need("hope-house", "guide", "good"),
                need("hope-house", "heat", "ok"), need("hope-house", "cup", "poor")));
        when(pointsWriteService.adjust(eq(userId), anyInt(), anyString(), anyString())).thenReturn(25L);
        when(runRepository.save(any())).thenAnswer(inv -> {
            GiveDayRun r = inv.getArgument(0);
            if (r.getId() == null) {
                r.setId(UUID.randomUUID());
            }
            return r;
        });
    }

    GiveDayRun run(int coins, String visits, String status) {
        GiveDayRun r = GiveDayRun.builder().id(UUID.randomUUID()).userId(userId).coins(coins).helped(0).visits(visits).status(status).playedOn(today).build();
        when(runRepository.findById(r.getId())).thenReturn(Optional.of(r));
        return r;
    }

    @Test
    void startingChargesTheChallengeOrUsesTheFreeTry() {
        GiveDayStartResponse paid = service.start(userId);
        verify(pointsWriteService).adjust(eq(userId), eq(-15), anyString(), eq("Give Day"));
        assertThat(paid).extracting("balance", "cost", "coins").containsExactly(25L, 15, 200);
        assertThat(paid.places()).extracting("id", "locked").containsExactly(org.assertj.core.groups.Tuple.tuple("hope-house", false),
                org.assertj.core.groups.Tuple.tuple("my-kit", false));
        assertThat(paid.places().get(0).counts()).containsExactly(6, 12, 18);
        assertThat(paid.products()).extracting("id").containsExactly("pads", "guide", "heat", "cup");

        challenge.setFreePlays(1);
        user.setPoints(0);
        assertThat(service.start(userId).cost()).isZero();
        verify(pointsWriteService).adjust(eq(userId), anyInt(), anyString(), anyString());
    }

    @Test
    void startingIsRefusedWhenClosedLockedEmptyOrUnaffordable() {
        challenge.setStatus("soon");
        assertThatThrownBy(() -> service.start(userId)).extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
        challenge.setStatus("live");
        challenge.setMinAge(18);
        user.setDob(LocalDate.of(2012, 1, 1));
        assertThatThrownBy(() -> service.start(userId)).extracting("errorCode").isEqualTo(ErrorCode.QUIZ_LOCKED);
        challenge.setMinAge(13);
        when(productRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of());
        assertThatThrownBy(() -> service.start(userId)).extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
        when(productRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(GiveDayProduct.builder().id("pads").cost(4).build()));
        user.setPoints(3);
        assertThatThrownBy(() -> service.start(userId)).extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
        challenge.setCost(0);
        service.start(userId);
        verify(pointsWriteService, never()).adjust(eq(userId), anyInt(), anyString(), anyString());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.start(userId)).isInstanceOf(ApiException.class);
    }

    @Test
    void aWellMatchedKitForEveryoneEarnsThreeStars() {
        GiveDayRun r = run(200, "", GiveDayRun.PLAYING);

        GiveDayGiveResponse give = service.give(userId, r.getId(), new GiveDayGiveRequest("hope-house", 18, List.of("pads", "guide", "pads")));

        assertThat(give).extracting("stars", "title", "thanks", "people", "spent", "coins", "helped")
                .containsExactly(3, "A perfect give!", "Thank you", 18, 90, 110, 18);
        assertThat(give.review()).extracting("productId", "rating").containsExactly(org.assertj.core.groups.Tuple.tuple("pads", "good"),
                org.assertj.core.groups.Tuple.tuple("guide", "good"));
        assertThat(give.missed()).isEmpty();
        assertThat(r.getVisits()).isEqualTo("hope-house:3");
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("hope-house", 6, List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);

        GiveDayGiveResponse self = service.give(userId, r.getId(), new GiveDayGiveRequest("my-kit", 1, List.of("heat")));
        assertThat(self).extracting("stars", "title", "helped").containsExactly(0, "Something important is missing", 18);
        assertThat(self.review().get(0).rating()).isEqualTo("ok");

        GiveDayFinishResponse done = service.finish(userId, r.getId());
        assertThat(done).extracting("stars", "helped", "coins").containsExactly(1, 18, 107);
        assertThat(r.getStatus()).isEqualTo(GiveDayRun.DONE);
        assertThatThrownBy(() -> service.finish(userId, r.getId())).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void weakerKitsGetFewerStarsAndShowWhatWasMissed() {
        GiveDayRun r = run(200, "", GiveDayRun.PLAYING);
        GiveDayGiveResponse one = service.give(userId, r.getId(), new GiveDayGiveRequest("hope-house", 6, List.of("cup", "heat")));
        assertThat(one).extracting("stars", "title").containsExactly(1, "A good start");
        assertThat(one.missed()).extracting("productId").containsExactlyInAnyOrder("pads", "guide");

        GiveDayRun two = run(200, "", GiveDayRun.PLAYING);
        assertThat(service.give(userId, two.getId(), new GiveDayGiveRequest("hope-house", 6, List.of("pads", "guide"))).title()).isEqualTo("Lovely work");
    }

    @Test
    void badGivesAreRefused() {
        GiveDayRun r = run(20, "", GiveDayRun.PLAYING);
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("nowhere", 6, List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("hope-house", 7, List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("hope-house", 6, java.util.Arrays.asList("glitter", null))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("hope-house", 6, List.of("cup"))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.give(UUID.randomUUID(), r.getId(), new GiveDayGiveRequest("hope-house", 6, List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
        GiveDayRun done = run(200, "", GiveDayRun.DONE);
        assertThatThrownBy(() -> service.give(userId, done.getId(), new GiveDayGiveRequest("hope-house", 6, List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }
}
