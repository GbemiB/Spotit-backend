package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayHomeRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayHomeResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayTripsResponse;
import com.spotit.api.spotlight.giveday.entity.GiveDayArea;
import com.spotit.api.spotlight.giveday.entity.GiveDayLevel;
import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.giveday.entity.GiveDayPlayer;
import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.giveday.entity.GiveDayTravel;
import com.spotit.api.spotlight.giveday.repository.GiveDayAreaRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlayerRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayRunRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayTravelRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
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
    @Mock GiveDayAreaRepository areaRepository;
    @Mock GiveDayPlaceRepository placeRepository;
    @Mock GiveDayProductRepository productRepository;
    @Mock GiveDayNeedRepository needRepository;
    @Mock GiveDayRunRepository runRepository;
    @Mock GiveDayLevelRepository levelRepository;
    @Mock GiveDayTravelRepository travelRepository;
    @Mock GiveDayPlayerRepository playerRepository;
    @InjectMocks GiveDayService service;

    final UUID userId = UUID.randomUUID();
    final LocalDate today = LocalDate.of(2026, 11, 3);
    final List<GiveDayRun> runs = new ArrayList<>();
    User user;
    SpotlightChallenge challenge;
    GiveDayArea yaba;
    GiveDayArea akoka;
    GiveDayArea lekki;
    GiveDayPlace school;
    GiveDayPlace far;
    GiveDayLevel farLevel;

    static GiveDayNeed need(String place, String product, String rating) {
        return GiveDayNeed.builder().id(place + ":" + product).placeId(place).productId(product).rating(rating).why("Because " + product).build();
    }

    static GiveDayArea area(String id, int x, int y) {
        return GiveDayArea.builder().id(id).name(id).region("Lagos").mapX(x).mapY(y).active(true).build();
    }

    static GiveDayLevel level(int n, int km, int need, int pass, int gift) {
        return GiveDayLevel.builder().number(n).name("L" + n).radiusKm(km).starsNeeded(need).passCost(pass).giftBudget(gift).active(true).build();
    }

    @BeforeEach
    void setUp() {
        user = User.builder().id(userId).points(40).dob(LocalDate.of(2000, 1, 1)).build();
        challenge = SpotlightChallenge.builder().id("give-day").kind("game").cost(15).minAge(13).status("live").active(true).build();
        yaba = area("yaba", 1000, 1000);
        akoka = area("akoka", 1100, 1100);
        lekki = area("lekki", 3000, 1000);
        school = GiveDayPlace.builder().id("akoka-school").areaId("akoka").name("Bright School").kind("School").teaser("18 girls").hostName("Miss Ada")
                .hostLook("teacher").who("girls").total(18).thanks("Thank you").facts("girls|pink|18 girls").mapX(1100).mapY(1100).closes(900)
                .minAge(13).active(true).build();
        far = GiveDayPlace.builder().id("lekki-market").areaId("lekki").name("Market").kind("Market").teaser("Traders").hostName("Mama")
                .hostLook("market").who("traders").total(20).facts("").mapX(3000).mapY(1000).closes(720).minAge(13).active(true).build();
        farLevel = level(4, 30, 20, 15, 10);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userClock.today(user)).thenReturn(today);
        when(challengeRepository.findById("give-day")).thenReturn(Optional.of(challenge));
        when(config.getGivedayDayStart()).thenReturn(480);
        when(config.getGivedayDayEnd()).thenReturn(1080);
        when(config.getGivedayVisitMinutes()).thenReturn(30);
        when(config.getGivedayUnitsPerKm()).thenReturn(100);
        when(config.getGivedayGoodPicks()).thenReturn(2);
        when(config.getGivedayTitleThree()).thenReturn("A perfect give!");
        when(config.getGivedayTitleTwo()).thenReturn("Lovely work");
        when(config.getGivedayTitleOne()).thenReturn("A good start");
        when(config.getGivedayTitleZero()).thenReturn("Something important is missing");
        when(areaRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(yaba, akoka, lekki));
        List.of(yaba, akoka, lekki).forEach(a -> when(areaRepository.findById(a.getId())).thenReturn(Optional.of(a)));
        when(placeRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(school, far));
        when(placeRepository.findByAreaIdAndActiveTrueOrderBySortOrderAsc("akoka")).thenReturn(List.of(school));
        when(placeRepository.findById("akoka-school")).thenReturn(Optional.of(school));
        when(placeRepository.findById("lekki-market")).thenReturn(Optional.of(far));
        when(levelRepository.findByActiveTrueOrderByNumberAsc()).thenReturn(List.of(level(1, 3, 0, 0, 5), level(2, 8, 3, 0, 5), level(3, 15, 10, 10, 10),
                farLevel));
        when(travelRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(
                GiveDayTravel.builder().id("walk").label("Walk").cost(0).unitsPerMinute(8).active(true).build(),
                GiveDayTravel.builder().id("car").label("Car").cost(8).unitsPerMinute(50).active(true).build()));
        when(travelRepository.findById("walk")).thenReturn(Optional.of(GiveDayTravel.builder().id("walk").label("Walk").cost(0).unitsPerMinute(8).active(true).build()));
        when(travelRepository.findById("car")).thenReturn(Optional.of(GiveDayTravel.builder().id("car").label("Car").cost(8).unitsPerMinute(50).active(true).build()));
        when(productRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(
                GiveDayProduct.builder().id("pads").name("Pads").cost(2).absorbent(true).build(),
                GiveDayProduct.builder().id("guide").name("Period guide").cost(1).build(),
                GiveDayProduct.builder().id("heat").name("Heat patch").cost(1).build(),
                GiveDayProduct.builder().id("cup").name("Cup").cost(3).absorbent(true).build()));
        when(needRepository.findByPlaceId("akoka-school")).thenReturn(List.of(need("akoka-school", "pads", "good"), need("akoka-school", "guide", "good"),
                need("akoka-school", "heat", "ok"), need("akoka-school", "cup", "poor")));
        when(pointsWriteService.adjust(eq(userId), anyInt(), anyString(), anyString())).thenReturn(25L);
        when(runRepository.findByUserId(userId)).thenReturn(runs);
        when(runRepository.save(any())).thenAnswer(inv -> {
            GiveDayRun r = inv.getArgument(0);
            if (r.getId() == null) {
                r.setId(UUID.randomUUID());
            }
            return r;
        });
    }

    GiveDayRun run(String home, String visits, String status) {
        GiveDayRun r = GiveDayRun.builder().id(UUID.randomUUID()).userId(userId).helped(0).visits(visits).status(status).clock(480).homeArea(home)
                .posX(1000).posY(1000).passes("").playedOn(today).build();
        when(runRepository.findById(r.getId())).thenReturn(Optional.of(r));
        runs.add(r);
        return r;
    }

    @Test
    void startingWithoutAHomeAsksForOneAndChargesTheChallenge() {
        GiveDayStartResponse paid = service.start(userId);
        verify(pointsWriteService).adjust(eq(userId), eq(-15), anyString(), eq("Give Day"));
        assertThat(paid).extracting("balance", "cost", "clock", "dayEnd", "visitMinutes", "unitsPerKm", "home", "stars", "level")
                .containsExactly(25L, 15, 480, 1080, 30, 100, null, 0, 1);
        assertThat(paid.areas()).extracting("id", "places", "level", "locked")
                .containsExactly(tuple("yaba", 0, 1, false), tuple("akoka", 1, 1, false), tuple("lekki", 1, 1, false));
        assertThat(paid.levels()).extracting("number", "radiusKm").startsWith(tuple(1, 3));
        assertThat(paid.travel()).extracting("id").containsExactly("walk", "car");
        assertThat(paid.products()).extracting("id").containsExactly("pads", "guide", "heat", "cup");

        challenge.setFreePlays(1);
        user.setPoints(0);
        assertThat(service.start(userId).cost()).isZero();
    }

    @Test
    void startingFromHomeGivesEachAreaItsRing() {
        when(playerRepository.findById(userId)).thenReturn(Optional.of(GiveDayPlayer.builder().userId(userId).homeArea("yaba").build()));
        challenge.setCost(0);
        GiveDayStartResponse start = service.start(userId);
        verify(pointsWriteService, never()).adjust(eq(userId), anyInt(), anyString(), anyString());
        assertThat(start).extracting("home", "x", "y").containsExactly("yaba", 1000, 1000);
        assertThat(start.areas()).extracting("id", "level", "locked")
                .containsExactly(tuple("yaba", 1, false), tuple("akoka", 1, false), tuple("lekki", 4, true));
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
        when(areaRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of());
        assertThatThrownBy(() -> service.start(userId)).extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
        when(areaRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(yaba));
        user.setPoints(3);
        assertThatThrownBy(() -> service.start(userId)).extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.start(userId)).isInstanceOf(ApiException.class);
    }

    @Test
    void pickingAHomeMovesDaysThatHaveNotTravelled() {
        GiveDayRun fresh = run(null, "", GiveDayRun.PLAYING);
        fresh.setPosX(0);
        GiveDayRun moved = run("akoka", "", GiveDayRun.PLAYING);
        moved.setHere("akoka-school");
        GiveDayHomeResponse home = service.home(userId, new GiveDayHomeRequest("lekki"));
        verify(playerRepository).save(any(GiveDayPlayer.class));
        assertThat(home).extracting("home", "x", "y").containsExactly("lekki", 3000, 1000);
        assertThat(home.areas()).extracting("level").containsExactly(4, 4, 1);
        assertThat(fresh).extracting("homeArea", "posX").containsExactly("lekki", 3000);
        assertThat(moved.getHomeArea()).isEqualTo("akoka");
        assertThatThrownBy(() -> service.home(userId, new GiveDayHomeRequest("atlantis")))
                .hasMessage(ErrorMessage.GIVEDAY_BAD_AREA);
    }

    @Test
    void anAreaListsItsPlacesWithTodaysStars() {
        GiveDayRun r = run("yaba", "akoka-school:2", GiveDayRun.PLAYING);
        GiveDayAreaResponse area = service.area(userId, r.getId(), "akoka");
        assertThat(area).extracting("areaId", "level", "locked", "passNeeded", "giftBudget").containsExactly("akoka", 1, false, false, 5);
        assertThat(area.places()).extracting("id", "closes", "stars").containsExactly(tuple("akoka-school", 900, 2));

        GiveDayAreaResponse lagos = service.area(userId, r.getId(), "lekki");
        assertThat(lagos).extracting("level", "locked", "starsNeeded", "passNeeded", "passCost").containsExactly(4, true, 20, true, 15);

        GiveDayRun homeless = run(null, "", GiveDayRun.PLAYING);
        assertThatThrownBy(() -> service.area(userId, homeless.getId(), "akoka")).hasMessage(ErrorMessage.GIVEDAY_NO_HOME);
        assertThatThrownBy(() -> service.area(userId, r.getId(), null)).hasMessage(ErrorMessage.GIVEDAY_BAD_AREA);
    }

    @Test
    void tripsQuoteEachWayAndWalkingFarCanMissClosing() {
        GiveDayRun r = run("yaba", "", GiveDayRun.PLAYING);
        GiveDayTripsResponse near = service.trips(userId, r.getId(), "akoka-school");
        assertThat(near).extracting("level", "levelOpen", "passNeeded", "giftBudget").containsExactly(1, true, false, 5);
        assertThat(near.quotes()).extracting("mode", "minutes", "arrive", "open").containsExactly(tuple("walk", 25, 505, true), tuple("car", 5, 485, true));

        farLevel.setStarsNeeded(0);
        GiveDayTripsResponse lekkiTrips = service.trips(userId, r.getId(), "lekki-market");
        assertThat(lekkiTrips).extracting("level", "levelOpen", "passNeeded", "passCost").containsExactly(4, true, true, 15);
        assertThat(lekkiTrips.quotes()).extracting("mode", "minutes", "open").containsExactly(tuple("walk", 250, false), tuple("car", 40, true));
    }

    @Test
    void travellingChargesTheWayAndADayPassOnce() {
        farLevel.setStarsNeeded(0);
        user.setPoints(100);
        GiveDayRun r = run("yaba", "", GiveDayRun.PLAYING);
        assertThatThrownBy(() -> service.travel(userId, r.getId(), new GiveDayTravelRequest("lekki-market", "walk")))
                .hasMessage(ErrorMessage.GIVEDAY_CLOSED);
        GiveDayTravelResponse car = service.travel(userId, r.getId(), new GiveDayTravelRequest("lekki-market", "car"));
        verify(pointsWriteService).adjust(userId, -23, "🎁", "Give Day: Car + level 4 pass");
        assertThat(car).extracting("minutes", "clock", "x", "y", "spent", "spentToday").containsExactly(40, 520, 3000, 1000, 23, 23);
        assertThat(r).extracting("passes", "here").containsExactly("4", "lekki-market");
        r.setHere(null);
        r.setPosX(1000);
        assertThat(service.travel(userId, r.getId(), new GiveDayTravelRequest("lekki-market", "car")).spent()).isEqualTo(8);

        user.setPoints(5);
        GiveDayRun poor = run("yaba", "", GiveDayRun.PLAYING);
        assertThatThrownBy(() -> service.travel(userId, poor.getId(), new GiveDayTravelRequest("lekki-market", "car")))
                .extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
        assertThatThrownBy(() -> service.travel(userId, poor.getId(), new GiveDayTravelRequest("akoka-school", "jet")))
                .hasMessage(ErrorMessage.GIVEDAY_BAD_TRAVEL);
        farLevel.setStarsNeeded(20);
        assertThatThrownBy(() -> service.travel(userId, poor.getId(), new GiveDayTravelRequest("lekki-market", "car")))
                .hasMessage(ErrorMessage.GIVEDAY_LEVEL_LOCKED);
    }

    @Test
    void aWellMatchedGiftWithinBudgetEarnsThreeStars() {
        GiveDayRun r = run("yaba", "", GiveDayRun.PLAYING);
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads"))))
                .hasMessage(ErrorMessage.GIVEDAY_NOT_THERE);
        service.travel(userId, r.getId(), new GiveDayTravelRequest("akoka-school", "walk"));
        verify(pointsWriteService, never()).adjust(eq(userId), anyInt(), anyString(), anyString());
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads", "cup", "guide"))))
                .hasMessage(ErrorMessage.GIVEDAY_OVER_BUDGET);

        GiveDayGiveResponse give = service.give(userId, r.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads", "guide", "pads")));
        assertThat(give).extracting("stars", "title", "thanks", "people", "giftCost", "giftBudget", "helped", "clock", "playerStars", "level")
                .containsExactly(3, "A perfect give!", "Thank you", 18, 3, 5, 18, 535, 3, 2);
        assertThat(give.missed()).isEmpty();
        assertThat(r.getVisits()).isEqualTo("akoka-school:3");
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads"))))
                .hasMessage(ErrorMessage.GIVEDAY_PLACE_DONE);
        assertThatThrownBy(() -> service.travel(userId, r.getId(), new GiveDayTravelRequest("akoka-school", "walk")))
                .hasMessage(ErrorMessage.GIVEDAY_PLACE_DONE);

        GiveDayFinishResponse done = service.finish(userId, r.getId());
        assertThat(done).extracting("stars", "helped", "spentToday").containsExactly(3, 18, 0);
        assertThat(r.getStatus()).isEqualTo(GiveDayRun.DONE);
        assertThatThrownBy(() -> service.finish(userId, r.getId())).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void weakerGiftsGetFewerStarsAndShowWhatWasMissed() {
        GiveDayRun r = run("yaba", "", GiveDayRun.PLAYING);
        r.setHere("akoka-school");
        GiveDayGiveResponse one = service.give(userId, r.getId(), new GiveDayGiveRequest("akoka-school", List.of("cup", "heat")));
        assertThat(one).extracting("stars", "title").containsExactly(1, "A good start");
        assertThat(one.missed()).extracting("productId").containsExactlyInAnyOrder("pads", "guide");

        GiveDayRun two = run("yaba", "", GiveDayRun.PLAYING);
        two.setHere("akoka-school");
        assertThat(service.give(userId, two.getId(), new GiveDayGiveRequest("akoka-school", List.of("heat"))).title())
                .isEqualTo("Something important is missing");
        GiveDayRun three = run("yaba", "", GiveDayRun.PLAYING);
        three.setHere("akoka-school");
        assertThat(service.give(userId, three.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads", "heat"))).title()).isEqualTo("Lovely work");
    }

    @Test
    void badGivesAreRefused() {
        GiveDayRun r = run("yaba", "", GiveDayRun.PLAYING);
        r.setHere("akoka-school");
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("nowhere", List.of("pads"))))
                .hasMessage(ErrorMessage.GIVEDAY_BAD_PLACE);
        assertThatThrownBy(() -> service.give(userId, r.getId(), new GiveDayGiveRequest("akoka-school", java.util.Arrays.asList("glitter", null))))
                .hasMessage(ErrorMessage.GIVEDAY_BAD_PRODUCT);
        assertThatThrownBy(() -> service.give(UUID.randomUUID(), r.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
        GiveDayRun done = run("yaba", "", GiveDayRun.DONE);
        assertThatThrownBy(() -> service.give(userId, done.getId(), new GiveDayGiveRequest("akoka-school", List.of("pads"))))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }
}
