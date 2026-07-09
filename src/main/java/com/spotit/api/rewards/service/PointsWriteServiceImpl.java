package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
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

    private static final int DAILY_LOG_POINTS = 80;
    private static final int DAILY_CLAIM_POINTS = 50;
    private static final int WATCH_AD_POINTS = 100;

    private final UserRepository userRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    private final SpotItProperties properties;

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

        user.setStreak(streak);
        user.setLongestStreak(Math.max(streak, user.getLongestStreak()));
        user.setLastLogDate(today);
        user.setPoints(user.getPoints() + DAILY_LOG_POINTS);
        userRepository.save(user);

        recordHistory(userId, "📝", "Logged flow, mood & symptoms", DAILY_LOG_POINTS);
        return new LogPointsResult(DAILY_LOG_POINTS, user.getPoints(), streak, true);
    }

    @Override
    @Transactional
    public DailyClaimResult claimDaily(UUID userId) {
        User user = requireUser(userId);
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        if (today.equals(user.getLastClaimedDate())) {
            return new DailyClaimResult(0, user.getPoints(), true);
        }
        user.setLastClaimedDate(today);
        user.setPoints(user.getPoints() + DAILY_CLAIM_POINTS);
        userRepository.save(user);
        recordHistory(userId, "🎁", "Daily check-in bonus", DAILY_CLAIM_POINTS);
        return new DailyClaimResult(DAILY_CLAIM_POINTS, user.getPoints(), false);
    }

    @Override
    @Transactional
    public AdWatchResult watchAd(UUID userId) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        long watchedToday = pointsHistoryRepository.countByUserIdAndOccurredOnAndLabel(userId, today, "Watched a rewarded ad");
        if (watchedToday >= properties.ads().dailyLimit()) {
            throw new ApiException(ErrorCode.DAILY_AD_LIMIT_REACHED, "You've reached today's rewarded-ad limit.");
        }
        User user = requireUser(userId);
        user.setPoints(user.getPoints() + WATCH_AD_POINTS);
        userRepository.save(user);
        recordHistory(userId, "🎬", "Watched a rewarded ad", WATCH_AD_POINTS);
        return new AdWatchResult(WATCH_AD_POINTS, user.getPoints());
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
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "User not found."));
    }
}
