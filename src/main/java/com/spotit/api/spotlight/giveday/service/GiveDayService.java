package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceView;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductView;
import com.spotit.api.spotlight.giveday.dto.GiveDayReview;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayRunRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiveDayService {
    public static final String CHALLENGE_ID = "give-day";
    static final String SPEND_ICON = "🎁";
    static final int MISSED_SHOWN = 2;

    private final UserRepository userRepository;
    private final UserClock userClock;
    private final ConfigurationDomainService config;
    private final PointsWriteService pointsWriteService;
    private final SpotlightChallengeRepository challengeRepository;
    private final GiveDayPlaceRepository placeRepository;
    private final GiveDayProductRepository productRepository;
    private final GiveDayNeedRepository needRepository;
    private final GiveDayRunRepository runRepository;

    @Transactional
    public GiveDayStartResponse start(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        LocalDate today = userClock.today(user);
        Integer age = SpotlightRules.ageOn(user.getDob(), today);
        SpotlightChallenge challenge = challengeRepository.findById(CHALLENGE_ID)
                .filter(c -> c.isActive() && SpotlightChallenge.LIVE.equals(c.getStatus()))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.GIVEDAY_NOT_OPEN));
        if (SpotlightRules.isLocked(challenge.getMinAge(), age)) {
            throw new ApiException(ErrorCode.QUIZ_LOCKED, ErrorMessage.GIVEDAY_AGE_LOCKED);
        }
        List<GiveDayPlace> places = placeRepository.findByActiveTrueOrderBySortOrderAsc();
        List<GiveDayProduct> products = productRepository.findByActiveTrueOrderBySortOrderAsc();
        if (places.isEmpty() || products.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.GIVEDAY_EMPTY);
        }
        long balance = user.getPoints();
        int cost = runRepository.countByUserId(userId) < challenge.getFreePlays() ? 0 : challenge.getCost();
        if (cost > 0) {
            if (balance < cost) {
                throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
            }
            balance = pointsWriteService.adjust(userId, -cost, SPEND_ICON, "Give Day");
        }
        int coins = config.getGivedayStartCoins();
        GiveDayRun run = runRepository.save(GiveDayRun.builder().userId(userId).coins(coins).helped(0).visits("").status(GiveDayRun.PLAYING)
                .stars(0).spSpent(cost).playedOn(today).startedAt(Instant.now()).build());
        return new GiveDayStartResponse(run.getId(), balance, cost, coins,
                places.stream().map(p -> new GiveDayPlaceView(p.getId(), p.getName(), p.getKind(), p.getTeaser(), p.getQuote(), p.getHostName(),
                        p.getHostLook(), p.getWho(), p.getTotal(), GiveDayRules.decodeCounts(p.getCounts()), GiveDayRules.decodeFacts(p.getFacts()),
                        p.getMapX(), p.getMapY(), p.isForSelf(), SpotlightRules.isLocked(p.getMinAge(), age))).toList(),
                products.stream().map(p -> new GiveDayProductView(p.getId(), p.getName(), p.getCost(), p.isAbsorbent())).toList());
    }

    @Transactional
    public GiveDayGiveResponse give(UUID userId, UUID runId, GiveDayGiveRequest request) {
        GiveDayRun run = playing(userId, runId);
        GiveDayPlace place = placeRepository.findById(request.placeId().strip()).filter(GiveDayPlace::isActive)
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_PLACE));
        Map<String, Integer> visits = GiveDayRules.decodeVisits(run.getVisits());
        if (visits.containsKey(place.getId())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_PLACE_DONE);
        }
        int people = request.people();
        if (!GiveDayRules.decodeCounts(place.getCounts()).contains(people)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_PEOPLE);
        }
        Map<String, GiveDayProduct> shelf = productRepository.findByActiveTrueOrderBySortOrderAsc().stream()
                .collect(Collectors.toMap(GiveDayProduct::getId, Function.identity()));
        List<GiveDayProduct> picked = new ArrayList<>();
        for (String id : new LinkedHashSet<>(request.products())) {
            GiveDayProduct product = shelf.get(id == null ? "" : id.strip());
            if (product == null) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_PRODUCT);
            }
            picked.add(product);
        }
        int spent = picked.stream().mapToInt(GiveDayProduct::getCost).sum() * people;
        if (spent > run.getCoins()) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_NOT_ENOUGH_COINS);
        }
        Map<String, GiveDayNeed> needs = needRepository.findByPlaceId(place.getId()).stream()
                .collect(Collectors.toMap(GiveDayNeed::getProductId, Function.identity()));
        List<GiveDayReview> review = picked.stream().map(p -> {
            GiveDayNeed need = needs.get(p.getId());
            return new GiveDayReview(p.getId(), p.getName(), need == null ? GiveDayNeed.OK : need.getRating(), need == null ? "" : need.getWhy());
        }).toList();
        List<GiveDayReview> missed = needs.values().stream()
                .filter(n -> GiveDayNeed.GOOD.equals(n.getRating()) && picked.stream().noneMatch(p -> p.getId().equals(n.getProductId())))
                .filter(n -> shelf.containsKey(n.getProductId()))
                .limit(MISSED_SHOWN)
                .map(n -> new GiveDayReview(n.getProductId(), shelf.get(n.getProductId()).getName(), n.getRating(), n.getWhy())).toList();
        int good = (int) review.stream().filter(r -> GiveDayNeed.GOOD.equals(r.rating())).count();
        int poor = (int) review.stream().filter(r -> GiveDayNeed.POOR.equals(r.rating())).count();
        boolean absorbent = picked.stream().anyMatch(GiveDayProduct::isAbsorbent);
        int stars = GiveDayRules.stars(absorbent, good, poor, people, place.getTotal(), config.getGivedayGoodPicks());
        visits.put(place.getId(), stars);
        run.setVisits(GiveDayRules.encodeVisits(visits));
        run.setCoins(run.getCoins() - spent);
        if (!place.isForSelf()) {
            run.setHelped(run.getHelped() + people);
        }
        runRepository.save(run);
        return new GiveDayGiveResponse(place.getId(), stars, title(stars), place.getThanks(), review, missed, people, spent, run.getCoins(), run.getHelped());
    }

    @Transactional
    public GiveDayFinishResponse finish(UUID userId, UUID runId) {
        GiveDayRun run = playing(userId, runId);
        Map<String, Integer> visits = GiveDayRules.decodeVisits(run.getVisits());
        run.setStars(GiveDayRules.dayStars(visits));
        run.setStatus(GiveDayRun.DONE);
        run.setFinishedAt(Instant.now());
        runRepository.save(run);
        return new GiveDayFinishResponse(run.getStars(), run.getHelped(), run.getCoins(), visits);
    }

    private GiveDayRun playing(UUID userId, UUID runId) {
        GiveDayRun run = runRepository.findById(runId).filter(r -> r.getUserId().equals(userId))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.GIVEDAY_RUN_NOT_FOUND));
        if (!GiveDayRun.PLAYING.equals(run.getStatus())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_GAME_OVER);
        }
        return run;
    }

    private String title(int stars) {
        return switch (stars) {
            case 3 -> config.getGivedayTitleThree();
            case 2 -> config.getGivedayTitleTwo();
            case 1 -> config.getGivedayTitleOne();
            default -> config.getGivedayTitleZero();
        };
    }
}
