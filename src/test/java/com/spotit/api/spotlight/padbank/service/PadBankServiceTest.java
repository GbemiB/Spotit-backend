package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.padbank.dto.PadBankServeRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankServeResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankStartRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankStartResponse;
import com.spotit.api.spotlight.padbank.entity.PadBankPack;
import com.spotit.api.spotlight.padbank.entity.PadBankRun;
import com.spotit.api.spotlight.padbank.entity.PadBankVisitor;
import com.spotit.api.spotlight.padbank.repository.PadBankPackRepository;
import com.spotit.api.spotlight.padbank.repository.PadBankRunRepository;
import com.spotit.api.spotlight.padbank.repository.PadBankVisitorRepository;
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
class PadBankServiceTest {
    @Mock UserRepository userRepository;
    @Mock UserClock userClock;
    @Mock ConfigurationDomainService config;
    @Mock PointsWriteService pointsWriteService;
    @Mock SpotlightChallengeRepository challengeRepository;
    @Mock PadBankPackRepository packRepository;
    @Mock PadBankVisitorRepository visitorRepository;
    @Mock PadBankRunRepository runRepository;
    @InjectMocks PadBankService service;

    final UUID userId = UUID.randomUUID();
    final LocalDate today = LocalDate.of(2026, 11, 2);
    User user;
    SpotlightChallenge challenge;

    static PadBankVisitor visitor(String id, String pack, String route) {
        return PadBankVisitor.builder().id(id).day(1).who("Girl, 13").look("girl").clues("drop|First period").pack(pack).route(route)
                .thanks("Thank you!").routeWhy("Pickup was free.").wrongWhy("comfort|Too much for a first period.").active(true).build();
    }

