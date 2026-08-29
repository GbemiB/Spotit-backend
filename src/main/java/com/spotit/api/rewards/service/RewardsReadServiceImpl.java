package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.dto.PointsHistoryEntryResponse;
import com.spotit.api.rewards.dto.PointsHistoryPageResponse;
import com.spotit.api.rewards.dto.RewardsSummaryResponse;
import com.spotit.api.rewards.entity.PointsHistoryEntry;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RewardsReadServiceImpl implements RewardsReadService {

    private final UserRepository userRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    private final LevelDefinitionService levelDefinitionService;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    @Transactional(readOnly = true)
    public RewardsSummaryResponse getSummary(UUID userId) {
        User user = requireUser(userId);
        LevelUtil.LevelInfo level = LevelUtil.levelFor(user.getPoints(), levelDefinitionService.getLevelDefs());
        return new RewardsSummaryResponse(
                user.getPoints(), level.name(), new RewardsSummaryResponse.LevelRange(level.lo(), level.hi()),
                level.nextLevelName(), level.pointsToNextLevel(), user.getStreak(), user.getLongestStreak()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PointsHistoryPageResponse getHistory(UUID userId, Integer limit, String cursor) {
        int pageSize = limit == null ? configurationDomainService.getRewardsHistoryPageSize() : limit;
        int offset = decodeCursor(cursor);

        List<PointsHistoryEntry> page = pointsHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(offset / pageSize, pageSize));

        List<PointsHistoryEntryResponse> entries = page.stream()
                .map(e -> new PointsHistoryEntryResponse(e.getIcon(), e.getLabel(), e.getDelta(), e.getOccurredOn()))
                .toList();

        String next = entries.size() == pageSize ? encodeCursor(offset + pageSize) : null;
        return new PointsHistoryPageResponse(entries, next);
    }

    private String encodeCursor(int offset) {
        return Base64.getEncoder().encodeToString(String.valueOf(offset).getBytes());
    }

    private int decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) return 0;
        try {
            return Integer.parseInt(new String(Base64.getDecoder().decode(cursor)));
        } catch (Exception e) {
            return 0;
        }
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
