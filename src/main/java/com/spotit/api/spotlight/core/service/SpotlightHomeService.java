package com.spotit.api.spotlight.core.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.dto.ChallengeCard;
import com.spotit.api.spotlight.core.dto.RankStep;
import com.spotit.api.spotlight.core.dto.SpotlightHomeResponse;
import com.spotit.api.spotlight.core.dto.SpotlightInvite;
import com.spotit.api.spotlight.core.dto.SpotlightStats;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.entity.SpotlightRank;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.repository.SpotlightRankRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpotlightHomeService {
    private final UserRepository userRepository;
    private final UserClock userClock;
    private final ConfigurationDomainService config;
    private final SpotlightChallengeRepository challengeRepository;
    private final SpotlightRankRepository rankRepository;
    private final List<SpotlightGame> games;

    @Transactional(readOnly = true)
    public SpotlightHomeResponse home(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        LocalDate today = userClock.today(user);
        Integer age = SpotlightRules.ageOn(user.getDob(), today);
        Map<String, GameSummary> byChallenge = new HashMap<>();
        games.forEach(g -> byChallenge.put(g.challengeId(), g.summary(userId, today)));
        List<GameSummary> parts = List.copyOf(byChallenge.values());
        Set<LocalDate> days = new HashSet<>();
        parts.forEach(p -> days.addAll(p.playDays()));
        int stars = parts.stream().mapToInt(GameSummary::stars).sum();
        List<SpotlightRank> rankList = rankRepository.findByActiveTrueOrderByMinStarsAsc();
        SpotlightStats stats = new SpotlightStats(SpotlightRules.streak(days, today), stars, parts.stream().mapToInt(GameSummary::levelsWon).sum(),
                parts.stream().mapToLong(GameSummary::rightAnswers).sum(), parts.stream().mapToInt(GameSummary::bestRun).max().orElse(0),
                parts.stream().mapToInt(GameSummary::topicsStarted).sum(), SpotlightRules.week(days, today),
                SpotlightRules.rank(rankList, stars));
        List<ChallengeCard> challenges = challengeRepository.findByActiveTrueOrderBySortOrderAsc().stream()
                .map(c -> new ChallengeCard(c.getId(), c.getKind(), c.getName(), c.getBlurb(), c.getIcon(), c.getTone(),
                        SpotlightChallenge.QUIZ.equals(c.getKind()) ? null : c.getCost(), c.isCostFrom(), c.getMinAge(), c.getStatus(),
                        SpotlightRules.isLocked(c.getMinAge(), age), freePlaysLeft(c, byChallenge.get(c.getId()))))
                .toList();
        SpotlightInvite invite = new SpotlightInvite(config.isSpotlightInviteEnabled(), config.getSpotlightInviteEveryDays(),
                config.getSpotlightInviteMaxDismissals());
        return new SpotlightHomeResponse(user.getPoints(), stats, challenges, invite,
                rankList.stream().map(r -> new RankStep(r.getId(), r.getName(), r.getMinStars())).toList());
    }

    static int freePlaysLeft(SpotlightChallenge challenge, GameSummary summary) {
        long played = summary == null ? 0 : summary.plays();
        return (int) Math.max(0, challenge.getFreePlays() - played);
    }
}
