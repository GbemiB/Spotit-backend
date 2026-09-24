package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.dto.CreateLevelDefinitionRequest;
import com.spotit.api.rewards.dto.LevelDefinitionAdminResponse;
import com.spotit.api.rewards.dto.LevelDefinitionResponse;
import com.spotit.api.rewards.dto.UpdateLevelDefinitionRequest;
import com.spotit.api.rewards.entity.LevelDefinition;
import com.spotit.api.rewards.repository.LevelDefinitionRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LevelDefinitionServiceImplTest {
    @Mock LevelDefinitionRepository levelDefinitionRepository;
    @InjectMocks LevelDefinitionServiceImpl service;

    private void stubLadder() {
        when(levelDefinitionRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(
                new LevelDefinition("blush", "Blush", 0, 500, 1),
                new LevelDefinition("petal", "Petal", 500, 2000, 2)));
    }

    @Test
    void levelDefsFollowSortOrder() {
        stubLadder();

        assertThat(service.getLevelDefs()).containsExactly(new LevelUtil.LevelDef("Blush", 0, 500), new LevelUtil.LevelDef("Petal", 500, 2000));
    }

    @Test
    void levelOrderEndsWithTheMaxLevel() {
        stubLadder();

        assertThat(service.getLevelOrder()).containsExactly("Blush", "Petal", LevelUtil.MAX_LEVEL_NAME);
    }

    @Test
    void publicLevelsExposeNameAndRange() {
        stubLadder();

        assertThat(service.getLevels()).containsExactly(new LevelDefinitionResponse("Blush", 0, 500), new LevelDefinitionResponse("Petal", 500, 2000));
    }

    @Test
    void adminListIncludesIdsAndSortOrder() {
        stubLadder();

        assertThat(service.listDefinitionsForAdmin()).first().isEqualTo(new LevelDefinitionAdminResponse("blush", "Blush", 0, 500, 1));
    }

    @Test
    void adminGetReturnsOneDefinition() {
        when(levelDefinitionRepository.findById("petal")).thenReturn(Optional.of(new LevelDefinition("petal", "Petal", 500, 2000, 2)));

        assertThat(service.getDefinitionForAdmin("petal").pointsHigh()).isEqualTo(2000);
    }

    @Test
    void adminGetForAnUnknownIdIs404() {
        when(levelDefinitionRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDefinitionForAdmin("nope"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void createStoresANewLevel() {
        when(levelDefinitionRepository.existsById("rose")).thenReturn(false);

        LevelDefinitionAdminResponse response = service.createDefinition(new CreateLevelDefinitionRequest("rose", "Rosé", 2000, 5000, 3));

        assertThat(response).isEqualTo(new LevelDefinitionAdminResponse("rose", "Rosé", 2000, 5000, 3));
        verify(levelDefinitionRepository).save(any(LevelDefinition.class));
    }

    @Test
    void createWithAnExistingIdIsRejected() {
        when(levelDefinitionRepository.existsById("petal")).thenReturn(true);

        assertThatThrownBy(() -> service.createDefinition(new CreateLevelDefinitionRequest("petal", "Petal", 500, 2000, 2)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
        verify(levelDefinitionRepository, never()).save(any());
    }

    @Test
    void updateOnlyChangesTheFieldsThatWereSent() {
        LevelDefinition def = new LevelDefinition("petal", "Petal", 500, 2000, 2);
        when(levelDefinitionRepository.findById("petal")).thenReturn(Optional.of(def));

        service.updateDefinition("petal", new UpdateLevelDefinitionRequest(null, null, 2500L, null));

        assertThat(def.getName()).isEqualTo("Petal");
        assertThat(def.getPointsLow()).isEqualTo(500);
        assertThat(def.getPointsHigh()).isEqualTo(2500);
        assertThat(def.getSortOrder()).isEqualTo(2);
    }

    @Test
    void updateCanChangeEveryField() {
        LevelDefinition def = new LevelDefinition("petal", "Petal", 500, 2000, 2);
        when(levelDefinitionRepository.findById("petal")).thenReturn(Optional.of(def));

        assertThat(service.updateDefinition("petal", new UpdateLevelDefinitionRequest("Bud", 400L, 1900L, 5)))
                .isEqualTo(new LevelDefinitionAdminResponse("petal", "Bud", 400, 1900, 5));
    }

    @Test
    void updateForAnUnknownIdIs404() {
        when(levelDefinitionRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateDefinition("nope", new UpdateLevelDefinitionRequest("x", null, null, null)))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void deleteRemovesAnExistingLevel() {
        when(levelDefinitionRepository.existsById("petal")).thenReturn(true);

        service.deleteDefinition("petal");

        verify(levelDefinitionRepository).deleteById("petal");
    }

    @Test
    void deleteForAnUnknownIdIs404() {
        when(levelDefinitionRepository.existsById("nope")).thenReturn(false);

        assertThatThrownBy(() -> service.deleteDefinition("nope")).isInstanceOf(ApiException.class);
        verify(levelDefinitionRepository, never()).deleteById(anyString());
    }
}
