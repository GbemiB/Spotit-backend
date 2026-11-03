package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;
import com.spotit.api.partner.entity.PartnerCheckIn;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerCheckInRepository;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.user.entity.AccountOwner;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerCheckInServiceImplTest {
    @Mock PartnerCheckInRepository partnerCheckInRepository;
    @Mock PartnerLinkRepository partnerLinkRepository;
    @Mock UserRepository userRepository;
    @Mock PointsWriteService pointsWriteService;
    @Mock ChallengeReadService challengeReadService;
    @Mock UserClock userClock;
    @Mock PartnerLoveNotifier partnerLoveNotifier;
    @Mock com.spotit.api.partner.repository.LoveOptionRepository loveOptionRepository;
    PartnerCheckInServiceImpl service;

    UUID partnerId = UUID.randomUUID();
    UUID ownerId = UUID.randomUUID();
    LocalDate today = LocalDate.of(2026, 10, 24);

    User partner(AccountOwner type) {
        User user = User.builder().id(partnerId).firstName("Tunde").accountType(type).points(40).streak(2).build();
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(user));
        return user;
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        org.mockito.Mockito.lenient().when(partnerCheckInRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        List<com.spotit.api.partner.entity.LoveOption> defaults = LoveOptionSeeder.defaults();
        org.mockito.Mockito.lenient().when(loveOptionRepository.findByKindAndActiveTrueOrderBySortOrderAsc(any()))
                .thenAnswer(inv -> defaults.stream().filter(o -> o.getKind().equals(inv.getArgument(0))).toList());
        org.mockito.Mockito.lenient().when(loveOptionRepository.findByKindOrderBySortOrderAsc(any()))
                .thenAnswer(inv -> defaults.stream().filter(o -> o.getKind().equals(inv.getArgument(0))).toList());
        org.mockito.Mockito.lenient().when(loveOptionRepository.findById(any()))
                .thenAnswer(inv -> defaults.stream().filter(o -> o.getId().equals(inv.getArgument(0))).findFirst());
        service = new PartnerCheckInServiceImpl(partnerCheckInRepository, partnerLinkRepository, userRepository, pointsWriteService,
                challengeReadService, userClock, partnerLoveNotifier, new LoveOptionService(loveOptionRepository));
    }

    void linked() {
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active))
                .thenReturn(Optional.of(PartnerLink.builder().ownerId(ownerId).partnerId(partnerId).build()));
    }

    void assertError(Runnable call, ErrorCode code) {
        assertThatThrownBy(call::run).isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getErrorCode()).isEqualTo(code));
    }

    @Test
    void todayListsTheWaysHeCanSupportHer() {
        User user = partner(AccountOwner.partner);
        when(userClock.today(user)).thenReturn(today);
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        PartnerCheckInResponse response = service.getToday(partnerId);

        assertThat(response.checkedInToday()).isFalse();
        assertThat(response.options()).extracting("label").containsExactly("Check on her", "Listen to her", "Tea & treats", "Back rub", "Do the chores", "Us time");
        assertThat(response.reward()).isEqualTo(10);
        assertThat(response.streak()).isEqualTo(2);
    }

    @Test
    void todayShowsWhatHeAlreadyChose() {
        User user = partner(AccountOwner.partner);
        when(userClock.today(user)).thenReturn(today);
        when(partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today))
                .thenReturn(List.of(PartnerCheckIn.builder().supports("2,6").build()));

        PartnerCheckInResponse response = service.getToday(partnerId);

        assertThat(response.checkedInToday()).isTrue();
        assertThat(response.supports()).containsExactly("snacks", "listened");
    }

    @Test
    void checkingInSavesItAndAwardsHisPoints() {
        User user = partner(AccountOwner.partner);
        linked();
        when(userClock.today(user)).thenReturn(today);
        when(pointsWriteService.recordCheckIn(partnerId)).thenReturn(new PointsWriteService.LogPointsResult(10, 50, 3, true));

        PartnerCheckInResponse response = service.checkIn(partnerId, new PartnerCheckInRequest(List.of("chores", "snacks", "chores")));

        ArgumentCaptor<PartnerCheckIn> saved = ArgumentCaptor.forClass(PartnerCheckIn.class);
        verify(partnerCheckInRepository).save(saved.capture());
        assertThat(saved.getValue().getSupports()).isEqualTo("4,2");
        assertThat(saved.getValue().getOwnerId()).isEqualTo(ownerId);
        assertThat(saved.getValue().getCheckInDate()).isEqualTo(today);
        assertThat(response.pointsAwarded()).isEqualTo(10);
        assertThat(response.newBalance()).isEqualTo(50);
        assertThat(response.streak()).isEqualTo(3);
        assertThat(response.supports()).containsExactly("chores", "snacks");
    }

    @Test
    void heCanSendAgainTheSameDay() {
        User user = partner(AccountOwner.partner);
        linked();
        when(userClock.today(user)).thenReturn(today);
        when(partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today)).thenReturn(List.of(
                PartnerCheckIn.builder().kind("promise").supports("2").build(),
                PartnerCheckIn.builder().kind("text").supports("").note("Hi").build()));
        when(pointsWriteService.recordCheckIn(partnerId)).thenReturn(new PointsWriteService.LogPointsResult(0, 50, 3, false));

        PartnerCheckInResponse response = service.checkIn(partnerId, new PartnerCheckInRequest("expression", null, null, "hug"));

        assertThat(response.pointsAwarded()).isZero();
        assertThat(response.kind()).isEqualTo("expression");
        assertThat(response.expression()).isEqualTo("hug");
        assertThat(response.sentToday()).isEqualTo(3);
        assertThat(response.sentByKind()).containsEntry("promise", 1).containsEntry("text", 1).containsEntry("expression", 1);
    }

    @Test
    void eachKindNeedsItsOwnContent() {
        partner(AccountOwner.partner);
        linked();
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest("promise", List.of(), "hi", null)), ErrorCode.VALIDATION_ERROR);
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest("text", List.of("snacks"), " ", null)), ErrorCode.VALIDATION_ERROR);
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest("expression", null, null, null)), ErrorCode.VALIDATION_ERROR);
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest("expression", null, null, "wink")), ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void aTextKeepsOnlyTheMessage() {
        User user = partner(AccountOwner.partner);
        linked();
        when(userClock.today(user)).thenReturn(today);
        when(pointsWriteService.recordCheckIn(partnerId)).thenReturn(new PointsWriteService.LogPointsResult(10, 50, 3, true));

        service.checkIn(partnerId, new PartnerCheckInRequest("text", List.of("snacks"), "Proud of you", null));

        ArgumentCaptor<PartnerCheckIn> saved = ArgumentCaptor.forClass(PartnerCheckIn.class);
        verify(partnerCheckInRepository).save(saved.capture());
        assertThat(saved.getValue().getKind()).isEqualTo("text");
        assertThat(saved.getValue().getSupports()).isEmpty();
        assertThat(saved.getValue().getNote()).isEqualTo("Proud of you");
    }

    @Test
    void anUnknownWayIsRefused() {
        partner(AccountOwner.partner);
        linked();
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of("flowers"))), ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void checkingInNeedsAnActiveLink() {
        partner(AccountOwner.partner);
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of("snacks"))), ErrorCode.NOT_FOUND);
    }

    @Test
    void anOwnerCannotUsePartnerCheckIns() {
        partner(AccountOwner.owner);
        assertError(() -> service.getToday(partnerId), ErrorCode.NOT_FOUND);
    }

    @Test
    void checkingInWithANoteSavesItAndTellsHer() {
        User user = partner(AccountOwner.partner);
        linked();
        User owner = User.builder().id(ownerId).firstName("Amaka").build();
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(userClock.today(user)).thenReturn(today);
        when(pointsWriteService.recordCheckIn(partnerId)).thenReturn(new PointsWriteService.LogPointsResult(10, 50, 3, true));

        PartnerCheckInResponse response = service.checkIn(partnerId, new PartnerCheckInRequest(List.of("listened", "snacks"), "  Proud of you  "));

        ArgumentCaptor<PartnerCheckIn> saved = ArgumentCaptor.forClass(PartnerCheckIn.class);
        verify(partnerCheckInRepository).save(saved.capture());
        assertThat(saved.getValue().getNote()).isEqualTo("Proud of you");
        assertThat(response.note()).isEqualTo("Proud of you");
        verify(partnerLoveNotifier).notifyOwner(owner, user, com.spotit.api.partner.entity.LoveKind.promise, List.of("Listen to her", "Tea & treats"),
                "Proud of you", null);
    }

    @Test
    void aNoteAloneIsEnough() {
        User user = partner(AccountOwner.partner);
        linked();
        when(userClock.today(user)).thenReturn(today);
        when(pointsWriteService.recordCheckIn(partnerId)).thenReturn(new PointsWriteService.LogPointsResult(10, 50, 3, true));

        PartnerCheckInResponse response = service.checkIn(partnerId, new PartnerCheckInRequest(List.of(), "Thinking of you"));

        assertThat(response.supports()).isEmpty();
        assertThat(response.note()).isEqualTo("Thinking of you");
    }

    @Test
    void nothingToSendIsRefused() {
        partner(AccountOwner.partner);
        linked();
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of(), "   ")), ErrorCode.VALIDATION_ERROR);
        verify(partnerCheckInRepository, never()).save(any());
    }

    @Test
    void sheSeesHisCheckInsWithLabelsAndNotes() {
        LocalDate from = LocalDate.of(2026, 10, 1);
        LocalDate to = LocalDate.of(2026, 10, 31);
        when(partnerCheckInRepository.findByOwnerIdAndCheckInDateBetweenOrderByCheckInDateAscCreatedAtAsc(ownerId, from, to)).thenReturn(List.of(
                PartnerCheckIn.builder().partnerId(partnerId).ownerId(ownerId).checkInDate(today).supports("6,2").note("Proud of you").build()));
        when(userRepository.findAllById(List.of(partnerId))).thenReturn(List.of(User.builder().id(partnerId).firstName("Tunde").build()));

        var items = service.loveFor(ownerId, from, to).items();

        assertThat(items).hasSize(1);
        assertThat(items.get(0).partnerName()).isEqualTo("Tunde");
        assertThat(items.get(0).supports()).containsExactly("listened", "snacks");
        assertThat(items.get(0).labels()).containsExactly("Listen to her", "Tea & treats");
        assertThat(items.get(0).note()).isEqualTo("Proud of you");
    }

    @Test
    void aBackwardsRangeIsRefused() {
        assertError(() -> service.loveFor(ownerId, today, today.minusDays(1)), ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void theStartersAndCategoriesComeFromTheDatabase() {
        User user = partner(AccountOwner.partner);
        when(userClock.today(user)).thenReturn(today);

        PartnerCheckInResponse response = service.getToday(partnerId);

        assertThat(response.starters()).containsExactly("Proud of you", "Thinking of you", "You’ve got this", "I’m here for you");
        assertThat(response.categories()).extracting("id").containsExactly("promise", "text", "expression");
        assertThat(response.expressions()).extracting("emoji").startsWith("🤗", "😘");
    }

    @Test
    void aSwitchedOffOptionCannotBeSentButStillShowsInHerHistory() {
        partner(AccountOwner.partner);
        linked();
        com.spotit.api.partner.entity.LoveOption off = com.spotit.api.partner.entity.LoveOption.builder().id("foot_rub").kind("promise")
                .label("Foot rub").storedValue(7).active(false).build();
        when(loveOptionRepository.findById("foot_rub")).thenReturn(Optional.of(off));
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of("foot_rub"))), ErrorCode.VALIDATION_ERROR);

        List<com.spotit.api.partner.entity.LoveOption> withOff = new java.util.ArrayList<>(LoveOptionSeeder.defaults().stream()
                .filter(o -> o.getKind().equals("promise")).toList());
        withOff.add(off);
        when(loveOptionRepository.findByKindOrderBySortOrderAsc("promise")).thenReturn(withOff);
        LocalDate from = LocalDate.of(2026, 10, 1);
        when(partnerCheckInRepository.findByOwnerIdAndCheckInDateBetweenOrderByCheckInDateAscCreatedAtAsc(ownerId, from, today)).thenReturn(List.of(
                PartnerCheckIn.builder().partnerId(partnerId).ownerId(ownerId).checkInDate(today).kind("promise").supports("7,2").build()));
        assertThat(service.loveFor(ownerId, from, today).items().get(0).labels()).containsExactly("Foot rub", "Tea & treats");
    }

    @Test
    void sheRepliesWithAHeartAndANoteAndHeIsTold() {
        UUID id = UUID.randomUUID();
        PartnerCheckIn checkIn = PartnerCheckIn.builder().id(id).partnerId(partnerId).ownerId(ownerId).checkInDate(today)
                .kind("expression").supports("").expression("hug").build();
        when(partnerCheckInRepository.findById(id)).thenReturn(Optional.of(checkIn));
        User him = User.builder().id(partnerId).firstName("Tunde").build();
        User her = User.builder().id(ownerId).firstName("Amaka").build();
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(him));
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(her));

        var item = service.reply(ownerId, id, new com.spotit.api.partner.dto.LoveReplyRequest(true, "  That made my day  "));

        assertThat(item.replyHeart()).isTrue();
        assertThat(item.replyNote()).isEqualTo("That made my day");
        assertThat(item.expressionEmoji()).isEqualTo("🤗");
        assertThat(checkIn.getRepliedAt()).isNotNull();
        verify(partnerLoveNotifier).notifyPartner(org.mockito.ArgumentMatchers.eq(him), org.mockito.ArgumentMatchers.eq(her),
                org.mockito.ArgumentMatchers.eq(com.spotit.api.partner.entity.LoveKind.expression), any(), org.mockito.ArgumentMatchers.eq(true),
                org.mockito.ArgumentMatchers.eq("That made my day"));
    }

    @Test
    void sendingTheSameReplyAgainDoesNotNotifyTwice() {
        UUID id = UUID.randomUUID();
        PartnerCheckIn checkIn = PartnerCheckIn.builder().id(id).partnerId(partnerId).ownerId(ownerId).checkInDate(today)
                .kind("text").supports("").note("Hi").replyHeart(true).replyNote("Thanks").repliedAt(java.time.Instant.now()).build();
        when(partnerCheckInRepository.findById(id)).thenReturn(Optional.of(checkIn));
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(User.builder().id(partnerId).firstName("Tunde").build()));

        service.reply(ownerId, id, new com.spotit.api.partner.dto.LoveReplyRequest(true, "Thanks"));

        verify(partnerLoveNotifier, never()).notifyPartner(any(), any(), any(), any(), org.mockito.ArgumentMatchers.anyBoolean(), any());
    }

    @Test
    void clearingTheReplyRemovesIt() {
        UUID id = UUID.randomUUID();
        PartnerCheckIn checkIn = PartnerCheckIn.builder().id(id).partnerId(partnerId).ownerId(ownerId).checkInDate(today)
                .kind("text").supports("").note("Hi").replyHeart(true).repliedAt(java.time.Instant.now()).build();
        when(partnerCheckInRepository.findById(id)).thenReturn(Optional.of(checkIn));

        var item = service.reply(ownerId, id, new com.spotit.api.partner.dto.LoveReplyRequest(false, " "));

        assertThat(item.replyHeart()).isFalse();
        assertThat(checkIn.getRepliedAt()).isNull();
        verify(partnerLoveNotifier, never()).notifyPartner(any(), any(), any(), any(), org.mockito.ArgumentMatchers.anyBoolean(), any());
    }

    @Test
    void sheCannotReplyToSomeoneElsesLove() {
        UUID id = UUID.randomUUID();
        when(partnerCheckInRepository.findById(id)).thenReturn(Optional.of(PartnerCheckIn.builder().id(id).ownerId(UUID.randomUUID()).build()));
        assertError(() -> service.reply(ownerId, id, new com.spotit.api.partner.dto.LoveReplyRequest(true, null)), ErrorCode.NOT_FOUND);
    }

    @Test
    void heSeesHerLatestReplyToday() {
        User user = partner(AccountOwner.partner);
        when(userClock.today(user)).thenReturn(today);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(User.builder().id(ownerId).firstName("Amaka").build()));
        when(partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today)).thenReturn(List.of(
                PartnerCheckIn.builder().ownerId(ownerId).kind("promise").supports("2").replyHeart(true)
                        .repliedAt(java.time.Instant.parse("2026-10-24T08:00:00Z")).build(),
                PartnerCheckIn.builder().ownerId(ownerId).kind("expression").supports("").expression("hug").replyHeart(true)
                        .replyNote("That made my day").repliedAt(java.time.Instant.parse("2026-10-24T09:00:00Z")).build()));

        var reply = service.getToday(partnerId).herReply();

        assertThat(reply.name()).isEqualTo("Amaka");
        assertThat(reply.note()).isEqualTo("That made my day");
        assertThat(reply.about()).isEqualTo("your hug 🤗");
    }

    @Test
    void herLoveListCarriesQuickReplies() {
        LocalDate from = LocalDate.of(2026, 10, 1);
        when(partnerCheckInRepository.findByOwnerIdAndCheckInDateBetweenOrderByCheckInDateAscCreatedAtAsc(ownerId, from, today)).thenReturn(List.of());
        assertThat(service.loveFor(ownerId, from, today).quickReplies()).contains("That made my day 🌸");
    }
}
