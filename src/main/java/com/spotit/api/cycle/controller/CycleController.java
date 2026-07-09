package com.spotit.api.cycle.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.cycle.service.CycleReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cycle")
@RequiredArgsConstructor
public class CycleController {

    private final CycleReadService cycleReadService;

    @GetMapping("/current")
    public CycleCurrentResponse current(@CurrentUserId UUID userId) {
        return cycleReadService.getCurrent(userId);
    }

    @GetMapping("/calendar")
    public CycleCalendarResponse calendar(@CurrentUserId UUID userId,
                                           @RequestParam int year,
                                           @RequestParam int month) {
        return cycleReadService.getCalendarMonth(userId, year, month);
    }
}
