package com.spotit.api.reminder.push;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.headerDoesNotExist;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ExpoPushSenderTest {
    static final String BASE = "https://exp.example/push";
    static final PushMessage MESSAGE = new PushMessage("ExponentPushToken[a]", "Period reminder",
            "Your period is predicted to start in 3 days.", Map.of("type", "period_upcoming", "screen", "home"));

    MockRestServiceServer server;
    ExpoPushSender sender;

    @BeforeEach
    void setUp() {
        sender = senderWithToken("");
    }

    private ExpoPushSender senderWithToken(String accessToken) {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        return new ExpoPushSender(builder, new PushProperties(BASE, accessToken), 0);
    }

    @Test
    void sendPostsTheMessageInExposFormatAndReturnsTheTicket() {
        server.expect(requestTo(BASE + "/send"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(headerDoesNotExist("Authorization"))
                .andExpect(jsonPath("$[0].to").value("ExponentPushToken[a]"))
                .andExpect(jsonPath("$[0].title").value("Period reminder"))
                .andExpect(jsonPath("$[0].body").value("Your period is predicted to start in 3 days."))
                .andExpect(jsonPath("$[0].data.screen").value("home"))
                .andExpect(jsonPath("$[0].channelId").value("reminders"))
                .andExpect(jsonPath("$[0].sound").value("default"))
                .andExpect(jsonPath("$[0].ttl").value(43200))
                .andRespond(withSuccess("{\"data\":[{\"status\":\"ok\",\"id\":\"ticket-1\"}]}", MediaType.APPLICATION_JSON));

        assertThat(sender.send(List.of(MESSAGE))).containsExactly(new PushResult("ExponentPushToken[a]", "ticket-1", null));
        server.verify();
    }

    @Test
    void theAccessTokenIsSentWhenOneIsConfigured() {
        sender = senderWithToken("secret-token");
        server.expect(requestTo(BASE + "/send"))
                .andExpect(header("Authorization", "Bearer secret-token"))
                .andRespond(withSuccess("{\"data\":[{\"status\":\"ok\",\"id\":\"ticket-1\"}]}", MediaType.APPLICATION_JSON));

        sender.send(List.of(MESSAGE));
        server.verify();
    }

    @Test
    void anUninstalledAppIsReportedAsDeviceGone() {
        server.expect(requestTo(BASE + "/send")).andRespond(withSuccess(
                "{\"data\":[{\"status\":\"error\",\"message\":\"not registered\",\"details\":{\"error\":\"DeviceNotRegistered\"}}]}",
                MediaType.APPLICATION_JSON));

        List<PushResult> results = sender.send(List.of(MESSAGE));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).ticketId()).isNull();
        assertThat(results.get(0).deviceGone()).isTrue();
    }

    @Test
    void largeSendsAreSplitIntoBatchesOfOneHundred() {
        List<PushMessage> messages = IntStream.range(0, 150)
                .mapToObj(i -> new PushMessage("ExponentPushToken[" + i + "]", "t", "b", Map.of())).toList();
        server.expect(requestTo(BASE + "/send")).andExpect(jsonPath("$.length()").value(100))
                .andRespond(withSuccess(tickets(0, 100), MediaType.APPLICATION_JSON));
        server.expect(requestTo(BASE + "/send")).andExpect(jsonPath("$.length()").value(50))
                .andRespond(withSuccess(tickets(100, 150), MediaType.APPLICATION_JSON));

        List<PushResult> results = sender.send(messages);

        assertThat(results).hasSize(150);
        assertThat(results.get(0)).isEqualTo(new PushResult("ExponentPushToken[0]", "ticket-0", null));
        assertThat(results.get(149)).isEqualTo(new PushResult("ExponentPushToken[149]", "ticket-149", null));
        server.verify();
    }

    private static String tickets(int from, int to) {
        return "{\"data\":[" + String.join(",", IntStream.range(from, to)
                .mapToObj(i -> "{\"status\":\"ok\",\"id\":\"ticket-" + i + "\"}").toList()) + "]}";
    }

    @Test
    void aServerErrorIsRetried() {
        server.expect(requestTo(BASE + "/send")).andRespond(withStatus(HttpStatus.BAD_GATEWAY));
        server.expect(requestTo(BASE + "/send")).andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));
        server.expect(requestTo(BASE + "/send"))
                .andRespond(withSuccess("{\"data\":[{\"status\":\"ok\",\"id\":\"ticket-1\"}]}", MediaType.APPLICATION_JSON));

        assertThat(sender.send(List.of(MESSAGE))).containsExactly(new PushResult("ExponentPushToken[a]", "ticket-1", null));
        server.verify();
    }

    @Test
    void aSendThatKeepsFailingGivesUpAfterThreeAttemptsWithoutThrowing() {
        server.expect(ExpectedCount.times(3), requestTo(BASE + "/send")).andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        List<PushResult> results = sender.send(List.of(MESSAGE));

        assertThat(results).containsExactly(new PushResult("ExponentPushToken[a]", null, "SendFailed"));
        server.verify();
    }

    @Test
    void aRejectedRequestIsNotRetried() {
        server.expect(ExpectedCount.once(), requestTo(BASE + "/send")).andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThat(sender.send(List.of(MESSAGE))).containsExactly(new PushResult("ExponentPushToken[a]", null, "SendFailed"));
        server.verify();
    }

    @Test
    void receiptsMapDeliveredAndFailedTickets() {
        server.expect(requestTo(BASE + "/getReceipts"))
                .andExpect(jsonPath("$.ids[0]").value("ticket-1"))
                .andExpect(jsonPath("$.ids[1]").value("ticket-2"))
                .andRespond(withSuccess("{\"data\":{\"ticket-1\":{\"status\":\"ok\"},"
                        + "\"ticket-2\":{\"status\":\"error\",\"details\":{\"error\":\"DeviceNotRegistered\"}}}}", MediaType.APPLICATION_JSON));

        Map<String, String> receipts = sender.receipts(List.of("ticket-1", "ticket-2", "ticket-3"));

        assertThat(receipts).containsOnly(Map.entry("ticket-1", ""), Map.entry("ticket-2", "DeviceNotRegistered"));
    }

    @Test
    void receiptsAreEmptyWhenTheCallFails() {
        server.expect(ExpectedCount.once(), requestTo(BASE + "/getReceipts")).andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThat(sender.receipts(List.of("ticket-1"))).isEmpty();
    }

    @Test
    void aMessageMissingFromExposAnswerIsReportedAsFailed() {
        PushMessage second = new PushMessage("ExponentPushToken[b]", "t", "b", Map.of());
        server.expect(requestTo(BASE + "/send"))
                .andRespond(withSuccess("{\"data\":[{\"status\":\"ok\",\"id\":\"ticket-1\"}]}", MediaType.APPLICATION_JSON));

        assertThat(sender.send(List.of(MESSAGE, second))).containsExactly(
                new PushResult("ExponentPushToken[a]", "ticket-1", null),
                new PushResult("ExponentPushToken[b]", null, "SendFailed"));
    }

    @Test
    void anAnswerWithoutDataFailsEveryMessageWithoutThrowing() {
        server.expect(requestTo(BASE + "/send"))
                .andRespond(withSuccess("{\"errors\":[{\"code\":\"VALIDATION_ERROR\"}]}", MediaType.APPLICATION_JSON));

        assertThat(sender.send(List.of(MESSAGE))).containsExactly(new PushResult("ExponentPushToken[a]", null, "SendFailed"));
    }

    @Test
    void anErrorTicketWithoutDetailsIsStillAFailure() {
        server.expect(requestTo(BASE + "/send"))
                .andRespond(withSuccess("{\"data\":[{\"status\":\"error\",\"message\":\"boom\"}]}", MediaType.APPLICATION_JSON));

        PushResult result = sender.send(List.of(MESSAGE)).get(0);

        assertThat(result).isEqualTo(new PushResult("ExponentPushToken[a]", null, "SendFailed"));
        assertThat(result.deviceGone()).isFalse();
    }

    @Test
    void rateLimitingIsRetriedUntilItGivesUp() {
        server.expect(ExpectedCount.times(3), requestTo(BASE + "/send")).andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThat(sender.send(List.of(MESSAGE))).containsExactly(new PushResult("ExponentPushToken[a]", null, "SendFailed"));
        server.verify();
    }

    @Test
    void nothingIsCalledWhenThereIsNothingToSend() {
        assertThat(sender.send(List.of())).isEmpty();
        assertThat(sender.receipts(List.of())).isEmpty();
        server.verify();
    }

    @Test
    void receiptsAreFetchedInBatchesOfThreeHundred() {
        List<String> ids = IntStream.range(0, 301).mapToObj(i -> "ticket-" + i).toList();
        server.expect(requestTo(BASE + "/getReceipts")).andExpect(jsonPath("$.ids.length()").value(300))
                .andRespond(withSuccess("{\"data\":{\"ticket-0\":{\"status\":\"ok\"}}}", MediaType.APPLICATION_JSON));
        server.expect(requestTo(BASE + "/getReceipts")).andExpect(jsonPath("$.ids.length()").value(1))
                .andRespond(withSuccess("{\"data\":{\"ticket-300\":{\"status\":\"ok\"}}}", MediaType.APPLICATION_JSON));

        assertThat(sender.receipts(ids)).containsOnly(Map.entry("ticket-0", ""), Map.entry("ticket-300", ""));
        server.verify();
    }

    @Test
    void oneFailedReceiptBatchDoesNotLoseTheOthers() {
        List<String> ids = IntStream.range(0, 301).mapToObj(i -> "ticket-" + i).toList();
        server.expect(ExpectedCount.once(), requestTo(BASE + "/getReceipts")).andRespond(withStatus(HttpStatus.BAD_REQUEST));
        server.expect(requestTo(BASE + "/getReceipts"))
                .andRespond(withSuccess("{\"data\":{\"ticket-300\":{\"status\":\"ok\"}}}", MediaType.APPLICATION_JSON));

        assertThat(sender.receipts(ids)).containsOnly(Map.entry("ticket-300", ""));
    }

    @Test
    void aGroupedMessageCarriesCollapseIdAndTag() {
        PushMessage grouped = new PushMessage("ExponentPushToken[a]", "Tunde sent you more love ♡", "2 more today", Map.of("screen", "home"),
                "partner-love-x");
        server.expect(requestTo(BASE + "/send"))
                .andExpect(jsonPath("$[0].collapseId").value("partner-love-x"))
                .andExpect(jsonPath("$[0].tag").value("partner-love-x"))
                .andRespond(withSuccess("{\"data\":[{\"status\":\"ok\",\"id\":\"ticket-1\"}]}", MediaType.APPLICATION_JSON));

        sender.send(List.of(grouped));
        server.verify();
    }

    @Test
    void anOrdinaryMessageHasNoCollapseId() {
        server.expect(requestTo(BASE + "/send"))
                .andExpect(jsonPath("$[0].collapseId").doesNotExist())
                .andExpect(jsonPath("$[0].tag").doesNotExist())
                .andRespond(withSuccess("{\"data\":[{\"status\":\"ok\",\"id\":\"ticket-1\"}]}", MediaType.APPLICATION_JSON));

        sender.send(List.of(MESSAGE));
        server.verify();
    }
}