    @BeforeEach
    void setUp() {
        user = User.builder().id(userId).points(50).dob(LocalDate.of(2000, 1, 1)).build();
        challenge = SpotlightChallenge.builder().id("pad-bank-run").kind("game").name("Pad Bank Run").cost(10).minAge(13).status("live").active(true).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userClock.today(user)).thenReturn(today);
        when(challengeRepository.findById("pad-bank-run")).thenReturn(Optional.of(challenge));
        when(config.getPadbankBudget()).thenReturn(100);
        when(config.getPadbankHearts()).thenReturn(3);
        when(config.getPadbankDeliveryCost()).thenReturn(10);
        when(config.getPadbankBudgetGoal()).thenReturn(40);
        when(config.getPadbankTitleGreat()).thenReturn("PERFECT!");
        when(config.getPadbankTitleOk()).thenReturn("GOOD!");
        when(config.getPadbankTitleOops()).thenReturn("OOPS!");
        when(visitorRepository.activeDays()).thenReturn(List.of(1, 2));
        when(packRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(
                PadBankPack.builder().id("starter").name("Starter Bag").cost(10).stock(4).color("pink").build(),
                PadBankPack.builder().id("comfort").name("Comfort Kit").cost(20).stock(1).color("rose").build()));
        when(packRepository.findById("starter")).thenReturn(Optional.of(PadBankPack.builder().id("starter").cost(10).build()));
        when(packRepository.findById("comfort")).thenReturn(Optional.of(PadBankPack.builder().id("comfort").cost(20).build()));
        when(visitorRepository.findByDayAndActiveTrueOrderBySortOrderAsc(1)).thenReturn(List.of(visitor("v1", "starter", "pickup"),
                visitor("v2", "comfort", "delivery")));
        when(visitorRepository.findById("v1")).thenReturn(Optional.of(visitor("v1", "starter", "pickup")));
        when(visitorRepository.findById("v2")).thenReturn(Optional.of(visitor("v2", "comfort", "delivery")));
        when(pointsWriteService.adjust(eq(userId), anyInt(), anyString(), anyString())).thenReturn(40L);
        when(runRepository.save(any())).thenAnswer(inv -> {
            PadBankRun r = inv.getArgument(0);
            if (r.getId() == null) {
                r.setId(UUID.randomUUID());
            }
            return r;
        });
    }

    PadBankRun run(int round, int hearts, String stock) {
        PadBankRun r = PadBankRun.builder().id(UUID.randomUUID()).userId(userId).day(1).visitorIds("v1,v2").round(round).hearts(hearts).budget(100)
                .served(0).rightCount(0).stock(stock).results("").status(PadBankRun.PLAYING).playedOn(today).build();
        when(runRepository.findById(r.getId())).thenReturn(Optional.of(r));
        return r;
    }

    @Test
    void openingTheBankChargesTheChallengeCostAndSendsCluesNotAnswers() {
        PadBankStartResponse start = service.start(userId, new PadBankStartRequest(1));

        verify(pointsWriteService).adjust(eq(userId), eq(-10), anyString(), eq("Pad Bank Run: day 1"));
        assertThat(start).extracting("day", "balance", "cost", "budget", "hearts", "deliveryCost", "budgetGoal").containsExactly(1, 40L, 10, 100, 3, 10, 40);
        assertThat(start.visitors()).extracting("id").containsExactly("v1", "v2");
        assertThat(start.visitors().get(0).clues()).extracting("icon").containsExactly("drop");
        assertThat(start.packs()).extracting("id", "stock").containsExactly(org.assertj.core.groups.Tuple.tuple("starter", 4),
                org.assertj.core.groups.Tuple.tuple("comfort", 1));
        assertThat(service.start(userId, null).day()).isEqualTo(1);
    }

    @Test
    void theServerPicksTheFirstDayNotYetCleared() {
        assertThat(service.nextDay(userId)).isEqualTo(1);
        when(runRepository.bestStarsByDay(userId)).thenReturn(List.<Object[]>of(new Object[]{1, 2}));
        assertThat(service.nextDay(userId)).isEqualTo(2);
        when(runRepository.bestStarsByDay(userId)).thenReturn(List.<Object[]>of(new Object[]{1, 2}, new Object[]{2, 1}));
        assertThat(service.nextDay(userId)).isEqualTo(1);
        when(runRepository.bestStarsByDay(userId)).thenReturn(List.<Object[]>of(new Object[]{1, 0}));
        assertThat(service.nextDay(userId)).isEqualTo(1);
        when(visitorRepository.activeDays()).thenReturn(List.of());
        assertThatThrownBy(() -> service.nextDay(userId)).extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
    }

    @Test
    void theFirstDayIsAFreeTryThenTheNormalPriceApplies() {
        challenge.setFreePlays(1);
        user.setPoints(0);
        when(runRepository.countByUserId(userId)).thenReturn(0L);

        PadBankStartResponse free = service.start(userId, null);
        assertThat(free).extracting("cost", "balance").containsExactly(0, 0L);
        verify(pointsWriteService, never()).adjust(eq(userId), anyInt(), anyString(), anyString());

        when(runRepository.countByUserId(userId)).thenReturn(1L);
        assertThatThrownBy(() -> service.start(userId, null)).extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
    }

    @Test
    void theBankStaysShutWhenNotLiveLockedEmptyOrTooExpensive() {
        challenge.setStatus("soon");
        assertThatThrownBy(() -> service.start(userId, null)).extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
        challenge.setStatus("live");
        challenge.setMinAge(18);
        user.setDob(LocalDate.of(2012, 1, 1));
        assertThatThrownBy(() -> service.start(userId, null)).extracting("errorCode").isEqualTo(ErrorCode.QUIZ_LOCKED);
        challenge.setMinAge(13);
        assertThatThrownBy(() -> service.start(userId, new PadBankStartRequest(2))).extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
        user.setPoints(5);
        assertThatThrownBy(() -> service.start(userId, null)).extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
        challenge.setCost(0);
        service.start(userId, null);
        verify(pointsWriteService, never()).adjust(eq(userId), eq(0), anyString(), anyString());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.start(userId, null)).isInstanceOf(ApiException.class);
    }

    @Test
    void aFullDayEndsWithStarsAndTheBadge() {
        PadBankRun r = run(0, 3, "starter:4,comfort:1");

        PadBankServeResponse first = service.serve(userId, r.getId(), new PadBankServeRequest(0, "starter", "pickup"));
        assertThat(first.title()).isEqualTo("PERFECT!");
        assertThat(first).extracting("result", "tip", "speech", "budget", "spent", "served", "right", "finished")
                .containsExactly("great", PadBankService.GREAT_TIP, "Thank you!", 90, 10, 1, 1, false);

        PadBankServeResponse last = service.serve(userId, r.getId(), new PadBankServeRequest(1, "comfort", "delivery"));
        assertThat(last).extracting("result", "budget", "spent", "finished", "stars", "badge").containsExactly("great", 60, 30, true, 3, true);
        assertThat(last.stock()).containsEntry("comfort", 0);
        assertThat(r.getStatus()).isEqualTo(PadBankRun.DONE);
        assertThatThrownBy(() -> service.serve(userId, r.getId(), new PadBankServeRequest(2, "starter", "pickup")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void theRightPackTheWrongWayIsOkAndAWrongPackCostsAHeart() {
        PadBankRun r = run(0, 1, "starter:4,comfort:1");
        PadBankServeResponse ok = service.serve(userId, r.getId(), new PadBankServeRequest(0, "starter", "Delivery"));
        assertThat(ok.title()).isEqualTo("GOOD!");
        assertThat(ok).extracting("result", "tip", "hearts", "served", "right").containsExactly("ok", "Pickup was free.", 1, 1, 0);

        PadBankServeResponse oops = service.serve(userId, r.getId(), new PadBankServeRequest(1, "starter", "pickup"));
        assertThat(oops.title()).isEqualTo("OOPS!");
        assertThat(oops).extracting("result", "speech", "hearts", "finished", "stars").containsExactly("oops", PadBankService.OOPS_SPEECH, 0, true, 0);

        PadBankRun other = run(0, 3, "starter:4,comfort:1");
        PadBankServeResponse why = service.serve(userId, other.getId(), new PadBankServeRequest(0, "comfort", "pickup"));
        assertThat(why.tip()).isEqualTo("Too much for a first period.");
    }

    @Test
    void badMovesAreRefused() {
        PadBankRun r = run(0, 3, "starter:0,comfort:1");
        assertThatThrownBy(() -> service.serve(userId, r.getId(), new PadBankServeRequest(1, "starter", "pickup")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.serve(userId, r.getId(), new PadBankServeRequest(0, "starter", "bike")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.serve(userId, r.getId(), new PadBankServeRequest(0, "mystery", "pickup")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.serve(userId, r.getId(), new PadBankServeRequest(0, "starter", "pickup")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.serve(UUID.randomUUID(), r.getId(), new PadBankServeRequest(0, "comfort", "pickup")))
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
        when(visitorRepository.findById("v1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.serve(userId, r.getId(), new PadBankServeRequest(0, "comfort", "pickup")))
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }
}
