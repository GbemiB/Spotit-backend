package com.spotit.api.reminder.push;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExpoPushSender implements PushSender {
    static final int SEND_BATCH = 100;
    static final int RECEIPT_BATCH = 300;
    static final String CHANNEL_ID = "reminders";
    static final int TTL_SECONDS = 12 * 60 * 60;
    private static final int ATTEMPTS = 3;
    private static final String FAILED = "SendFailed";

    private final RestClient client;
    private final long backoffMillis;

    @Autowired
    public ExpoPushSender(RestClient.Builder builder, PushProperties properties) {
        this(builder, properties, 1000);
    }

    ExpoPushSender(RestClient.Builder builder, PushProperties properties, long backoffMillis) {
        RestClient.Builder configured = builder.baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        if (properties.hasAccessToken()) {
            configured.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.expoAccessToken().trim());
        }
        this.client = configured.build();
        this.backoffMillis = backoffMillis;
    }

    @Override
    public List<PushResult> send(List<PushMessage> messages) {
        List<PushResult> results = new ArrayList<>(messages.size());
        for (int from = 0; from < messages.size(); from += SEND_BATCH) {
            results.addAll(sendBatch(messages.subList(from, Math.min(from + SEND_BATCH, messages.size()))));
        }
        return results;
    }

    private List<PushResult> sendBatch(List<PushMessage> batch) {
        List<Map<String, Object>> payload = batch.stream().map(ExpoPushSender::toPayload).toList();
        JsonNode data = post("/send", payload);
        List<PushResult> results = new ArrayList<>(batch.size());
        for (int i = 0; i < batch.size(); i++) {
            JsonNode ticket = data != null && data.isArray() && i < data.size() ? data.get(i) : null;
            results.add(toResult(batch.get(i).to(), ticket));
        }
        return results;
    }

    private static Map<String, Object> toPayload(PushMessage message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("to", message.to());
        payload.put("title", message.title());
        payload.put("body", message.body());
        payload.put("data", message.data());
        payload.put("sound", "default");
        payload.put("channelId", CHANNEL_ID);
        payload.put("priority", "high");
        payload.put("ttl", TTL_SECONDS);
        return payload;
    }

    private static PushResult toResult(String to, JsonNode ticket) {
        if (ticket == null) {
            return new PushResult(to, null, FAILED);
        }
        if ("ok".equals(ticket.path("status").asText())) {
            return new PushResult(to, ticket.path("id").asText(null), null);
        }
        return new PushResult(to, null, ticket.path("details").path("error").asText(FAILED));
    }

    @Override
    public Map<String, String> receipts(List<String> ticketIds) {
        Map<String, String> receipts = new HashMap<>();
        for (int from = 0; from < ticketIds.size(); from += RECEIPT_BATCH) {
            List<String> batch = ticketIds.subList(from, Math.min(from + RECEIPT_BATCH, ticketIds.size()));
            JsonNode data = post("/getReceipts", Map.of("ids", batch));
            if (data == null || !data.isObject()) {
                continue;
            }
            data.fields().forEachRemaining(entry -> {
                boolean ok = "ok".equals(entry.getValue().path("status").asText());
                receipts.put(entry.getKey(), ok ? "" : entry.getValue().path("details").path("error").asText(FAILED));
            });
        }
        return receipts;
    }

    private JsonNode post(String path, Object body) {
        for (int attempt = 1; attempt <= ATTEMPTS; attempt++) {
            try {
                JsonNode response = client.post().uri(path).contentType(MediaType.APPLICATION_JSON).body(body)
                        .retrieve().body(JsonNode.class);
                return response == null ? null : response.get("data");
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() != 429) {
                    log.error("Expo push {} rejected with {}", path, e.getStatusCode());
                    return null;
                }
                log.warn("Expo push {} rate limited (attempt {})", path, attempt);
            } catch (RestClientException e) {
                log.warn("Expo push {} failed (attempt {}): {}", path, attempt, e.getMessage());
            }
            if (attempt < ATTEMPTS && !pause(backoffMillis * attempt)) {
                return null;
            }
        }
        log.error("Expo push {} gave up after {} attempts", path, ATTEMPTS);
        return null;
    }

    private static boolean pause(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
