package com.spotit.api.spotlight.games.giveday.service;

import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayAreaAdmin;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayFact;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayLevelAdmin;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.games.giveday.dto.GiveDayTravelAdmin;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayArea;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayLevel;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayTravel;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayAreaRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayTravelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GiveDayAdminServiceTest {
    @Mock GiveDayPlaceRepository placeRepository;
    @Mock GiveDayProductRepository productRepository;
    @Mock GiveDayNeedRepository needRepository;
    @Mock GiveDayAreaRepository areaRepository;
    @Mock GiveDayLevelRepository levelRepository;
    @Mock GiveDayTravelRepository travelRepository;
    @Mock ConfigurationDomainService config;
    @InjectMocks GiveDayAdminService service;

    @BeforeEach
    void setUp() {
        when(placeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(needRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(areaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(levelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(travelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(areaRepository.existsById("yaba")).thenReturn(true);
        when(config.getSpotlightAdminPageSize()).thenReturn(50);
        when(placeRepository.existsById("school")).thenReturn(true);
        when(productRepository.existsById("pads")).thenReturn(true);
    }

    @Test
    void placesCanBeAddedAndEdited() {
        when(placeRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(GiveDayPlace.builder().id("school").areaId("yaba").name("School")
                .facts("girls|pink|40 girls").build()));
        assertThat(service.places(null, null).items().get(0)).extracting("areaId", "name").containsExactly("yaba", "School");

        GiveDayPlaceAdmin created = service.savePlace("camp", new GiveDayPlaceAdmin(null, " yaba ", " Hope Camp ", "Camp", "30 women", "\"Light please\"",
                "Mrs Hauwa", "woman", "women", 30, "Thanks", List.of(new GiveDayFact("tent", "peach", "Tents")), 7600, 2600, 960, false, null, 4, null));
        assertThat(created).extracting("areaId", "name", "total", "x", "y", "closes", "minAge", "active")
                .containsExactly("yaba", "Hope Camp", 30, 7600, 2600, 960, 13, true);
        assertThat(created.facts()).containsExactly(new GiveDayFact("tent", "peach", "Tents"));

        GiveDayPlace existing = GiveDayPlace.builder().id("camp").areaId("yaba").name("Camp").facts("").minAge(13).active(true).build();
        when(placeRepository.findById("camp")).thenReturn(Optional.of(existing));
        GiveDayPlaceAdmin edited = service.savePlace("camp", new GiveDayPlaceAdmin(null, null, null, null, null, null, null, null, null, 0, null, null,
                null, null, -5, true, 18, null, false));
        assertThat(edited).extracting("total", "closes", "forSelf", "minAge", "active").containsExactly(1, 0, true, 18, false);
        assertThatThrownBy(() -> service.savePlace("camp", new GiveDayPlaceAdmin(null, "atlantis", null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null))).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);

        assertThatThrownBy(() -> service.savePlace("new", new GiveDayPlaceAdmin(null, "yaba", "New", null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null))).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void areasLevelsAndTravelCanBeManaged() {
        when(areaRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(GiveDayArea.builder().id("yaba").name("Yaba").build()));
        assertThat(service.areas(null, null).items()).extracting("id").containsExactly("yaba");
        assertThat(service.saveArea("ikeja", new GiveDayAreaAdmin(null, " Ikeja ", " Ikeja ", 7400, 1650, null, null)))
                .extracting("name", "region", "x", "y", "active").containsExactly("Ikeja", "Ikeja", 7400, 1650, true);
        when(areaRepository.findById("ikeja")).thenReturn(Optional.of(GiveDayArea.builder().id("ikeja").name("Ikeja").active(true).build()));
        assertThat(service.saveArea("ikeja", new GiveDayAreaAdmin(null, null, null, null, null, 4, false))).extracting("sortOrder", "active")
                .containsExactly(4, false);
        assertThatThrownBy(() -> service.saveArea("new", new GiveDayAreaAdmin(null, "New", null, null, null, null, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);

        when(levelRepository.findAllByOrderByNumberAsc()).thenReturn(List.of(GiveDayLevel.builder().number(1).name("Near").build()));
        assertThat(service.levels(null, null).items()).extracting("number").containsExactly(1);
        assertThat(service.saveLevel(3, new GiveDayLevelAdmin(null, " Across ", 0, 10, -1, 5, 3, true, null)))
                .extracting("number", "name", "radiusKm", "starsNeeded", "passCost", "giftBudget", "giftMax", "extrasFree", "active")
                .containsExactly(3, "Across", 1, 10, 0, 5, 5, true, true);
        when(levelRepository.findById(3)).thenReturn(Optional.of(GiveDayLevel.builder().number(3).name("Across").radiusKm(15).active(true).build()));
        assertThat(service.saveLevel(3, new GiveDayLevelAdmin(null, null, null, null, null, null, null, null, false)).active()).isFalse();
        assertThatThrownBy(() -> service.saveLevel(9, new GiveDayLevelAdmin(null, "Far", null, 1, 0, 1, 2, false, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);

        when(travelRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(GiveDayTravel.builder().id("walk").label("Walk").build()));
        assertThat(service.travel(null, null).items()).extracting("id").containsExactly("walk");
        assertThat(service.saveTravel("bike", new GiveDayTravelAdmin(null, " Bike ", -2, 0, 2, null)))
                .extracting("label", "cost", "unitsPerMinute", "sortOrder", "active").containsExactly("Bike", 0, 1, 2, true);
        when(travelRepository.findById("bike")).thenReturn(Optional.of(GiveDayTravel.builder().id("bike").label("Bike").active(true).build()));
        assertThat(service.saveTravel("bike", new GiveDayTravelAdmin(null, null, 3, 25, null, false))).extracting("cost", "unitsPerMinute", "active")
                .containsExactly(3, 25, false);
        assertThatThrownBy(() -> service.saveTravel("jet", new GiveDayTravelAdmin(null, "Jet", null, null, null, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void productsAndNeedsCanBeManaged() {
        when(productRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(GiveDayProduct.builder().id("pads").name("Pads").cost(4).build()));
        assertThat(service.products(0, 10).items()).extracting("id").containsExactly("pads");
        GiveDayProductAdmin product = service.saveProduct("cup", new GiveDayProductAdmin(null, " Cup ", -1, true, 3, null));
        assertThat(product).extracting("name", "cost", "absorbent", "active").containsExactly("Cup", 0, true, true);
        GiveDayProduct existing = GiveDayProduct.builder().id("cup").name("Cup").cost(12).sortOrder(3).active(true).build();
        when(productRepository.findById("cup")).thenReturn(Optional.of(existing));
        assertThat(service.saveProduct("cup", new GiveDayProductAdmin(null, null, null, false, 9, false))).extracting("absorbent", "sortOrder", "active")
                .containsExactly(false, 9, false);
        assertThatThrownBy(() -> service.saveProduct("new", new GiveDayProductAdmin(null, "New", null, null, null, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);

        when(needRepository.findAllByOrderByPlaceIdAscProductIdAsc()).thenReturn(List.of(GiveDayNeed.builder().placeId("school").productId("pads")
                .rating("good").why("Easy").build()));
        when(needRepository.findByPlaceId("school")).thenReturn(List.of());
        assertThat(service.needs(null, null, null).items()).extracting("rating").containsExactly("good");
        assertThat(service.needs("school", null, null).items()).isEmpty();
        GiveDayNeedAdmin need = service.saveNeed("school", "pads", new GiveDayNeedAdmin(null, null, " GOOD ", " Easy to change "));
        assertThat(need).extracting("placeId", "productId", "rating", "why").containsExactly("school", "pads", "good", "Easy to change");
        when(needRepository.findById("school:pads")).thenReturn(Optional.of(GiveDayNeed.builder().id("school:pads").placeId("school").productId("pads")
                .rating("good").why("Easy").build()));
        assertThat(service.saveNeed("school", "pads", new GiveDayNeedAdmin(null, null, null, "Quick")).why()).isEqualTo("Quick");
        assertThatThrownBy(() -> service.saveNeed("school", "pads", new GiveDayNeedAdmin(null, null, "great", null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.saveNeed("nowhere", "pads", new GiveDayNeedAdmin(null, null, "good", "x")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.saveNeed("school", "glitter", new GiveDayNeedAdmin(null, null, "good", "x")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        when(needRepository.findById("school:pads")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.saveNeed("school", "pads", new GiveDayNeedAdmin(null, null, null, "x")))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }
}
