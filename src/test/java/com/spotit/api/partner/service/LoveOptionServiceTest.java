package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.partner.dto.CreateLoveOptionRequest;
import com.spotit.api.partner.dto.UpdateLoveOptionRequest;
import com.spotit.api.partner.entity.LoveOption;
import com.spotit.api.partner.repository.LoveOptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoveOptionServiceTest {
    @Mock LoveOptionRepository loveOptionRepository;
    @InjectMocks LoveOptionService service;

    @Test
    void aNewPromiseGetsTheNextStoredNumber() {
        when(loveOptionRepository.maxPromiseValue()).thenReturn(6);
        when(loveOptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var created = service.create(new CreateLoveOptionRequest("foot_rub", "promise", " Foot rub ", null, "🦶", null, 7, null));

        assertThat(created.storedValue()).isEqualTo(7);
        assertThat(created.label()).isEqualTo("Foot rub");
        assertThat(created.active()).isTrue();
    }

    @Test
    void aStarterHasNoStoredNumber() {
        when(loveOptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.create(new CreateLoveOptionRequest("starter_miss", "starter", "Missing you", null, null, null, 5, true))
                .storedValue()).isNull();
    }

    @Test
    void anIdCanOnlyBeUsedOnce() {
        when(loveOptionRepository.existsById("hug")).thenReturn(true);
        assertThatThrownBy(() -> service.create(new CreateLoveOptionRequest("hug", "expression", "Hug", null, "🤗", "a hug", 1, true)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void updatingChangesOnlyTheGivenFieldsAndDeletingSwitchesOff() {
        LoveOption hug = LoveOption.builder().id("hug").kind("expression").label("Hug").emoji("🤗").phrase("a hug").sortOrder(1).active(true)
                .build();
        when(loveOptionRepository.findById("hug")).thenReturn(Optional.of(hug));
        when(loveOptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var updated = service.update("hug", new UpdateLoveOptionRequest("Big hug", null, null, "a big hug", null, null));
        assertThat(updated.label()).isEqualTo("Big hug");
        assertThat(updated.emoji()).isEqualTo("🤗");
        assertThat(updated.phrase()).isEqualTo("a big hug");

        service.switchOff("hug");
        assertThat(hug.isActive()).isFalse();
        verify(loveOptionRepository, org.mockito.Mockito.times(2)).save(hug);
    }

    @Test
    void theDefaultsCoverEveryKind() {
        List<LoveOption> defaults = LoveOptionSeeder.defaults();
        assertThat(defaults).extracting(LoveOption::getKind).contains("promise", "expression", "starter", "category");
        assertThat(defaults.stream().filter(o -> o.getKind().equals("promise")).map(LoveOption::getStoredValue))
                .containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
    }
}
