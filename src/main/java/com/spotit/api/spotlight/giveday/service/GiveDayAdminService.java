package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayLevelAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelAdmin;
import com.spotit.api.spotlight.giveday.entity.GiveDayArea;
import com.spotit.api.spotlight.giveday.entity.GiveDayLevel;
import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.giveday.entity.GiveDayTravel;
import com.spotit.api.spotlight.giveday.repository.GiveDayAreaRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayTravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GiveDayAdminService {
    private static final int NEW_SORT_ORDER = 100;
    private static final int MIN_AGE = 13;
    private final GiveDayAreaRepository areaRepository;
    private final GiveDayPlaceRepository placeRepository;
    private final GiveDayProductRepository productRepository;
    private final GiveDayNeedRepository needRepository;
    private final GiveDayLevelRepository levelRepository;
    private final GiveDayTravelRepository travelRepository;
    private final ConfigurationDomainService config;

    @Transactional(readOnly = true)
    public AdminPage<GiveDayPlaceAdmin> places(Integer page, Integer size) {
        return paged(placeRepository.findAllByOrderBySortOrderAsc().stream().map(GiveDayPlaceAdmin::of).toList(), page, size);
    }

    @Transactional
    public GiveDayPlaceAdmin savePlace(String id, GiveDayPlaceAdmin body) {
        GiveDayPlace place = placeRepository.findById(id).orElse(null);
        if (place == null) {
            requireAll(body.areaId(), body.name(), body.kind(), body.teaser(), body.hostName(), body.hostLook(), body.who(), body.total(), body.x(), body.y(),
                    body.closes());
            place = GiveDayPlace.builder().id(id).facts("").minAge(MIN_AGE).sortOrder(NEW_SORT_ORDER).active(true).build();
        }
        if (body.areaId() != null) {
            if (!areaRepository.existsById(body.areaId().strip())) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_AREA);
            }
            place.setAreaId(body.areaId().strip());
        }
        if (body.name() != null) {
            place.setName(body.name().strip());
        }
        if (body.kind() != null) {
            place.setKind(body.kind().strip());
        }
        if (body.teaser() != null) {
            place.setTeaser(body.teaser().strip());
        }
        if (body.quote() != null) {
            place.setQuote(body.quote().strip());
        }
        if (body.hostName() != null) {
            place.setHostName(body.hostName().strip());
        }
        if (body.hostLook() != null) {
            place.setHostLook(body.hostLook().strip());
        }
        if (body.who() != null) {
            place.setWho(body.who().strip());
        }
        if (body.total() != null) {
            place.setTotal(Math.max(1, body.total()));
        }
        if (body.thanks() != null) {
            place.setThanks(body.thanks().strip());
        }
        if (body.facts() != null) {
            place.setFacts(GiveDayRules.encodeFacts(body.facts()));
        }
        if (body.x() != null) {
            place.setMapX(body.x());
        }
        if (body.y() != null) {
            place.setMapY(body.y());
        }
        if (body.closes() != null) {
            place.setCloses(Math.max(0, body.closes()));
        }
        if (body.forSelf() != null) {
            place.setForSelf(body.forSelf());
        }
        if (body.minAge() != null) {
            place.setMinAge(Math.max(MIN_AGE, body.minAge()));
        }
        if (body.sortOrder() != null) {
            place.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            place.setActive(body.active());
        }
        return GiveDayPlaceAdmin.of(placeRepository.save(place));
    }

    @Transactional(readOnly = true)
    public AdminPage<GiveDayAreaAdmin> areas(Integer page, Integer size) {
        return paged(areaRepository.findAllByOrderBySortOrderAsc().stream().map(GiveDayAreaAdmin::of).toList(), page, size);
    }

    @Transactional
    public GiveDayAreaAdmin saveArea(String id, GiveDayAreaAdmin body) {
        GiveDayArea area = areaRepository.findById(id).orElse(null);
        if (area == null) {
            requireAll(body.name(), body.region(), body.x(), body.y());
            area = GiveDayArea.builder().id(id).sortOrder(NEW_SORT_ORDER).active(true).build();
        }
        if (body.name() != null) {
            area.setName(body.name().strip());
        }
        if (body.region() != null) {
            area.setRegion(body.region().strip());
        }
        if (body.x() != null) {
            area.setMapX(body.x());
        }
        if (body.y() != null) {
            area.setMapY(body.y());
        }
        if (body.sortOrder() != null) {
            area.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            area.setActive(body.active());
        }
        return GiveDayAreaAdmin.of(areaRepository.save(area));
    }

    @Transactional(readOnly = true)
    public AdminPage<GiveDayProductAdmin> products(Integer page, Integer size) {
        return paged(productRepository.findAllByOrderBySortOrderAsc().stream().map(GiveDayProductAdmin::of).toList(), page, size);
    }

    @Transactional
    public GiveDayProductAdmin saveProduct(String id, GiveDayProductAdmin body) {
        GiveDayProduct product = productRepository.findById(id).orElse(null);
        if (product == null) {
            requireAll(body.name(), body.cost());
            product = GiveDayProduct.builder().id(id).sortOrder(NEW_SORT_ORDER).active(true).build();
        }
        if (body.name() != null) {
            product.setName(body.name().strip());
        }
        if (body.cost() != null) {
            product.setCost(Math.max(0, body.cost()));
        }
        if (body.absorbent() != null) {
            product.setAbsorbent(body.absorbent());
        }
        if (body.sortOrder() != null) {
            product.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            product.setActive(body.active());
        }
        return GiveDayProductAdmin.of(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public AdminPage<GiveDayLevelAdmin> levels(Integer page, Integer size) {
        return paged(levelRepository.findAllByOrderByNumberAsc().stream().map(GiveDayLevelAdmin::of).toList(), page, size);
    }

    @Transactional
    public GiveDayLevelAdmin saveLevel(int number, GiveDayLevelAdmin body) {
        GiveDayLevel level = levelRepository.findById(number).orElse(null);
        if (level == null) {
            requireAll(body.name(), body.radiusKm(), body.starsNeeded(), body.giftBudget(), body.giftMax());
            level = GiveDayLevel.builder().number(Math.max(1, number)).active(true).build();
        }
        if (body.name() != null) {
            level.setName(body.name().strip());
        }
        if (body.radiusKm() != null) {
            level.setRadiusKm(Math.max(1, body.radiusKm()));
        }
        if (body.starsNeeded() != null) {
            level.setStarsNeeded(Math.max(0, body.starsNeeded()));
        }
        if (body.passCost() != null) {
            level.setPassCost(Math.max(0, body.passCost()));
        }
        if (body.giftBudget() != null) {
            level.setGiftBudget(Math.max(0, body.giftBudget()));
        }
        if (body.giftMax() != null) {
            level.setGiftMax(Math.max(0, body.giftMax()));
        }
        level.setGiftMax(Math.max(level.getGiftMax(), level.getGiftBudget()));
        if (body.extrasFree() != null) {
            level.setExtrasFree(body.extrasFree());
        }
        if (body.active() != null) {
            level.setActive(body.active());
        }
        return GiveDayLevelAdmin.of(levelRepository.save(level));
    }

    @Transactional(readOnly = true)
    public AdminPage<GiveDayTravelAdmin> travel(Integer page, Integer size) {
        return paged(travelRepository.findAllByOrderBySortOrderAsc().stream().map(GiveDayTravelAdmin::of).toList(), page, size);
    }

    @Transactional
    public GiveDayTravelAdmin saveTravel(String id, GiveDayTravelAdmin body) {
        GiveDayTravel travel = travelRepository.findById(id).orElse(null);
        if (travel == null) {
            requireAll(body.label(), body.cost(), body.unitsPerMinute());
            travel = GiveDayTravel.builder().id(id).sortOrder(NEW_SORT_ORDER).active(true).build();
        }
        if (body.label() != null) {
            travel.setLabel(body.label().strip());
        }
        if (body.cost() != null) {
            travel.setCost(Math.max(0, body.cost()));
        }
        if (body.unitsPerMinute() != null) {
            travel.setUnitsPerMinute(Math.max(1, body.unitsPerMinute()));
        }
        if (body.sortOrder() != null) {
            travel.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            travel.setActive(body.active());
        }
        return GiveDayTravelAdmin.of(travelRepository.save(travel));
    }

    @Transactional(readOnly = true)
    public AdminPage<GiveDayNeedAdmin> needs(String placeId, Integer page, Integer size) {
        List<GiveDayNeed> needs = placeId == null ? needRepository.findAllByOrderByPlaceIdAscProductIdAsc() : needRepository.findByPlaceId(placeId);
        return paged(needs.stream().map(GiveDayNeedAdmin::of).toList(), page, size);
    }

    @Transactional
    public GiveDayNeedAdmin saveNeed(String placeId, String productId, GiveDayNeedAdmin body) {
        if (!placeRepository.existsById(placeId)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_PLACE);
        }
        if (!productRepository.existsById(productId)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_PRODUCT);
        }
        String id = placeId + ":" + productId;
        GiveDayNeed need = needRepository.findById(id).orElse(null);
        if (need == null) {
            requireAll(body.rating(), body.why());
            need = GiveDayNeed.builder().id(id).placeId(placeId).productId(productId).build();
        }
        if (body.rating() != null) {
            String rating = body.rating().strip().toLowerCase(Locale.ROOT);
            if (!List.of(GiveDayNeed.GOOD, GiveDayNeed.OK, GiveDayNeed.POOR).contains(rating)) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.GIVEDAY_BAD_RATING);
            }
            need.setRating(rating);
        }
        if (body.why() != null) {
            need.setWhy(body.why().strip());
        }
        return GiveDayNeedAdmin.of(needRepository.save(need));
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
