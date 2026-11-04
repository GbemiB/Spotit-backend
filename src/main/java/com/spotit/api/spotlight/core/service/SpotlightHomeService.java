package com.spotit.api.spotlight.core.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
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
    static final String NAME_SLOT = "{name}";

    private final UserRepository userRepository;
    private final UserClock userClock;
    private final ConfigurationDomainService config;
    private final SpotlightChallengeRepository challengeRepository;
    private final SpotlightRankRepository rankRepository;
    private final PartnerLinkRepository partnerLinkRepository;
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
        String herName = herName(user);
        List<ChallengeCard> challenges = challengeRepository.findByActiveTrueOrderBySortOrderAsc().stream()
                .map(c -> new ChallengeCard(c.getId(), c.getKind(), nameFor(c, herName), c.getBlurb(), c.getIcon(), c.getTone(),
                        SpotlightChallenge.QUIZ.equals(c.getKind()) ? null : c.getCost(), c.isCostFrom(), c.getMinAge(), c.getStatus(),
                        SpotlightRules.isLocked(c.getMinAge(), age), freePlaysLeft(c, byChallenge.get(c.getId())),
                        starsOf(byChallenge.get(c.getId())), playsOf(byChallenge.get(c.getId()))))
                .toList();
        SpotlightInvite invite = new SpotlightInvite(config.isSpotlightInviteEnabled(), config.getSpotlightInviteEveryDays(),
                config.getSpotlightInviteMaxDismissals());
        return new SpotlightHomeResponse(user.getPoints(), stats, challenges, invite,
                rankList.stream().map(r -> new RankStep(r.getId(), r.getName(), r.getMinStars())).toList());
    }

    private String herName(User user) {
        if (user.getAccountType() == null || !user.getAccountType().isPartner()) {
            return null;
        }
        return partnerLinkRepository.findFirstByPartnerIdAndStatus(user.getId(), PartnerLinkStatus.active)
                .map(link -> nicknameOrFirstName(link))
                .orElse(null);
    }

    private String nicknameOrFirstName(PartnerLink link) {
        if (link.getPartnerNickname() != null && !link.getPartnerNickname().isBlank()) {
            return link.getPartnerNickname().strip();
        }
        return userRepository.findById(link.getOwnerId()).map(User::getFirstName).orElse(null);
    }

    static String nameFor(SpotlightChallenge challenge, String herName) {
        if (herName == null || herName.isBlank() || challenge.getPartnerName() == null || challenge.getPartnerName().isBlank()) {
            return challenge.getName();
        }
        return challenge.getPartnerName().replace(NAME_SLOT, herName);
    }

    static int starsOf(GameSummary summary) {
        return summary == null ? 0 : summary.stars();
    }

    static long playsOf(GameSummary summary) {
        return summary == null ? 0 : summary.plays();
    }

    static int freePlaysLeft(SpotlightChallenge challenge, GameSummary summary) {
        long played = summary == null ? 0 : summary.plays();
        return (int) Math.max(0, challenge.getFreePlays() - played);
    }
}
