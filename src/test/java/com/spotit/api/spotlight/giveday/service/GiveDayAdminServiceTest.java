package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.giveday.dto.GiveDayFact;
import com.spotit.api.spotlight.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;
import com.spotit.api.spotlight.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;
import com.spotit.api.spotlight.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayProductRepository;
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
    @Mock ConfigurationDomainService config;
    @InjectMocks GiveDayAdminService service;

    @BeforeEach
    void setUp() {
        when(placeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(needRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(config.getSpotlightAdminPageSize()).thenReturn(50);
        when(placeRepository.existsById("school")).thenReturn(true);
        when(productRepository.existsById("pads")).thenReturn(true);
    }

    @Test
    void placesCanBeAddedAndEdited() {
        when(placeRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(GiveDayPlace.builder().id("school").name("School").counts("10,20")
                .facts("girls|pink|40 girls").build()));
        assertThat(service.places(null, null).items().get(0).counts()).containsExactly(10, 20);

        GiveDayPlaceAdmin created = service.savePlace("camp", new GiveDayPlaceAdmin(null, " Hope Camp ", "Camp", "30 women", "\"Light please\"",
                "Mrs Hauwa", "woman", "women", 30, List.of(10, 20, 30), "Thanks", List.of(new GiveDayFact("tent", "peach", "Tents")), 70, 305, false,
                null, 4, null));
        assertThat(created).extracting("name", "total", "x", "y", "minAge", "active").containsExactly("Hope Camp", 30, 70, 305, 13, true);
        assertThat(created.facts()).containsExactly(new GiveDayFact("tent", "peach", "Tents"));

        GiveDayPlace existing = GiveDayPlace.builder().id("camp").name("Camp").counts("10").facts("").minAge(13).active(true).build();
        when(placeRepository.findById("camp")).thenReturn(Optional.of(existing));
        GiveDayPlaceAdmin edited = service.savePlace("camp", new GiveDayPlaceAdmin(null, null, null, null, null, null, null, null, 0, null, null, null,
                null, null, true, 18, null, false));
        assertThat(edited).extracting("total", "forSelf", "minAge", "active").containsExactly(1, true, 18, false);

        assertThatThrownBy(() -> service.savePlace("new", new GiveDayPlaceAdmin(null, "New", null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null))).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
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
