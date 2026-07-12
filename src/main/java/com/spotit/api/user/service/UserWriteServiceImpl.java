package com.spotit.api.user.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.config.SpotItProperties;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import com.spotit.api.user.dto.*;
import com.spotit.api.user.entity.ExportJob;
import com.spotit.api.user.entity.Goal;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.ExportJobRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserWriteServiceImpl implements UserWriteService {

    private final UserRepository userRepository;
    private final ExportJobRepository exportJobRepository;
    private final CycleLogRepository cycleLogRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserChallengeProgressRepository userChallengeProgressRepository;
    private final SpotItProperties properties;

    @Override
    @Transactional
    public UserResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = requireUser(userId);

        if (request.firstName() != null && !request.firstName().isBlank()) {
            user.setFirstName(request.firstName());
        }
        if (request.lastName() != null && !request.lastName().isBlank()) {
            user.setLastName(request.lastName());
        }
        if (request.cycleLength() != null) {
            if (request.cycleLength() < 21 || request.cycleLength() > 45) {
                throw new ApiException(ErrorCode.CYCLE_LENGTH_OUT_OF_RANGE, ErrorMessage.CYCLE_LENGTH_OUT_OF_RANGE);
            }
            user.setCycleLength(request.cycleLength());
        }
        if (request.periodLength() != null) {
            if (request.periodLength() < 2 || request.periodLength() > 10) {
                throw new ApiException(ErrorCode.PERIOD_LENGTH_OUT_OF_RANGE, ErrorMessage.PERIOD_LENGTH_OUT_OF_RANGE);
            }
            user.setPeriodLength(request.periodLength());
        }
        if (request.themePref() != null) {
            user.setThemePref(ThemePref.valueOf(request.themePref()));
        }
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public NotificationPrefsResponse updateNotificationPrefs(UUID userId, UpdateNotificationPrefsRequest request) {
        User user = requireUser(userId);
        if (request.period() != null) user.setNotifPeriod(request.period());
        if (request.ovulation() != null) user.setNotifOvulation(request.ovulation());
        if (request.dailyLog() != null) user.setNotifDailyLog(request.dailyLog());
        if (request.digest() != null) user.setNotifDigest(request.digest());
        userRepository.save(user);
        return new NotificationPrefsResponse(user.isNotifPeriod(), user.isNotifOvulation(), user.isNotifDailyLog(), user.isNotifDigest());
    }

    @Override
    @Transactional
    public OnboardingResponse completeOnboarding(UUID userId, OnboardingRequest request) {
        User user = requireUser(userId);
        user.setDob(request.dob());
        user.setLastPeriodDate(request.lastPeriodDate());
        user.setGoal(resolveGoal(request.goal()));
        if (request.cycleLength() != null) {
            if (request.cycleLength() < 21 || request.cycleLength() > 45) {
                throw new ApiException(ErrorCode.CYCLE_LENGTH_OUT_OF_RANGE, ErrorMessage.CYCLE_LENGTH_OUT_OF_RANGE);
            }
            user.setCycleLength(request.cycleLength());
        }
        if (request.periodLength() != null) {
            if (request.periodLength() < 2 || request.periodLength() > 10) {
                throw new ApiException(ErrorCode.PERIOD_LENGTH_OUT_OF_RANGE, ErrorMessage.PERIOD_LENGTH_OUT_OF_RANGE);
            }
            user.setPeriodLength(request.periodLength());
        }
        user.setOnboarded(true);
        userRepository.save(user);
        return new OnboardingResponse(true, user.getCycleLength(), user.getPeriodLength(), user.getGoal().name());
    }

    private Goal resolveGoal(String goal) {
        try {
            return Goal.valueOf(goal);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.INVALID_GOAL, ErrorMessage.invalidGoal(goal));
        }
    }

    @Override
    @Transactional
    public ExportJobResponse requestExport(UUID userId) {
        requireUser(userId);
        ExportJob job = ExportJob.builder().userId(userId).status(ExportJob.ExportJobStatus.ready).build();
        job = exportJobRepository.save(job);
        return new ExportJobResponse(job.getId(), job.getStatus().name());
    }

    @Override
    @Transactional
    public ResetResponse resetAllData(UUID userId) {
        User user = requireUser(userId);

        cycleLogRepository.deleteByUserId(userId);
        pointsHistoryRepository.deleteByUserId(userId);
        userBadgeRepository.deleteByUserId(userId);
        userChallengeProgressRepository.deleteByUserId(userId);

        user.setOnboarded(false);
        user.setGoal(null);
        user.setDob(null);
        user.setLastPeriodDate(null);
        user.setCycleLength(properties.cycle().defaultCycleLength());
        user.setPeriodLength(properties.cycle().defaultPeriodLength());
        user.setThemePref(ThemePref.system);
        user.setNotifPeriod(true);
        user.setNotifOvulation(true);
        user.setNotifDailyLog(true);
        user.setNotifDigest(false);
        user.setPoints(0);
        user.setStreak(0);
        user.setLongestStreak(0);
        user.setLastLogDate(null);
        user.setLastClaimedDate(null);
        userRepository.save(user);

        return new ResetResponse("All data has been reset.", Instant.now());
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
