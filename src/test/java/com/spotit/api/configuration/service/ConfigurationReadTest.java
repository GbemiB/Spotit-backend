package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.configuration.ConfigPropertyCatalog;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ConfigurationReadTest {
    @Mock SecurityConfigRepository securityConfigRepository;
    @Mock GlobalConfigRepository globalConfigRepository;
    @Mock EncryptionService encryptionService;

    ConfigurationDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ConfigurationDomainServiceImpl(securityConfigRepository, globalConfigRepository, encryptionService, new ConfigPropertyCatalog());
        lenient().when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(
                SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).otpTtlSeconds(600).build()));
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID)
                .cycleDefaultLength(28).cycleDefaultPeriodLength(5).pointsDailyClaim(10)
                .accountPurgeGraceDays(30).badgeKnowYourBodyThreshold(10).badgeCycleVeteranThreshold(90)
                .badgeWeekWarriorStreakThreshold(7).cycleHighConfidenceLogThreshold(20)
                .insightIrregularVariationThresholdDays(7).insightUnusualPeriodLengthDeltaDays(2).insightDefaultCycles(6)
                .subscriptionPeriodDays(31).logMaxPeriodRangeDays(14).rewardsHistoryPageSize(20).contentFeedDefaultLimit(12)
                .build()));
    }

    @Test
    void typedGettersReturnTheStoredValues() {
        assertThat(service.getOtpTtlSeconds()).isEqualTo(600);
        assertThat(service.getCycleDefaultLength()).isEqualTo(28);
        assertThat(service.getCycleDefaultPeriodLength()).isEqualTo(5);
        assertThat(service.getPointsDailyClaim()).isEqualTo(10);
        assertThat(service.getAccountPurgeGraceDays()).isEqualTo(30);
        assertThat(service.getBadgeKnowYourBodyThreshold()).isEqualTo(10);
        assertThat(service.getBadgeCycleVeteranThreshold()).isEqualTo(90);
        assertThat(service.getBadgeWeekWarriorStreakThreshold()).isEqualTo(7);
        assertThat(service.getCycleHighConfidenceLogThreshold()).isEqualTo(20);
        assertThat(service.getInsightIrregularVariationThresholdDays()).isEqualTo(7);
        assertThat(service.getInsightUnusualPeriodLengthDeltaDays()).isEqualTo(2);
        assertThat(service.getInsightDefaultCycles()).isEqualTo(6);
        assertThat(service.getSubscriptionPeriodDays()).isEqualTo(31);
        assertThat(service.getLogMaxPeriodRangeDays()).isEqualTo(14);
        assertThat(service.getRewardsHistoryPageSize()).isEqualTo(20);
        assertThat(service.getContentFeedDefaultLimit()).isEqualTo(12);
    }

    @Test
    void listByGroupReturnsOnlyThatGroupsProperties() {
        List<GlobalConfigurationResponse> security = service.listByGroup(PropertyNames.GROUP_SECURITY);

        assertThat(security).isNotEmpty().allSatisfy(p -> assertThat(p.groupName()).isEqualTo(PropertyNames.GROUP_SECURITY));
        assertThat(security).extracting(GlobalConfigurationResponse::name).contains(PropertyNames.OTP_TTL_SECONDS);
    }

    @Test
    void unsetAppVersionSettingsTurnTheCheckOffAndUseThePlayListing() {
        assertThat(service.getAppMinBuildAndroid()).isZero();
        assertThat(service.getAppMinBuildIos()).isZero();
        assertThat(service.getAppStoreUrlAndroid()).isEqualTo("https://play.google.com/store/apps/details?id=com.spotit.app");
        assertThat(service.getAppStoreUrlIos()).isNull();
        assertThat(service.getAppSupportEmail()).isEqualTo("spotitinternational@gmail.com");
    }

    @Test
    void setAppVersionSettingsAreReturned() {
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID)
                .appMinBuildAndroid(14L).appMinBuildIos(3L)
                .appStoreUrlAndroid("market://details?id=com.spotit.app").appStoreUrlIos("https://apps.apple.com/app/id1")
                .appSupportEmail(" help@spotit.app ")
                .build()));

        assertThat(service.getAppMinBuildAndroid()).isEqualTo(14);
        assertThat(service.getAppMinBuildIos()).isEqualTo(3);
        assertThat(service.getAppStoreUrlAndroid()).isEqualTo("market://details?id=com.spotit.app");
        assertThat(service.getAppStoreUrlIos()).isEqualTo("https://apps.apple.com/app/id1");
        assertThat(service.getAppSupportEmail()).isEqualTo("help@spotit.app");
    }

    @Test
    void appSettingsAreListedInTheirOwnGroup() {
        assertThat(service.listByGroup(PropertyNames.GROUP_APP)).extracting(GlobalConfigurationResponse::name)
                .containsExactlyInAnyOrder(PropertyNames.APP_MIN_BUILD_ANDROID, PropertyNames.APP_MIN_BUILD_IOS,
                        PropertyNames.APP_STORE_URL_ANDROID, PropertyNames.APP_STORE_URL_IOS, PropertyNames.APP_SUPPORT_EMAIL);
    }

    @Test
    void spotlightInviteIsOnUntilAnAdminTurnsItOff() {
        assertThat(service.isSpotlightInviteEnabled()).isTrue();

        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID).spotlightInviteEnabled(false).spotlightInviteEveryDays(4).spotlightInviteMaxDismissals(2)
                .build()));

        assertThat(service.isSpotlightInviteEnabled()).isFalse();
        assertThat(service.getSpotlightInviteEveryDays()).isEqualTo(4);
        assertThat(service.getSpotlightInviteMaxDismissals()).isEqualTo(2);
    }

    @Test
    void giveDaySettingsAreReturned() {
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID).givedayUnitsPerKm(50).givedayDayStart(420).givedayGoodPicks(2).givedayTitleThree("Wow").build()));

        assertThat(service.getGivedayUnitsPerKm()).isEqualTo(50);
        assertThat(service.getGivedayDayStart()).isEqualTo(420);
        assertThat(service.getGivedayDayEnd()).isEqualTo(1080);
        assertThat(service.getGivedayVisitMinutes()).isEqualTo(30);
        assertThat(service.getGivedayGoodPicks()).isEqualTo(2);
        assertThat(service.getGivedayTitleThree()).isEqualTo("Wow");
        assertThat(service.getGivedayTitleTwo()).isEqualTo("Lovely work");
        assertThat(service.getGivedayTitleOne()).isEqualTo("A good start");
        assertThat(service.getGivedayTitleZero()).isEqualTo("Something important is missing");
    }

    @Test
    void padBankSettingsAreReturned() {
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID).padbankFreeVisitors(0).padbankHearts(2).padbankDeliveryCost(15).padbankVisitorFee(3).padbankTitleOops(" ").build()));

        assertThat(service.getPadbankFreeVisitors()).isZero();
        assertThat(service.getPadbankHearts()).isEqualTo(2);
        assertThat(service.getPadbankDeliveryCost()).isEqualTo(15);
        assertThat(service.getPadbankVisitorFee()).isEqualTo(3);
        assertThat(service.getPadbankTitleGreat()).isEqualTo("PERFECT!");
        assertThat(service.getPadbankTitleOk()).isEqualTo("GOOD!");
        assertThat(service.getPadbankTitleOops()).isEqualTo("OOPS!");
        assertThat(service.getPadbankDayTitlePerfect()).isEqualTo("Perfect level!");
        assertThat(service.getPadbankDayTitleDone()).isEqualTo("Bank closed");
        assertThat(service.getPadbankDayTitleLost()).isEqualTo("Out of hearts");
    }

    @Test
    void quizResultTitlesUseSavedTextOrDefaults() {
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID).quizTitleWon(" Well done! ").quizTitleLost(" ").padbankDayTitleDone("Closed").build()));

        assertThat(service.getQuizTitleWon()).isEqualTo("Well done!");
        assertThat(service.getQuizTitlePerfect()).isEqualTo("Brilliant!");
        assertThat(service.getQuizTitleLost()).isEqualTo("Out of hearts");
        assertThat(service.getPadbankDayTitleDone()).isEqualTo("Closed");
    }

    @Test
    void quizPageSizesAreReturned() {
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID).quizTopicsPageSize(6).spotlightAdminPageSize(25).build()));

        assertThat(service.getQuizTopicsPageSize()).isEqualTo(6);
        assertThat(service.getSpotlightAdminPageSize()).isEqualTo(25);
    }

    @Test
    void listByAnUnknownGroupIsEmpty() {
        assertThat(service.listByGroup("smtp")).isEmpty();
    }
}
