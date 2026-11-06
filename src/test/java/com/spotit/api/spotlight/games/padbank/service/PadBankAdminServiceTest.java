package com.spotit.api.spotlight.games.padbank.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.games.padbank.dto.PadBankClue;
import com.spotit.api.spotlight.games.padbank.dto.PadBankPackAdmin;
import com.spotit.api.spotlight.games.padbank.dto.PadBankVisitorAdmin;
import com.spotit.api.spotlight.games.padbank.entity.PadBankPack;
import com.spotit.api.spotlight.games.padbank.entity.PadBankVisitor;
import com.spotit.api.spotlight.games.padbank.repository.PadBankPackRepository;
import com.spotit.api.spotlight.games.padbank.repository.PadBankVisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PadBankAdminServiceTest {
    @Mock PadBankPackRepository packRepository;
    @Mock PadBankVisitorRepository visitorRepository;
    @Mock ConfigurationDomainService config;
    @InjectMocks PadBankAdminService service;

    @BeforeEach
    void setUp() {
        when(packRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(visitorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(config.getSpotlightAdminPageSize()).thenReturn(50);
        when(packRepository.existsById("starter")).thenReturn(true);
    }

    @Test
    void packsCanBeListedAddedAndRestocked() {
        when(packRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(PadBankPack.builder().id("starter").name("Starter Bag").cost(10).build()));
        assertThat(service.packs(null, null).items()).extracting("id").containsExactly("starter");

        PadBankPackAdmin created = service.savePack("school", new PadBankPackAdmin(null, " School Kit ", 15, null, null, null, null, null, null));
        assertThat(created).extracting("name", "cost", "stock", "color", "fromDay", "active").containsExactly("School Kit", 15, 1, "pink", 1, true);

        PadBankPack existing = PadBankPack.builder().id("school").name("School Kit").cost(15).stock(3).color("peach").sortOrder(2).active(true).build();
        when(packRepository.findById("school")).thenReturn(Optional.of(existing));
        PadBankPackAdmin patched = service.savePack("school", new PadBankPackAdmin(null, null, -2, -1, "gold",
                List.of(" 10 day pads ", "", "Guide|book"), 0, 5, false));
        assertThat(patched).extracting("cost", "stock", "color", "fromDay", "sortOrder", "active").containsExactly(0, 0, "gold", 1, 5, false);
        assertThat(patched.contents()).containsExactly("10 day pads", "Guide/book");

        assertThatThrownBy(() -> service.savePack("new", new PadBankPackAdmin(null, "New", null, null, null, null, null, null, null)))
                .isInstanceOf(ApiException.class).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void visitorsCanBeAddedAndEditedWithChecks() {
        when(visitorRepository.findAllByOrderByDayAscSortOrderAsc()).thenReturn(List.of(PadBankVisitor.builder().id("v1").day(1).who("Girl")
                .look("girl").clues("drop|First").packs("starter").route("pickup").thanks("Thanks").routeWhy("Why").wrongWhy("").active(true).build()));
        assertThat(service.visitors(0, 10).items()).extracting("id").containsExactly("v1");

        PadBankVisitorAdmin created = service.saveVisitor("v2", new PadBankVisitorAdmin(null, 2, 1, " Woman, 27 ", "woman",
                List.of(new PadBankClue("drops", "Very heavy")), List.of(" starter ", "starter"), "Delivery", "Thank you", "Delivery was right", " Each needs one ",
                Map.of("comfort", "Too much"), null));
        assertThat(created).extracting("day", "who", "route", "active").containsExactly(2, "Woman, 27", "delivery", true);
        assertThat(created.clues()).containsExactly(new PadBankClue("drops", "Very heavy"));
        assertThat(created.wrongWhy()).containsEntry("comfort", "Too much");
        assertThat(created.packs()).containsExactly("starter", "starter");
        assertThat(created.groupWhy()).isEqualTo("Each needs one");

        PadBankVisitor existing = PadBankVisitor.builder().id("v2").day(2).who("W").look("woman").clues("").packs("starter").route("pickup")
                .thanks("T").routeWhy("R").wrongWhy("").active(true).build();
        when(visitorRepository.findById("v2")).thenReturn(Optional.of(existing));
        PadBankVisitorAdmin patched = service.saveVisitor("v2", new PadBankVisitorAdmin(null, 0, 3, null, null, null, null, null, null, null, " ", null, false));
        assertThat(patched).extracting("day", "sortOrder", "groupWhy", "active").containsExactly(1, 3, null, false);

        assertThatThrownBy(() -> service.saveVisitor("v2", new PadBankVisitorAdmin(null, null, null, null, null, null, null, "bike", null, null, null, null, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.saveVisitor("v2", new PadBankVisitorAdmin(null, null, null, null, null, null, List.of("mystery"), null, null, null, null, null, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.saveVisitor("v3", new PadBankVisitorAdmin(null, null, null, "Who", null, null, null, null, null, null, null, null, null)))
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }
}
