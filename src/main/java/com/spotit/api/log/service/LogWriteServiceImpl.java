package com.spotit.api.log.service;

import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.service.PointsWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogWriteServiceImpl implements LogWriteService {

    private final CycleLogRepository cycleLogRepository;
    private final PointsWriteService pointsWriteService;

    @Override
    @Transactional
    public SaveLogResponse saveLog(UUID userId, LocalDate date, SaveLogRequest request) {
        var existing = cycleLogRepository.findByUserIdAndLogDate(userId, date);
        boolean isNewEntry = existing.isEmpty();
        CycleLog entry = existing.orElseGet(() -> CycleLog.builder().userId(userId).logDate(date).build());

        entry.setFlow(request.flow() == null ? null : FlowIntensity.valueOf(request.flow()));
        entry.setMood(request.mood() == null ? null : MoodType.valueOf(request.mood()));
        entry.setSymptoms(request.symptoms() == null ? List.of() : request.symptoms());
        entry.setNotes(request.notes());
        entry.setIntimate(request.intimate());
        cycleLogRepository.save(entry);

        var pointsResult = pointsWriteService.recordDailyLog(userId, date, isNewEntry);

        return new SaveLogResponse(
                date, request.flow(), request.mood(), entry.getSymptoms(), entry.getNotes(), entry.isIntimate(),
                pointsResult.pointsAwarded(), pointsResult.newBalance(), pointsResult.streak(), isNewEntry
        );
    }

    @Override
    @Transactional
    public void deleteLog(UUID userId, LocalDate date) {
        cycleLogRepository.deleteByUserIdAndLogDate(userId, date);
    }
}
