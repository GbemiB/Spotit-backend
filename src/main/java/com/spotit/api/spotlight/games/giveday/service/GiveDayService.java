package com.spotit.api.spotlight.games.giveday.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayAreaResponse;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayAreaView;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayHomeRequest;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayHomeResponse;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayLevelView;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayPlaceView;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayProductView;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayReview;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayTravelRequest;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayTravelResponse;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayTravelView;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayTripQuote;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayTripsResponse;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayArea;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayLevel;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayPlayer;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayTravel;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayAreaRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayPlayerRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayRunRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayTravelRepository;
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
import java.util.Set;
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
    private final GiveDayAreaRepository areaRepository;
    private final GiveDayPlaceRepository placeRepository;
    private final GiveDayProductRepository productRepository;
    private final GiveDayNeedRepository needRepository;
    private final GiveDayRunRepository runRepository;
    private final GiveDayLevelRepository levelRepository;
    private final GiveDayTravelRepository travelRepository;
    private final GiveDayPlayerRepository playerRepository;

    @Transactional
    public GiveDayStartResponse start(UUID userId) {
        User user = user(userId);
        LocalDate today = userClock.today(user);
        Integer age = SpotlightRules.ageOn(user.getDob(), today);
        SpotlightChallenge challenge = challengeRepository.findById(CHALLENGE_ID)
                .filter(c -> c.isActive() && SpotlightChallenge.LIVE.equals(c.getStatus()))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.GIVEDAY_NOT_OPEN));
        if (SpotlightRules.isLocked(challenge.getMinAge(), age)) {
            throw new ApiException(ErrorCode.QUIZ_LOCKED, ErrorMessage.GIVEDAY_AGE_LOCKED);
        }
        List<GiveDayArea> areas = areaRepository.findByActiveTrueOrderBySortOrderAsc();
        List<GiveDayPlace> places = placeRepository.findByActiveTrueOrderBySortOrderAsc();
        List<GiveDayProduct> products = productRepository.findByActiveTrueOrderBySortOrderAsc();
        List<GiveDayLevel> levels = levelRepository.findByActiveTrueOrderByNumberAsc();
        List<GiveDayTravel> travel = travelRepository.findByActiveTrueOrderBySortOrderAsc();
        if (areas.isEmpty() || places.isEmpty() || products.isEmpty() || levels.isEmpty() || travel.isEmpty()) {
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
        GiveDayArea home = playerRepository.findById(userId).flatMap(p -> areaRepository.findById(p.getHomeArea())).filter(GiveDayArea::isActive)
                .orElse(null);
        int stars = playerStars(userId, null);
        GiveDayRun run = runRepository.save(GiveDayRun.builder().userId(userId).helped(0).visits("").status(GiveDayRun.PLAYING)
                .clock(config.getGivedayDayStart()).homeArea(home == null ? null : home.getId()).posX(home == null ? 0 : home.getMapX())
                .posY(home == null ? 0 : home.getMapY()).passes("").stars(0).spSpent(cost).playedOn(today).startedAt(Instant.now()).build());
        return new GiveDayStartResponse(run.getId(), balance, cost, run.getClock(), config.getGivedayDayEnd(), config.getGivedayVisitMinutes(),
                config.getGivedayUnitsPerKm(), run.getHomeArea(), run.getPosX(), run.getPosY(), stars, playerLevel(stars, levels),
                levels.stream().map(l -> new GiveDayLevelView(l.getNumber(), l.getName(), l.getRadiusKm(), l.getStarsNeeded(), l.getPassCost(),
                        l.getGiftBudget(), l.getGiftMax(), l.isExtrasFree())).toList(),
                travel.stream().map(t -> new GiveDayTravelView(t.getId(), t.getLabel(), t.getCost())).toList(),
                areaViews(home, areas, places, levels, stars, Map.of()),
                products.stream().map(p -> new GiveDayProductView(p.getId(), p.getName(), p.getCost(), p.isAbsorbent())).toList());
    }

    @Transactional
    public GiveDayHomeResponse home(UUID userId, GiveDayHomeRequest request) {
        user(userId);
        GiveDayArea home = area(request.areaId());
        GiveDayPlayer player = playerRepository.findById(userId).orElseGet(() -> GiveDayPlayer.builder().userId(userId).build());
        player.setHomeArea(home.getId());
        player.setUpdatedAt(Instant.now());
        playerRepository.save(player);
        runRepository.findByUserId(userId).stream().filter(r -> GiveDayRun.PLAYING.equals(r.getStatus()) && r.getHere() == null).forEach(r -> {
            r.setHomeArea(home.getId());
            r.setPosX(home.getMapX());
            r.setPosY(home.getMapY());
            runRepository.save(r);
        });
        List<GiveDayLevel> levels = levelRepository.findByActiveTrueOrderByNumberAsc();
        return new GiveDayHomeResponse(home.getId(), home.getMapX(), home.getMapY(), areaViews(home, areaRepository.findByActiveTrueOrderBySortOrderAsc(),
                placeRepository.findByActiveTrueOrderBySortOrderAsc(), levels, playerStars(userId, null), Map.of()));
    }

    @Transactional(readOnly = true)
    public GiveDayAreaResponse area(UUID userId, UUID runId, String areaId) {
        GiveDayRun run = playing(userId, runId);
        User user = user(userId);
        Integer age = SpotlightRules.ageOn(user.getDob(), userClock.today(user));
        GiveDayArea area = area(areaId);
        GiveDayLevel level = levelOf(run, area);
        int stars = playerStars(userId, run);
        Map<String, Integer> visits = GiveDayRules.decodeVisits(run.getVisits());
        List<GiveDayPlaceView> places = placeRepository.findByAreaIdAndActiveTrueOrderBySortOrderAsc(area.getId()).stream()
                .map(p -> new GiveDayPlaceView(p.getId(), p.getName(), p.getKind(), p.getTeaser(), p.getQuote(), p.getHostName(), p.getHostLook(),
                        p.getWho(), p.getTotal(), GiveDayRules.decodeFacts(p.getFacts()), p.getMapX(), p.getMapY(), p.getCloses(), p.isForSelf(),
                        SpotlightRules.isLocked(p.getMinAge(), age), visits.get(p.getId()))).toList();
        return new GiveDayAreaResponse(area.getId(), area.getName(), level.getNumber(), stars < level.getStarsNeeded(), level.getStarsNeeded(),
                passNeeded(run, level), level.getPassCost(), level.getGiftBudget(), level.getGiftMax(), level.isExtrasFree(), places);
    }

    @Transactional(readOnly = true)
    public GiveDayTripsResponse trips(UUID userId, UUID runId, String placeId) {
        GiveDayRun run = playing(userId, runId);
        GiveDayPlace place = place(placeId);
        GiveDayLevel level = levelOf(run, area(place.getAreaId()));
        int stars = playerStars(userId, run);
        int closesBy = closesBy(place);
        List<GiveDayTripQuote> quotes = travelRepository.findByActiveTrueOrderBySortOrderAsc().stream().map(t -> {
            int minutes = minutesTo(run, place, t);
            int arrive = run.getClock() + minutes;
            return new GiveDayTripQuote(t.getId(), t.getLabel(), t.getCost(), minutes, arrive, arrive + config.getGivedayVisitMinutes() <= closesBy);
        }).toList();
        return new GiveDayTripsResponse(place.getId(), run.getClock(), place.getCloses(), level.getNumber(), stars >= level.getStarsNeeded(),
                level.getStarsNeeded(), passNeeded(run, level), level.getPassCost(), level.getGiftBudget(), level.getGiftMax(), level.isExtrasFree(),
                quotes);
    }

    @Transactional
    public GiveDayTravelResponse travel(UUID userId, UUID runId, GiveDayTravelRequest request) {
        GiveDayRun run = playing(userId, runId);
        GiveDayPlace place = place(request.placeId());
        if (GiveDayRules.decodeVisits(run.getVisits()).containsKey(place.getId())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_PLACE_DONE);
        }
        GiveDayTravel mode = travelRepository.findById(request.mode().strip()).filter(GiveDayTravel::isActive)
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_TRAVEL));
        GiveDayLevel level = levelOf(run, area(place.getAreaId()));
        if (playerStars(userId, run) < level.getStarsNeeded()) {
            throw new ApiException(ErrorCode.QUIZ_LOCKED, ErrorMessage.GIVEDAY_LEVEL_LOCKED);
        }
        int minutes = minutesTo(run, place, mode);
        int arrive = run.getClock() + minutes;
        if (arrive + config.getGivedayVisitMinutes() > closesBy(place)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_CLOSED);
        }
        boolean buyPass = passNeeded(run, level);
        int spent = level.isExtrasFree() ? 0 : mode.getCost() + (buyPass ? level.getPassCost() : 0);
        long balance = user(userId).getPoints();
        if (spent > 0) {
            if (balance < spent) {
                throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
            }
            balance = pointsWriteService.adjust(userId, -spent, SPEND_ICON, "Give Day: " + mode.getLabel() + (buyPass ? " + level " + level.getNumber() + " pass" : ""));
            run.setSpSpent(run.getSpSpent() + spent);
        }
        if (buyPass) {
            Set<Integer> passes = GiveDayRules.decodePasses(run.getPasses());
            passes.add(level.getNumber());
            run.setPasses(GiveDayRules.encodePasses(passes));
        }
        run.setClock(arrive);
        run.setPosX(place.getMapX());
        run.setPosY(place.getMapY());
        run.setHere(place.getId());
        runRepository.save(run);
        return new GiveDayTravelResponse(place.getId(), mode.getId(), minutes, arrive, run.getPosX(), run.getPosY(), spent, run.getSpSpent(), balance);
    }

    @Transactional
    public GiveDayGiveResponse give(UUID userId, UUID runId, GiveDayGiveRequest request) {
        GiveDayRun run = playing(userId, runId);
        GiveDayPlace place = place(request.placeId());
        Map<String, Integer> visits = GiveDayRules.decodeVisits(run.getVisits());
        boolean repack = Boolean.TRUE.equals(request.repack());
        if (!place.getId().equals(run.getHere())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_NOT_THERE);
        }
        if (repack && !canRepack(run, place, visits)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_NO_REPACK);
        }
        if (!repack && visits.containsKey(place.getId())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_PLACE_DONE);
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
        GiveDayLevel level = levelOf(run, area(place.getAreaId()));
        int giftCost = picked.stream().mapToInt(GiveDayProduct::getCost).sum();
        if (giftCost > Math.max(level.getGiftMax(), level.getGiftBudget())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_OVER_BUDGET);
        }
        int giftPaid = GiveDayRules.giftPaid(giftCost, level.getGiftBudget(), level.isExtrasFree());
        long balance = user(userId).getPoints();
        Map<String, Integer> paidByPlace = GiveDayRules.decodeVisits(run.getGiftsPaid());
        int refunded = repack ? paidByPlace.getOrDefault(place.getId(), 0) : 0;
        if (balance + refunded < giftPaid) {
            throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
        }
        if (refunded > 0) {
            balance = pointsWriteService.adjust(userId, refunded, SPEND_ICON, "Give Day: repack refund at " + place.getName());
            run.setSpSpent(run.getSpSpent() - refunded);
        }
        paidByPlace.put(place.getId(), giftPaid);
        run.setGiftsPaid(GiveDayRules.encodeVisits(paidByPlace));
        if (repack) {
            Set<String> done = GiveDayRules.decodeIds(run.getRepacks());
            done.add(place.getId());
            run.setRepacks(String.join(",", done));
        }
        if (giftPaid > 0) {
            if (balance < giftPaid) {
                throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
            }
            balance = pointsWriteService.adjust(userId, -giftPaid, SPEND_ICON, "Give Day: extra gifts at " + place.getName());
            run.setSpSpent(run.getSpSpent() + giftPaid);
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
        int stars = GiveDayRules.stars(absorbent, good, poor, config.getGivedayGoodPicks());
        visits.put(place.getId(), stars);
        run.setVisits(GiveDayRules.encodeVisits(visits));
        run.setClock(run.getClock() + config.getGivedayVisitMinutes());
        int people = place.isForSelf() ? 1 : place.getTotal();
        if (!place.isForSelf() && !repack) {
            run.setHelped(run.getHelped() + people);
        }
        runRepository.save(run);
        int playerStars = playerStars(userId, run);
        return new GiveDayGiveResponse(place.getId(), stars, title(stars), place.getThanks(), review, missed, people, giftCost, level.getGiftBudget(),
                giftPaid, level.isExtrasFree(), run.getSpSpent(), balance, run.getHelped(), run.getClock(), playerStars,
                playerLevel(playerStars, levelRepository.findByActiveTrueOrderByNumberAsc()), refunded, canRepack(run, place, visits));
    }

    @Transactional
    public GiveDayFinishResponse finish(UUID userId, UUID runId) {
        GiveDayRun run = playing(userId, runId);
        Map<String, Integer> visits = GiveDayRules.decodeVisits(run.getVisits());
        run.setStars(GiveDayRules.dayStars(visits));
        run.setStatus(GiveDayRun.DONE);
        run.setFinishedAt(Instant.now());
        runRepository.save(run);
        return new GiveDayFinishResponse(run.getStars(), run.getHelped(), run.getSpSpent(), visits);
    }

    int playerStars(UUID userId, GiveDayRun current) {
        List<Map<String, Integer>> days = new ArrayList<>();
        runRepository.findByUserId(userId).forEach(r -> {
            if (current == null || !r.getId().equals(current.getId())) {
                days.add(GiveDayRules.decodeVisits(r.getVisits()));
            }
        });
        if (current != null) {
            days.add(GiveDayRules.decodeVisits(current.getVisits()));
        }
        return GiveDayRules.bestStars(days);
    }

    private List<GiveDayAreaView> areaViews(GiveDayArea home, List<GiveDayArea> areas, List<GiveDayPlace> places, List<GiveDayLevel> levels, int stars,
            Map<String, Integer> visits) {
        Map<String, Long> counts = places.stream().collect(Collectors.groupingBy(GiveDayPlace::getAreaId, Collectors.counting()));
        Map<String, Long> visited = places.stream().filter(p -> visits.containsKey(p.getId()))
                .collect(Collectors.groupingBy(GiveDayPlace::getAreaId, Collectors.counting()));
        return areas.stream().map(a -> {
            GiveDayLevel level = home == null ? levels.get(0) : ring(home, a, levels);
            return new GiveDayAreaView(a.getId(), a.getName(), a.getRegion(), a.getMapX(), a.getMapY(), counts.getOrDefault(a.getId(), 0L).intValue(),
                    level.getNumber(), stars < level.getStarsNeeded(), visited.getOrDefault(a.getId(), 0L).intValue());
        }).toList();
    }

    private GiveDayLevel ring(GiveDayArea home, GiveDayArea area, List<GiveDayLevel> levels) {
        return GiveDayRules.ring(home.getMapX(), home.getMapY(), area.getMapX(), area.getMapY(), config.getGivedayUnitsPerKm(), levels,
                GiveDayLevel::getRadiusKm);
    }

    private GiveDayLevel levelOf(GiveDayRun run, GiveDayArea area) {
        if (run.getHomeArea() == null) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_NO_HOME);
        }
        List<GiveDayLevel> levels = levelRepository.findByActiveTrueOrderByNumberAsc();
        if (levels.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.GIVEDAY_EMPTY);
        }
        return ring(area(run.getHomeArea()), area, levels);
    }

    private static int playerLevel(int stars, List<GiveDayLevel> levels) {
        return GiveDayRules.level(stars, levels, GiveDayLevel::getStarsNeeded, GiveDayLevel::getNumber);
    }

    private static boolean passNeeded(GiveDayRun run, GiveDayLevel level) {
        return level.getPassCost() > 0 && !GiveDayRules.decodePasses(run.getPasses()).contains(level.getNumber());
    }

    private int minutesTo(GiveDayRun run, GiveDayPlace place, GiveDayTravel mode) {
        return GiveDayRules.minutes(GiveDayRules.distance(run.getPosX(), run.getPosY(), place.getMapX(), place.getMapY()), mode.getUnitsPerMinute());
    }

    private boolean canRepack(GiveDayRun run, GiveDayPlace place, Map<String, Integer> visits) {
        Integer stars = visits.get(place.getId());
        return stars != null && stars < GiveDayRules.MAX_STARS && !GiveDayRules.decodeIds(run.getRepacks()).contains(place.getId())
                && run.getClock() + config.getGivedayVisitMinutes() <= closesBy(place);
    }

    private int closesBy(GiveDayPlace place) {
        return Math.min(place.getCloses(), config.getGivedayDayEnd());
    }

    private GiveDayArea area(String areaId) {
        return areaRepository.findById(areaId == null ? "" : areaId.strip()).filter(GiveDayArea::isActive)
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_AREA));
    }

    private GiveDayPlace place(String placeId) {
        return placeRepository.findById(placeId == null ? "" : placeId.strip()).filter(GiveDayPlace::isActive)
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_PLACE));
    }

    private User user(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
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
