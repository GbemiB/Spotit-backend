package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.partner.dto.CreateLoveOptionRequest;
import com.spotit.api.partner.dto.LoveOptionAdminResponse;
import com.spotit.api.partner.dto.LoveOptionView;
import com.spotit.api.partner.dto.UpdateLoveOptionRequest;
import com.spotit.api.partner.entity.LoveOption;
import com.spotit.api.partner.repository.LoveOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoveOptionService {
    private final LoveOptionRepository loveOptionRepository;

    public List<LoveOptionView> active(String kind) {
        return loveOptionRepository.findByKindAndActiveTrueOrderBySortOrderAsc(kind).stream().map(LoveOptionService::view).toList();
    }

    public Map<String, LoveOption> allOf(String kind) {
        return loveOptionRepository.findByKindOrderBySortOrderAsc(kind).stream()
                .collect(Collectors.toMap(LoveOption::getId, Function.identity(), (a, b) -> a));
    }

    public Optional<LoveOption> activeOption(String kind, String id) {
        return loveOptionRepository.findById(id).filter(o -> kind.equals(o.getKind()) && o.isActive());
    }

    public Optional<LoveOption> option(String id) {
        return id == null ? Optional.empty() : loveOptionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<LoveOptionAdminResponse> listForAdmin() {
        return loveOptionRepository.findAllByOrderByKindAscSortOrderAsc().stream().map(LoveOptionService::admin).toList();
    }

    @Transactional
    public LoveOptionAdminResponse create(CreateLoveOptionRequest request) {
        if (loveOptionRepository.existsById(request.id())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.LOVE_OPTION_EXISTS);
        }
        Integer value = null;
        if (LoveOption.PROMISE.equals(request.kind())) {
            Integer max = loveOptionRepository.maxPromiseValue();
            value = max == null ? 1 : max + 1;
        }
        LoveOption option = loveOptionRepository.save(LoveOption.builder()
                .id(request.id())
                .kind(request.kind())
                .label(request.label().strip())
                .sub(request.sub())
                .emoji(request.emoji())
                .phrase(request.phrase())
                .storedValue(value)
                .sortOrder(request.sortOrder() == null ? 100 : request.sortOrder())
                .active(request.active() == null || request.active())
                .build());
        return admin(option);
    }

    @Transactional
    public LoveOptionAdminResponse update(String id, UpdateLoveOptionRequest request) {
        LoveOption option = require(id);
        if (request.label() != null) {
            option.setLabel(request.label().strip());
        }
        if (request.sub() != null) {
            option.setSub(request.sub());
        }
        if (request.emoji() != null) {
            option.setEmoji(request.emoji());
        }
        if (request.phrase() != null) {
            option.setPhrase(request.phrase());
        }
        if (request.sortOrder() != null) {
            option.setSortOrder(request.sortOrder());
        }
        if (request.active() != null) {
            option.setActive(request.active());
        }
        return admin(loveOptionRepository.save(option));
    }

    @Transactional
    public void switchOff(String id) {
        LoveOption option = require(id);
        option.setActive(false);
        loveOptionRepository.save(option);
    }

    private LoveOption require(String id) {
        return loveOptionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.LOVE_OPTION_NOT_FOUND));
    }

    static LoveOptionView view(LoveOption o) {
        return new LoveOptionView(o.getId(), o.getLabel(), o.getEmoji(), o.getSub(), o.getPhrase());
    }

    private static LoveOptionAdminResponse admin(LoveOption o) {
        return new LoveOptionAdminResponse(o.getId(), o.getKind(), o.getLabel(), o.getSub(), o.getEmoji(), o.getPhrase(), o.getStoredValue(),
                o.getSortOrder(), o.isActive());
    }
}
