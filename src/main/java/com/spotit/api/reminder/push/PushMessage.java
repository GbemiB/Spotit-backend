package com.spotit.api.reminder.push;

import java.util.Map;

public record PushMessage(String to, String title, String body, Map<String, String> data, String collapseId) {
    public PushMessage(String to, String title, String body, Map<String, String> data) {
        this(to, title, body, data, null);
    }
}
