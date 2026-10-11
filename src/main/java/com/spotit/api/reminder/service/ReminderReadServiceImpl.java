package com.spotit.api.reminder.service;

import com.spotit.api.reminder.dto.NotificationInboxResponse;
import com.spotit.api.reminder.dto.NotificationItemResponse;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderReadServiceImpl implements ReminderReadService {
    private final ReminderDeliveryRepository reminderDeliveryRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationInboxResponse getInbox(UUID userId) {
        List<NotificationItemResponse> items = reminderDeliveryRepository.findTop50ByUserIdOrderBySentAtDesc(userId).stream()
                .map(d -> new NotificationItemResponse(d.getId(), d.getType().name(), d.getTitle(), d.getBody(),
                        d.getType().getScreen(), d.getSentAt(), d.isRead()))
                .toList();
        return new NotificationInboxResponse(items, reminderDeliveryRepository.countByUserIdAndReadFalse(userId));
    }
}
