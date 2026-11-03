package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.spotlight.padbank.dto.PadBankCloseResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankDayRules;
import com.spotit.api.spotlight.padbank.dto.PadBankDayTitles;
import com.spotit.api.spotlight.padbank.dto.PadBankPackView;
import com.spotit.api.spotlight.padbank.dto.PadBankServeRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankServeResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankStartRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankStartResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankVisitorView;
import com.spotit.api.spotlight.padbank.entity.PadBankPack;
import com.spotit.api.spotlight.padbank.entity.PadBankRun;
import com.spotit.api.spotlight.padbank.entity.PadBankVisitor;
import com.spotit.api.spotlight.padbank.repository.PadBankPackRepository;
import com.spotit.api.spotlight.padbank.repository.PadBankRunRepository;
import com.spotit.api.spotlight.padbank.repository.PadBankVisitorRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PadBankService {
    public static final String CHALLENGE_ID = "pad-bank-run";
    static final String SPEND_ICON = "🛍️";
    static final String GREAT_TIP = "Right pack, right way to reach her!";
    static final String OOPS_SPEECH = "Hmm… this isn’t quite what I need.";
    static final String OOPS_TIP = "Look at her needs again.";

    private final UserRepository userRepository;
    private final UserClock userClock;
    private final ConfigurationDomainService config;
    private final PointsWriteService pointsWriteService;
    private final SpotlightChallengeRepository challengeRepository;
    private final PadBankPackRepository packRepository;
    private final PadBankVisitorRepository visitorRepository;
    private final PadBankRunRepository runRepository;

    @Transactional
    public PadBankStartResponse start(UUID userId, PadBankStartRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        LocalDate today = userClock.today(user);
        SpotlightChallenge challenge = challengeRepository.findById(CHALLENGE_ID)
                .filter(c -> c.isActive() && SpotlightChallenge.LIVE.equals(c.getStatus()))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.PADBANK_NOT_OPEN));
        if (SpotlightRules.isLocked(challenge.getMinAge(), SpotlightRules.ageOn(user.getDob(), today))) {
            throw new ApiException(ErrorCode.QUIZ_LOCKED, ErrorMessage.PADBANK_AGE_LOCKED);
        }
        int day = request == null || request.day() == null ? nextDay(userId) : request.day();
        List<PadBankVisitor> visitors = visitorRepository.findByDayAndActiveTrueOrderBySortOrderAsc(day);
        List<PadBankPack> packs = packRepository.findByActiveTrueOrderBySortOrderAsc();
        if (visitors.isEmpty() || packs.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.PADBANK_DAY_EMPTY);
        }
        long balance = user.getPoints();
        int cost = runRepository.countByUserId(userId) < challenge.getFreePlays() ? 0 : challenge.getCost();
        if (cost > 0) {
            if (balance < cost) {
                throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
            }
            balance = pointsWriteService.adjust(userId, -cost, SPEND_ICON, "Pad Bank Run: day " + day);
        }
        Map<String, Integer> stock = new LinkedHashMap<>();
        packs.forEach(p -> stock.put(p.getId(), p.getStock()));
        int hearts = config.getPadbankHearts();
        PadBankRun run = runRepository.save(PadBankRun.builder().userId(userId).day(day)
                .visitorIds(String.join(",", visitors.stream().map(PadBankVisitor::getId).toList()))
                .round(0).hearts(hearts).budget(0).served(0).rightCount(0).rightPack(0).rightWay(0).stock(PadBankRules.encodeStock(stock)).results("")
                .status(PadBankRun.PLAYING).stars(0).spSpent(cost).playedOn(today).startedAt(Instant.now()).build());
        return new PadBankStartResponse(run.getId(), day, balance, cost, hearts, config.getPadbankFreeVisitors(), config.getPadbankVisitorFee(),
                config.getPadbankDeliveryCost(),
                packs.stream().map(p -> new PadBankPackView(p.getId(), p.getName(), p.getCost(), p.getStock(), p.getColor(),
                        PadBankRules.decodeLines(p.getContents()))).toList(),
                visitors.stream().map(v -> new PadBankVisitorView(v.getId(), v.getWho(), v.getLook(), PadBankRules.decodeClues(v.getClues()))).toList(),
                new PadBankDayTitles(config.getPadbankDayTitlePerfect(), config.getPadbankDayTitleDone(), config.getPadbankDayTitleLost()));
    }

    @Transactional(readOnly = true)
    public PadBankDayRules rules(UUID userId) {
        int day = nextDay(userId);
        int visitors = visitorRepository.findByDayAndActiveTrueOrderBySortOrderAsc(day).size();
        return new PadBankDayRules(day, visitors, config.getPadbankHearts(), config.getPadbankFreeVisitors(), config.getPadbankVisitorFee(),
                config.getPadbankDeliveryCost());
    }

    int nextDay(UUID userId) {
        List<Integer> days = visitorRepository.activeDays();
        if (days.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.PADBANK_DAY_EMPTY);
        }
        Set<Integer> cleared = new HashSet<>();
        for (Object[] row : runRepository.bestStarsByDay(userId)) {
            if (((Number) row[1]).intValue() > 0) {
                cleared.add(((Number) row[0]).intValue());
            }
        }
        return days.stream().filter(d -> !cleared.contains(d)).findFirst().orElse(days.get(0));
    }

    private PadBankRun playing(UUID userId, UUID runId) {
        PadBankRun run = runRepository.findById(runId).filter(r -> r.getUserId().equals(userId))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PADBANK_RUN_NOT_FOUND));
        if (!PadBankRun.PLAYING.equals(run.getStatus())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_GAME_OVER);
        }
        return run;
    }

    @Transactional
    public PadBankCloseResponse close(UUID userId, UUID runId) {
        PadBankRun run = playing(userId, runId);
        int total = run.getVisitorIds().split(",").length;
        List<Boolean> goals = PadBankRules.goals(run.getServed(), run.getRightPack(), run.getRightWay(), run.getHearts(), total);
        run.setStatus(PadBankRun.DONE);
        run.setStars(PadBankRules.stars(goals, run.getHearts()));
        run.setFinishedAt(Instant.now());
        runRepository.save(run);
        return new PadBankCloseResponse(run.getServed(), run.getRightPack(), run.getRightWay(), run.getSpSpent(), run.getStars(), goals);
    }

    @Transactional
    public PadBankServeResponse serve(UUID userId, UUID runId, PadBankServeRequest request) {
        PadBankRun run = playing(userId, runId);
        if (request.round() != run.getRound()) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_WRONG_ROUND);
        }
        String route = request.route().strip().toLowerCase(Locale.ROOT);
        if (!PadBankVisitor.PICKUP.equals(route) && !PadBankVisitor.DELIVERY.equals(route)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PADBANK_BAD_ROUTE);
        }
        Map<String, Integer> stock = PadBankRules.decodeStock(run.getStock());
        String packId = request.pack().strip();
        PadBankPack pack = packRepository.findById(packId).filter(p -> stock.containsKey(p.getId()))
                .orElseThrow(() -> new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PADBANK_BAD_PACK));
        if (stock.get(packId) <= 0) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PADBANK_OUT_OF_STOCK);
        }
        List<String> ids = Arrays.asList(run.getVisitorIds().split(","));
        PadBankVisitor visitor = visitorRepository.findById(ids.get(run.getRound()))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PADBANK_RUN_NOT_FOUND));
        int spent = PadBankRules.price(run.getRound(), config.getPadbankFreeVisitors(), config.getPadbankVisitorFee(), pack.getCost(),
                PadBankVisitor.DELIVERY.equals(route), config.getPadbankDeliveryCost());
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        long balance = user.getPoints();
        if (spent > 0) {
            if (balance < spent) {
                throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
            }
            balance = pointsWriteService.adjust(userId, -spent, SPEND_ICON, "Pad Bank Run: visitor " + (run.getRound() + 1));
            run.setSpSpent(run.getSpSpent() + spent);
        }
        boolean packOk = visitor.getPack().equals(packId);
        boolean routeOk = visitor.getRoute().equals(route);
        String result = PadBankRules.result(packOk, routeOk);
        stock.put(packId, stock.get(packId) - 1);
        run.setStock(PadBankRules.encodeStock(stock));
        run.setRightPack(run.getRightPack() + (packOk ? 1 : 0));
        run.setRightWay(run.getRightWay() + (routeOk ? 1 : 0));
        if (!PadBankRules.OOPS.equals(result)) {
            run.setServed(run.getServed() + 1);
        } else {
            run.setHearts(run.getHearts() - 1);
        }
        if (PadBankRules.GREAT.equals(result)) {
            run.setRightCount(run.getRightCount() + 1);
        }
        List<String> results = new ArrayList<>(run.getResults().isBlank() ? List.of() : Arrays.asList(run.getResults().split(",")));
        results.add(result);
        run.setResults(String.join(",", results));
        run.setRound(run.getRound() + 1);
        boolean finished = run.getHearts() <= 0 || run.getRound() >= ids.size();
        List<Boolean> goals = PadBankRules.goals(run.getServed(), run.getRightPack(), run.getRightWay(), run.getHearts(), ids.size());
        if (finished) {
            run.setStatus(PadBankRun.DONE);
            run.setStars(PadBankRules.stars(goals, run.getHearts()));
            run.setFinishedAt(Instant.now());
        }
        runRepository.save(run);
        String tip = switch (result) {
            case PadBankRules.GREAT -> GREAT_TIP;
            case PadBankRules.OK -> visitor.getRouteWhy();
            default -> PadBankRules.decodeMap(visitor.getWrongWhy()).getOrDefault(packId, OOPS_TIP);
        };
        String speech = PadBankRules.OOPS.equals(result) ? OOPS_SPEECH : visitor.getThanks();
        String title = switch (result) {
            case PadBankRules.GREAT -> config.getPadbankTitleGreat();
            case PadBankRules.OK -> config.getPadbankTitleOk();
            default -> config.getPadbankTitleOops();
        };
        return new PadBankServeResponse(run.getRound(), result, title, tip, speech, run.getHearts(), balance, spent, run.getSpSpent(), run.getServed(),
                run.getRightCount(), run.getRightPack(), run.getRightWay(), stock, finished, finished ? run.getStars() : 0, goals,
                finished && run.getRightCount() >= ids.size() && run.getHearts() > 0);
    }
}
