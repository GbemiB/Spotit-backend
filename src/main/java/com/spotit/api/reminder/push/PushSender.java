package com.spotit.api.reminder.push;

import java.util.List;
import java.util.Map;

public interface PushSender {
    List<PushResult> send(List<PushMessage> messages);

    Map<String, String> receipts(List<String> ticketIds);
}
