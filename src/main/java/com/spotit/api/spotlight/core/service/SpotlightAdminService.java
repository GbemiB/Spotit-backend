package com.spotit.api.spotlight.core.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.core.dto.SpotlightChallengeAdmin;
import com.spotit.api.spotlight.core.dto.SpotlightRankAdmin;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.entity.SpotlightRank;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.repository.SpotlightRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SpotlightAdminService {
    private static final int MIN_AGE = 13;
    private static final int NEW_SORT_ORDER = 100;
    private final SpotlightChallengeRepository challengeRepository;
    private final SpotlightRankRepository rankRepository;
    private final ConfigurationDomainService config;

    @Transactional(readOnly = true)
    public AdminPage<SpotlightRankAdmin> ranks(Integer page, Integer size) {
        return paged(rankRepository.findAllByOrderByMinStarsAsc().stream().map(SpotlightRankAdmin::of).toList(), page, size);
    }

    @Transactional
    public SpotlightRankAdmin saveRank(String id, SpotlightRankAdmin body) {
        SpotlightRank rank = rankRepository.findById(id).orElse(null);
        if (rank == null) {
            requireAll(body.name(), body.minStars());
            rank = SpotlightRank.builder().id(id).active(true).build();
        }
        if (body.name() != null) {
            rank.setName(body.name().strip());
        }
        if (body.minStars() != null) {
            rank.setMinStars(Math.max(0, body.minStars()));
        }
        if (body.active() != null) {
            rank.setActive(body.active());
        }
        return SpotlightRankAdmin.of(rankRepository.save(rank));
    }

    @Transactional(readOnly = true)
    public AdminPage<SpotlightChallengeAdmin> challenges(Integer page, Integer size) {
        return paged(challengeRepository.findAllByOrderBySortOrderAsc().stream().map(SpotlightChallengeAdmin::of).toList(), page, size);
    }

    @Transactional
    public SpotlightChallengeAdmin saveChallenge(String id, SpotlightChallengeAdmin body) {
        SpotlightChallenge challenge = challengeRepository.findById(id).orElse(null);
        if (challenge == null) {
            requireAll(body.kind(), body.name());
            challenge = SpotlightChallenge.builder().id(id).tone("pink").minAge(MIN_AGE).status(SpotlightChallenge.SOON).sortOrder(NEW_SORT_ORDER)
                    .active(true).build();
        }
        if (body.kind() != null) {
            challenge.setKind(oneOf(body.kind(), SpotlightChallenge.QUIZ, SpotlightChallenge.GAME));
        }
        if (body.status() != null) {
            challenge.setStatus(oneOf(body.status(), SpotlightChallenge.LIVE, SpotlightChallenge.SOON));
        }
        if (body.name() != null) {
            challenge.setName(body.name().strip());
        }
        if (body.partnerName() != null) {
            challenge.setPartnerName(body.partnerName().isBlank() ? null : body.partnerName().strip());
        }
        if (body.blurb() != null) {
            challenge.setBlurb(body.blurb().strip());
        }
        if (body.icon() != null) {
            challenge.setIcon(body.icon().isBlank() ? null : body.icon().strip());
        }
        if (body.tone() != null) {
            challenge.setTone(body.tone());
        }
        if (body.cost() != null) {
            challenge.setCost(Math.max(0, body.cost()));
        }
        if (body.costFrom() != null) {
            challenge.setCostFrom(body.costFrom());
        }
        if (body.minAge() != null) {
            challenge.setMinAge(Math.max(MIN_AGE, body.minAge()));
        }
        if (body.sortOrder() != null) {
            challenge.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            challenge.setActive(body.active());
        }
        if (body.freePlays() != null) {
            challenge.setFreePlays(Math.max(0, body.freePlays()));
        }
        return SpotlightChallengeAdmin.of(challengeRepository.save(challenge));
    }

    private static String oneOf(String value, String a, String b) {
        String v = value.strip().toLowerCase(Locale.ROOT);
        if (!v.equals(a) && !v.equals(b)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_BAD_CHALLENGE);
        }
        return v;
    }

    private <T> AdminPage<T> paged(List<T> all, Integer page, Integer size) {
        return SpotlightRules.page(all, page, size, config.getSpotlightAdminPageSize());
    }

    private static void requireAll(Object... values) {
        if (Stream.of(values).anyMatch(Objects::isNull)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_NEW_ITEM_INCOMPLETE);
        }
    }
}
