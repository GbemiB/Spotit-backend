package com.spotit.api.reminder.push;

import java.util.List;
import java.util.Map;

public interface PushSender {
    /** Sends the messages and returns one result per message, in the same order. Never throws: a failed call yields errors. */
    List<PushResult> send(List<PushMessage> messages);

    /**
     * Looks up delivery receipts. The map holds an entry only for tickets whose receipt is ready:
     * an empty string when delivered, otherwise the error code.
     */
    Map<String, String> receipts(List<String> ticketIds);
}
