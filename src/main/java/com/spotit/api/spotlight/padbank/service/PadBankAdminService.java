package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.spotlight.padbank.dto.PadBankPackAdmin;
import com.spotit.api.spotlight.padbank.dto.PadBankVisitorAdmin;
import com.spotit.api.spotlight.padbank.entity.PadBankPack;
import com.spotit.api.spotlight.padbank.entity.PadBankVisitor;
import com.spotit.api.spotlight.padbank.repository.PadBankPackRepository;
import com.spotit.api.spotlight.padbank.repository.PadBankVisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PadBankAdminService {
    private static final int NEW_SORT_ORDER = 100;
    private final PadBankPackRepository packRepository;
    private final PadBankVisitorRepository visitorRepository;
    private final ConfigurationDomainService config;

    @Transactional(readOnly = true)
    public AdminPage<PadBankPackAdmin> packs(Integer page, Integer size) {
        return SpotlightRules.page(packRepository.findAllByOrderBySortOrderAsc().stream().map(PadBankPackAdmin::of).toList(), page, size,
                config.getSpotlightAdminPageSize());
    }

    @Transactional
    public PadBankPackAdmin savePack(String id, PadBankPackAdmin body) {
        PadBankPack pack = packRepository.findById(id).orElse(null);
        if (pack == null) {
            requireAll(body.name(), body.cost());
            pack = PadBankPack.builder().id(id).stock(1).color("pink").sortOrder(NEW_SORT_ORDER).active(true).build();
        }
        if (body.name() != null) {
            pack.setName(body.name().strip());
        }
        if (body.cost() != null) {
            pack.setCost(Math.max(0, body.cost()));
        }
        if (body.stock() != null) {
            pack.setStock(Math.max(0, body.stock()));
        }
        if (body.color() != null) {
            pack.setColor(body.color());
        }
        if (body.sortOrder() != null) {
            pack.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            pack.setActive(body.active());
        }
        return PadBankPackAdmin.of(packRepository.save(pack));
    }

    @Transactional(readOnly = true)
    public AdminPage<PadBankVisitorAdmin> visitors(Integer page, Integer size) {
        return SpotlightRules.page(visitorRepository.findAllByOrderByDayAscSortOrderAsc().stream().map(PadBankVisitorAdmin::of).toList(), page, size,
                config.getSpotlightAdminPageSize());
    }

    @Transactional
    public PadBankVisitorAdmin saveVisitor(String id, PadBankVisitorAdmin body) {
        PadBankVisitor visitor = visitorRepository.findById(id).orElse(null);
        if (visitor == null) {
            requireAll(body.who(), body.look(), body.pack(), body.route(), body.thanks(), body.routeWhy(), body.clues());
            visitor = PadBankVisitor.builder().id(id).day(1).sortOrder(NEW_SORT_ORDER).wrongWhy("").active(true).build();
        }
        if (body.route() != null) {
            String route = body.route().strip().toLowerCase(Locale.ROOT);
            if (!PadBankVisitor.PICKUP.equals(route) && !PadBankVisitor.DELIVERY.equals(route)) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PADBANK_BAD_ROUTE);
            }
            visitor.setRoute(route);
        }
        if (body.pack() != null) {
            if (!packRepository.existsById(body.pack().strip())) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PADBANK_BAD_PACK);
            }
            visitor.setPack(body.pack().strip());
        }
        if (body.day() != null) {
            visitor.setDay(Math.max(1, body.day()));
        }
        if (body.sortOrder() != null) {
            visitor.setSortOrder(body.sortOrder());
        }
        if (body.who() != null) {
            visitor.setWho(body.who().strip());
        }
        if (body.look() != null) {
            visitor.setLook(body.look().strip());
        }
        if (body.clues() != null) {
            visitor.setClues(PadBankRules.encodeClues(body.clues()));
        }
        if (body.thanks() != null) {
            visitor.setThanks(body.thanks().strip());
        }
        if (body.routeWhy() != null) {
            visitor.setRouteWhy(body.routeWhy().strip());
        }
        if (body.wrongWhy() != null) {
            visitor.setWrongWhy(PadBankRules.encodeMap(body.wrongWhy()));
        }
        if (body.active() != null) {
            visitor.setActive(body.active());
        }
        return PadBankVisitorAdmin.of(visitorRepository.save(visitor));
    }

    private static void requireAll(Object... values) {
        if (Stream.of(values).anyMatch(Objects::isNull)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_NEW_ITEM_INCOMPLETE);
        }
    }
}
