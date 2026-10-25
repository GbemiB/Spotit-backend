package com.spotit.api.partner.service;

import com.spotit.api.partner.entity.LoveOption;
import com.spotit.api.partner.repository.LoveOptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoveOptionSeederTest {
    @Mock LoveOptionRepository loveOptionRepository;
    @InjectMocks LoveOptionSeeder seeder;

    @Test
    void onlyMissingKindsAreFilledIn() {
        when(loveOptionRepository.findByKindOrderBySortOrderAsc(any())).thenAnswer(inv ->
                "reply".equals(inv.getArgument(0)) ? List.of() : List.of(LoveOption.builder().id("x").build()));

        seeder.seed();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<LoveOption>> saved = ArgumentCaptor.forClass(List.class);
        verify(loveOptionRepository).saveAll(saved.capture());
        assertThat(saved.getValue()).extracting(LoveOption::getKind).containsOnly("reply");
        assertThat(saved.getValue()).extracting(LoveOption::getLabel).contains("That made my day 🌸");
    }

    @Test
    void nothingIsSeededWhenEveryKindExists() {
        when(loveOptionRepository.findByKindOrderBySortOrderAsc(any())).thenReturn(List.of(LoveOption.builder().id("x").build()));

        seeder.seed();

        verify(loveOptionRepository, never()).saveAll(anyIterable());
    }
}
