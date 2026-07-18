package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.config.SpotItProperties;
import com.spotit.api.rewards.entity.PointsHistoryEntry;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointsWriteServiceImpl implements PointsWriteService {

    private final UserRepository userRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    private final SpotItProperties properties;
    private final ChallengeReadService challengeReadService;

    @Override
    @Transactional
    public LogPointsResult recordDailyLog(UUID userId, LocalDate logDate, boolean isNewEntry) {
        User user = requireUser(userId);
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        boolean earns = isNewEntry && logDate.equals(today);

        if (!earns) {
            return new LogPointsResult(0, user.getPoints(), user.getStreak(), isNewEntry);
        }

        LocalDate yesterday = today.minusDays(1);
        int streak = (user.getLastLogDate() != null &&
                (user.getLastLogDate().equals(yesterday) || user.getLastLogDate().equals(today)))
                ? user.getStreak() + 1 : 1;

        int points = challengeReadService.getDailyLogReward();
        user.setStreak(streak);
        user.setLongestStreak(Math.max(streak, user.getLongestStreak()));
        user.setLastLogDate(today);
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);

        recordHistory(userId, "📝", "Logged flow, mood & symptoms", points);
        return new LogPointsResult(points, user.getPoints(), streak, true);
    }

    @Override
    @Transactional
    public DailyClaimResult claimDaily(UUID userId) {
        User user = requireUser(userId);
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        if (today.equals(user.getLastClaimedDate())) {
            return new DailyClaimResult(0, user.getPoints(), true);
        }
        int points = properties.points().dailyClaim();
        user.setLastClaimedDate(today);
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
        recordHistory(userId, "🎁", "Daily check-in bonus", points);
        return new DailyClaimResult(points, user.getPoints(), false);
    }

    @Override
    @Transactional
    public AdWatchResult watchAd(UUID userId) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        long watchedToday = pointsHistoryRepository.countByUserIdAndOccurredOnAndLabel(userId, today, "Watched a rewarded ad");
        if (watchedToday >= properties.ads().dailyLimit()) {
            throw new ApiException(ErrorCode.DAILY_AD_LIMIT_REACHED, ErrorMessage.DAILY_AD_LIMIT_REACHED);
        }
        User user = requireUser(userId);
        int points = properties.points().watchAd();
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
        recordHistory(userId, "🎬", "Watched a rewarded ad", points);
        return new AdWatchResult(points, user.getPoints());
    }

    @Override
    @Transactional
    public long adjust(UUID userId, int delta, String icon, String label) {
        User user = requireUser(userId);
        user.setPoints(user.getPoints() + delta);
        userRepository.save(user);
        recordHistory(userId, icon, label, delta);
        return user.getPoints();
    }

    private void recordHistory(UUID userId, String icon, String label, int delta) {
        pointsHistoryRepository.save(PointsHistoryEntry.builder()
                .userId(userId)
                .icon(icon)
                .label(label)
                .delta(delta)
                .occurredOn(LocalDate.now(ZoneOffset.UTC))
                .build());
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
