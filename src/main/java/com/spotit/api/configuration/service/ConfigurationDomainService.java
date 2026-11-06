package com.spotit.api.configuration.service;

import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;

import java.util.List;

public interface ConfigurationDomainService {
    String getJwtSecret();

    long getJwtAccessTokenTtlSeconds();

    long getJwtRefreshTokenTtlSeconds();

    long getOtpTtlSeconds();

    int getCycleDefaultLength();

    int getCycleDefaultPeriodLength();

    int getPointsDailyClaim();

    int getQuizRounds();

    int getQuizLives();

    int getQuizHintCost();

    boolean isSpotlightInviteEnabled();

    int getSpotlightInviteEveryDays();

    int getSpotlightInviteMaxDismissals();

    int getQuizTopicsPageSize();

    int getGivedayDayStart();

    int getGivedayDayEnd();

    int getGivedayVisitMinutes();

    int getGivedayUnitsPerKm();

    int getGivedayGoodPicks();

    String getGivedayTitleThree();

    String getGivedayTitleTwo();

    String getGivedayTitleOne();

    String getGivedayTitleZero();

    int getPadbankFreeVisitors();

    int getPadbankFreeDays();

    int getPadbankHearts();

    int getPadbankDeliveryCost();

    int getPadbankVisitorFee();

    String getPadbankTitleGreat();

    String getPadbankTitleOk();

    String getPadbankTitleOops();

    String getQuizTitleWon();

    String getQuizTitlePerfect();

    String getQuizTitleLost();

    String getPadbankDayTitlePerfect();

    String getPadbankDayTitleDone();

    String getPadbankDayTitleLost();

    int getSpotlightAdminPageSize();

    long getAccountPurgeGraceDays();

    int getBadgeKnowYourBodyThreshold();

    int getBadgeCycleVeteranThreshold();

    int getBadgeWeekWarriorStreakThreshold();

    long getCycleHighConfidenceLogThreshold();

    int getInsightIrregularVariationThresholdDays();

    int getInsightUnusualPeriodLengthDeltaDays();

    int getInsightDefaultCycles();

    long getSubscriptionPeriodDays();

    int getLogMaxPeriodRangeDays();

    int getRewardsHistoryPageSize();

    int getContentFeedDefaultLimit();

    long getAppMinBuildAndroid();

    long getAppMinBuildIos();

    String getAppStoreUrlAndroid();

    String getAppSupportEmail();

    String getAppStoreUrlIos();

    List<GlobalConfigurationResponse> listAll();

    List<String> listGroupNames();

    List<GlobalConfigurationResponse> listByGroup(String groupName);

    GlobalConfigurationResponse getByName(String name);

    GlobalConfigurationResponse update(String name, UpdateGlobalConfigurationRequest request);
}
