package com.spotit.api.user.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.log.entity.SymptomType;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.user.dto.ExportDataResponse;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.dto.UserResponse;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.ExportJobRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserReadServiceImpl implements UserReadService {
    private final UserRepository userRepository;
    private final ExportJobRepository exportJobRepository;
    private final CycleLogRepository cycleLogRepository;
    private final PointsHistoryRepository pointsHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile(UUID userId) {
        return UserMapper.toResponse(requireUser(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationPrefsResponse getNotificationPrefs(UUID userId) {
        User user = requireUser(userId);
        return new NotificationPrefsResponse(user.isNotifPeriod(), user.isNotifOvulation(), user.isNotifDailyLog(), user.isNotifDigest());
    }

    @Override
    @Transactional(readOnly = true)
    public ExportDataResponse getExportData(UUID userId, UUID jobId) {
        exportJobRepository.findByIdAndUserId(jobId, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.EXPORT_JOB_NOT_FOUND));

        User user = requireUser(userId);
        var logs = cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, LocalDate.of(2000, 1, 1), LocalDate.now())
                .stream()
                .<Map<String, Object>>map(l -> Map.of(
                        "date", l.getLogDate().toString(),
                        "flow", l.getFlow() == null ? "" : l.getFlow().name(),
                        "mood", l.getMood() == null ? "" : l.getMood().name(),
                        "symptoms", SymptomType.fromValues(l.getSymptoms()),
                        "notes", l.getNotes() == null ? "" : l.getNotes()))
                .toList();

        var history = pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged())
                .stream()
                .<Map<String, Object>>map(h -> Map.of(
                        "label", h.getLabel(), "delta", h.getDelta(), "date", h.getOccurredOn().toString()))
                .toList();

        return new ExportDataResponse(UserMapper.toResponse(user), logs, history);
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
